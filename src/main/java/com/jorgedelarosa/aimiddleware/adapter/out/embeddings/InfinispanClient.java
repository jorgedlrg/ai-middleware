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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.infinispan.client.hotrod.configuration.ConfigurationBuilder;
import org.infinispan.configuration.cache.IndexStorage;
import org.infinispan.configuration.global.GlobalConfigurationBuilder;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.manager.EmbeddedCacheManager;
import org.infinispan.server.hotrod.HotRodServer;
import org.infinispan.server.hotrod.configuration.HotRodServerConfigurationBuilder;

/**
 * @author jorge
 */
@Slf4j
@RequiredArgsConstructor
public class InfinispanClient {

  private static final InfinispanServer SERVER;

  static {
    SERVER = new InfinispanServer();
  }

  public static void mvp() {

    ConfigurationBuilder builder = new ConfigurationBuilder();
    builder.addServers("localhost:11222");
    EmbeddingStore<TextSegment> embeddingStore =
        InfinispanEmbeddingStore.builder()
            .cacheName("embeddings")
            .infinispanConfigBuilder(builder)
            .registerSchema(true)
            .dimension(384)
            .build();

    EmbeddingModel embeddingModel = new AllMiniLmL6V2EmbeddingModel();

    TextSegment segment1 = TextSegment.from("I like football.");
    Embedding embedding1 = embeddingModel.embed(segment1).content();
    embeddingStore.add(embedding1, segment1);

    TextSegment segment2 = TextSegment.from("The weather is good today.");
    Embedding embedding2 = embeddingModel.embed(segment2).content();
    embeddingStore.add(embedding2, segment2);

    Embedding queryEmbedding = embeddingModel.embed("What is your favourite sport?").content();
    EmbeddingSearchRequest embeddingSearchRequest =
        EmbeddingSearchRequest.builder().queryEmbedding(queryEmbedding).maxResults(1).build();
    List<EmbeddingMatch<TextSegment>> matches =
        embeddingStore.search(embeddingSearchRequest).matches();
    EmbeddingMatch<TextSegment> embeddingMatch = matches.get(0);

    log.info(embeddingMatch.score().toString()); // 0.814428....
    log.info(embeddingMatch.embedded().text()); // I like football.
  }
}
