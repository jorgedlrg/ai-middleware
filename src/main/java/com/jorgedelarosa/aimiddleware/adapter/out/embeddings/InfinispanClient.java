package com.jorgedelarosa.aimiddleware.adapter.out.embeddings;

import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.embedding.onnx.allminilml6v2.AllMiniLmL6V2EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingMatch;
import dev.langchain4j.store.embedding.EmbeddingSearchRequest;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.infinispan.InfinispanEmbeddingStore;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.infinispan.client.hotrod.configuration.ConfigurationBuilder;

/**
 * @author jorge
 */
@Slf4j
public class InfinispanClient {

  // TODO: attempt to access the in-process infinispan server directly, intead of using a socket.
  // Also, test this server as a @Component
  private static final InfinispanServer SERVER;

  static {
    SERVER = new InfinispanServer();
  }

  private final EmbeddingStore<TextSegment> embeddingStore;
  private final EmbeddingModel embeddingModel;

  public InfinispanClient() {
    ConfigurationBuilder builder = new ConfigurationBuilder();
    builder.addServers("localhost:11222");
    embeddingStore =
        InfinispanEmbeddingStore.builder()
            .cacheName("embeddings")
            .infinispanConfigBuilder(builder)
            .registerSchema(true)
            .dimension(384)
            .build();
    embeddingModel = new AllMiniLmL6V2EmbeddingModel();
  }

  public void addTextSegment(String text) {
    TextSegment textSegment = TextSegment.from(text);
    Embedding embedding = embeddingModel.embed(textSegment).content();
    embeddingStore.add(embedding, textSegment);
  }

  public List<String> query(String query) {
    Embedding queryEmbedding = embeddingModel.embed(query).content();
    EmbeddingSearchRequest embeddingSearchRequest =
        EmbeddingSearchRequest.builder()
            .queryEmbedding(queryEmbedding)
            .maxResults(10)
            .build();
    List<EmbeddingMatch<TextSegment>> matches =
        embeddingStore.search(embeddingSearchRequest).matches();
    // TODO: I need to rethink how I handle these caches... maybe I need to create one per session.
    embeddingStore.removeAll();
    log.debug(String.format("Number of matches: %s", matches.size()));
    return matches.stream().map(e -> e.embedded().text()).toList();
  }
}
