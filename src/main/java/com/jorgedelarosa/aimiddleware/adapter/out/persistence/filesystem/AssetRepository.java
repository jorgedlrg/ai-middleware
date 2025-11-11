package com.jorgedelarosa.aimiddleware.adapter.out.persistence.filesystem;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author jorge
 */
@Component
@Slf4j
public class AssetRepository {
  // TODO refine this Asset thing. Could it be a value object?
  private static final String PATH_TO_ASSETS =
      System.getProperty("user.home") + "/aimiddleware/assets";

  public AssetRepository() {
    File directory = new File(PATH_TO_ASSETS);
    if (!directory.exists()) {
      directory.mkdirs();
    }
  }

  public void save(String directory, String filename, byte[] data) {
    File dir = new File(PATH_TO_ASSETS + "/" + directory);
    dir.mkdirs();
    String file = dir.getAbsolutePath() + "/" + filename;
    try {
      Files.write(new File(file).toPath(), data);
    } catch (IOException ex) {
      log.error(String.format("Error when saving %s", file));
    }
  }

  public void delete(String filename) {
    String file = PATH_TO_ASSETS + "/" + filename;
    try {
      Files.deleteIfExists(new File(file).toPath());
    } catch (IOException ex) {
      log.warn(String.format("Error when deleting %s", file));
    }
  }

  public byte[] load(String filename) {
    String file = PATH_TO_ASSETS + "/" + filename;
    byte[] bytes = new byte[0];
    try {
      bytes = Files.readAllBytes(new File(file).toPath());
    } catch (IOException ex) {
      log.debug(String.format("Error when reading %s", file));
    }
    return bytes;
  }

  public List<byte[]> loadAssets(String directory) {
    List<byte[]> assets = new ArrayList<>();
    File dir = Paths.get(PATH_TO_ASSETS + "/" + directory).toFile();
    if (dir.isDirectory()) {
      File[] files = dir.listFiles();
      for (File file : files) {
        try {
          byte[] bytes = Files.readAllBytes(file.toPath());
          assets.add(bytes);
        } catch (IOException ex) {
          log.debug(String.format("Error when reading %s", file.getAbsolutePath()));
        }
      }

    } else {
      log.debug(String.format("%s is not a directory", dir.getAbsolutePath()));
    }

    return assets;
  }
}
