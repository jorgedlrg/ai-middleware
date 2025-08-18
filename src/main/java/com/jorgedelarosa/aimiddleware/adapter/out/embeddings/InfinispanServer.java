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
public class InfinispanServer {

  private final HotRodServer hotRodServer;

  public InfinispanServer() {
    GlobalConfigurationBuilder global = GlobalConfigurationBuilder.defaultClusteredBuilder();
    EmbeddedCacheManager cacheManager = new DefaultCacheManager(global.build());
    cacheManager.defineConfiguration(
        "embeddings",
        new org.infinispan.configuration.cache.ConfigurationBuilder()
            .memory()
            .encoding()
            .mediaType("application/x-protostream")
            .indexing()
            .addIndexedEntity("dev.langchain4j.LangChainItem384")
            .storage(IndexStorage.LOCAL_HEAP)
            .enable()
            .build());

    HotRodServerConfigurationBuilder hotRodBuilder = new HotRodServerConfigurationBuilder();
    hotRodBuilder.host("127.0.0.1").port(11222);
    hotRodServer = new HotRodServer();
    hotRodServer.start(hotRodBuilder.build(), cacheManager);
  }
}
