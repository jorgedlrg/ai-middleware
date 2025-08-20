package com.jorgedelarosa.aimiddleware.adapter.out.embeddings;

import org.infinispan.configuration.cache.IndexStorage;
import org.infinispan.configuration.global.GlobalConfigurationBuilder;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.manager.EmbeddedCacheManager;
import org.infinispan.server.hotrod.HotRodServer;
import org.infinispan.server.hotrod.configuration.HotRodServerConfigurationBuilder;

/**
 * @author jorge
 */
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
