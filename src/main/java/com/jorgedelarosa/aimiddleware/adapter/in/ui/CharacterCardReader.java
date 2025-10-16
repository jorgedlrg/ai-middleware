package com.jorgedelarosa.aimiddleware.adapter.in.ui;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.zip.InflaterInputStream;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CharacterCardReader {
  private static final byte[] PNG_SIGNATURE =
      new byte[] {(byte) 0x89, 0x50, 0x4E, 0x47, 0x0D, 0x0A, 0x1A, 0x0A};

  private static final Set<String> PREFERRED_KEYS =
      new HashSet<>(Arrays.asList("chara", "chara_card"));

  private final ObjectMapper mapper = new ObjectMapper();

  public CharacterCardV2 read(Path pngFile) throws IOException {
    byte[] bytes = Files.readAllBytes(pngFile);
    return read(bytes);
  }

  public CharacterCardV2 read(byte[] pngBytes) throws IOException {
    Map<String, String> textEntries = extractTextChunks(pngBytes);

    // 1) Try preferred keys first
    for (String key : textEntries.keySet()) {
      if (PREFERRED_KEYS.contains(key.toLowerCase(Locale.ROOT))) {
        String value = textEntries.get(key);
        CharacterCardV2 card = parseCard(value);
        if (card != null) return card;
      }
    }

    // 2) Fallback: search all values for v2
    for (String value : textEntries.values()) {
      CharacterCardV2 card = parseCard(value);
      if (card != null) return card;
    }

    throw new IOException("No Character Card v2 JSON found in PNG.");
  }

  private CharacterCardV2 parseCard(String json) {
    if (json != null) {
      byte[] decoded = Base64.getDecoder().decode(json);
      String trimmed = new String(decoded, StandardCharsets.UTF_8).trim();
      try {
        JsonNode root = mapper.readTree(trimmed);
        // Map to POJO (unknown fields ignored)
        CharacterCardV2 card = mapper.convertValue(root, CharacterCardV2.class);
        return card;
      } catch (JsonProcessingException | IllegalArgumentException ex) {
        log.error(String.format("Can't process data:\n%s", trimmed), ex);
        return null;
      }
    } else {
      return null;
    }
  }

  public Map<String, String> extractTextChunks(byte[] pngBytes) throws IOException {
    try (ByteArrayInputStream in = new ByteArrayInputStream(pngBytes)) {
      return extractTextChunks(in);
    }
  }

  public Map<String, String> extractTextChunks(InputStream in) throws IOException {
    // Validate PNG signature
    byte[] sig = in.readNBytes(8);
    if (!Arrays.equals(sig, PNG_SIGNATURE)) {
      throw new IOException("Not a valid PNG file (bad signature).");
    }

    Map<String, String> texts = new HashMap<>();

    while (true) {
      byte[] lenBytes = in.readNBytes(4);
      if (lenBytes.length < 4) break; // EOF

      int length = toIntBigEndian(lenBytes);
      if (length < 0) throw new IOException("Invalid PNG chunk length: " + length);

      byte[] typeBytes = in.readNBytes(4);
      if (typeBytes.length < 4) throw new IOException("Unexpected EOF reading chunk type.");
      String type = new String(typeBytes, java.nio.charset.StandardCharsets.US_ASCII);

      byte[] data = in.readNBytes(length);
      if (data.length < length) throw new IOException("Unexpected EOF reading chunk data.");

      byte[] crc = in.readNBytes(4);
      if (crc.length < 4) throw new IOException("Unexpected EOF reading CRC.");

      switch (type) {
        case "tEXt" -> {
          parseTEXt(data).ifPresent(e -> texts.put(e.keyword, e.text));
        }
        case "zTXt" -> {
          parseZTXt(data).ifPresent(e -> texts.put(e.keyword, e.text));
        }
        case "iTXt" -> {
          parseITXt(data).ifPresent(e -> texts.put(e.keyword, e.text));
        }
        default -> {}
      }
    }

    return texts;
  }

  private Optional<TextEntry> parseTEXt(byte[] data) {
    int sep = indexOf(data, (byte) 0x00, 0);
    if (sep < 0) return Optional.empty();
    String keyword = new String(data, 0, sep, java.nio.charset.StandardCharsets.ISO_8859_1).trim();
    // PNG spec says Latin-1, but many put JSON (ASCII/UTF-8-like). ISO-8859-1 is safe superset
    // byte-wise.
    String text =
        new String(
            data, sep + 1, data.length - (sep + 1), java.nio.charset.StandardCharsets.ISO_8859_1);
    return Optional.of(new TextEntry(keyword, text));
  }

  private Optional<TextEntry> parseZTXt(byte[] data) {
    int sep = indexOf(data, (byte) 0x00, 0);
    if (sep < 0 || sep + 1 >= data.length) return Optional.empty();

    String keyword = new String(data, 0, sep, java.nio.charset.StandardCharsets.ISO_8859_1).trim();
    int pos = sep + 1;

    int compressionMethod = data[pos] & 0xFF;
    pos += 1;
    if (pos > data.length) return Optional.empty();
    if (compressionMethod != 0) {
      // Only method 0 (zlib/deflate) is defined
      return Optional.empty();
    }

    byte[] compressed = Arrays.copyOfRange(data, pos, data.length);
    try (InflaterInputStream inflater =
        new InflaterInputStream(new ByteArrayInputStream(compressed))) {
      byte[] decompressed = inflater.readAllBytes();
      String text = new String(decompressed, java.nio.charset.StandardCharsets.UTF_8);
      return Optional.of(new TextEntry(keyword, text));
    } catch (IOException e) {
      return Optional.empty();
    }
  }

  private Optional<TextEntry> parseITXt(byte[] data) {
    int pos = 0;

    // keyword (Latin-1) NUL
    int sepKeyword = indexOf(data, (byte) 0x00, pos);
    if (sepKeyword < 0) return Optional.empty();
    String keyword =
        new String(data, pos, sepKeyword - pos, java.nio.charset.StandardCharsets.ISO_8859_1)
            .trim();
    pos = sepKeyword + 1;
    if (pos + 2 > data.length) return Optional.empty();

    int compressionFlag = data[pos] & 0xFF;
    pos++;
    int compressionMethod = data[pos] & 0xFF;
    pos++;

    // language tag (ASCII) NUL
    int sepLang = indexOf(data, (byte) 0x00, pos);
    if (sepLang < 0) return Optional.empty();
    // String languageTag = new String(data, pos, sepLang - pos, StandardCharsets.US_ASCII);
    pos = sepLang + 1;

    // translated keyword (UTF-8) NUL
    int sepTrans = indexOf(data, (byte) 0x00, pos);
    if (sepTrans < 0) return Optional.empty();
    // String translatedKeyword = new String(data, pos, sepTrans - pos, StandardCharsets.UTF_8);
    pos = sepTrans + 1;

    if (pos > data.length) return Optional.empty();

    byte[] textBytes = Arrays.copyOfRange(data, pos, data.length);
    String text;
    if (compressionFlag == 1) {
      if (compressionMethod != 0) return Optional.empty();
      try (InflaterInputStream inflater =
          new InflaterInputStream(new ByteArrayInputStream(textBytes))) {
        byte[] decompressed = inflater.readAllBytes();
        text = new String(decompressed, java.nio.charset.StandardCharsets.UTF_8);
      } catch (IOException e) {
        return Optional.empty();
      }
    } else {
      text = new String(textBytes, java.nio.charset.StandardCharsets.UTF_8);
    }

    return Optional.of(new TextEntry(keyword, text));
  }

  private static int indexOf(byte[] data, byte target, int start) {
    for (int i = start; i < data.length; i++) {
      if (data[i] == target) return i;
    }
    return -1;
  }

  private static int toIntBigEndian(byte[] four) {
    ByteBuffer bb = ByteBuffer.wrap(four);
    bb.order(ByteOrder.BIG_ENDIAN);
    long val = Integer.toUnsignedLong(bb.getInt());
    if (val > Integer.MAX_VALUE) {
      // Chunk length is 31-bit max per PNG spec; still guard.
      throw new UncheckedIOException(new IOException("Chunk length too large: " + val));
    }
    return (int) val;
  }

  private static record TextEntry(String keyword, String text) {}

  @JsonIgnoreProperties(ignoreUnknown = true)
  public static record CharacterCardV2(Data data, String spec, String spec_version) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static record Data(
        String name,
        String description,
        String personality,
        String scenario,
        String first_mes,
        String mes_example,
        String system_prompt,
        String creator_notes,
        List<String> tags,
        String post_history_instructions,
        String character_version,
        Map<String, Object> extensions) {}
  }
}
