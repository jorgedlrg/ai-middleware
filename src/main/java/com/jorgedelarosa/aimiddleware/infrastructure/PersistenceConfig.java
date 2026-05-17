package com.jorgedelarosa.aimiddleware.infrastructure;

import java.util.Objects;
import javax.sql.DataSource;
import liquibase.integration.spring.SpringLiquibase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.autoconfigure.DataSourceProperties;
import org.springframework.boot.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author jorge
 */
@Configuration
@EnableJpaAuditing
@EnableTransactionManagement
@EnableJpaRepositories(
    basePackages = "com.jorgedelarosa.aimiddleware.adapter.out.persistence",
    entityManagerFactoryRef = "defaultEntityManagerFactory",
    transactionManagerRef = "defaultTransactionManager")
public class PersistenceConfig {

  @Bean
  public LocalContainerEntityManagerFactoryBean defaultEntityManagerFactory(
      @Qualifier("defaultDataSource") DataSource dataSource, EntityManagerFactoryBuilder builder) {
    LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
    em.setDataSource(dataSource);
    em.setPackagesToScan("com.jorgedelarosa.aimiddleware.adapter.out.persistence");

    JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
    em.setJpaVendorAdapter(vendorAdapter);

    return em;
  }

  @Bean
  public PlatformTransactionManager defaultTransactionManager(
      @Qualifier("defaultEntityManagerFactory")
          LocalContainerEntityManagerFactoryBean providerEntityManagerFactory) {
    return new JpaTransactionManager(
        Objects.requireNonNull(providerEntityManagerFactory.getObject()));
  }

  @Bean
  @ConfigurationProperties("spring.datasource")
  public DataSourceProperties providerDataSourceProperties() {
    return new DataSourceProperties();
  }

  @Bean(name = "defaultDataSource")
  public DataSource defaultDataSource() {
    return providerDataSourceProperties().initializeDataSourceBuilder().build();
  }

  @Bean
  public SpringLiquibase defaultLiquibase(@Qualifier("defaultDataSource") DataSource dataSource) {
    SpringLiquibase liquibase = new SpringLiquibase();
    liquibase.setDataSource(dataSource);
    liquibase.setChangeLog("classpath:db/changelog/changelog.h2.sql");
    return liquibase;
  }
}
