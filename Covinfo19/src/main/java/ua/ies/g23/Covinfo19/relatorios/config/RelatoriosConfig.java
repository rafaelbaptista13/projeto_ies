package ua.ies.g23.Covinfo19.relatorios.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
 entityManagerFactoryRef = "relatoriosEntityManagerFactory",
 transactionManagerRef = "relatoriosTransactionManager",
 basePackages = {
  "ua.ies.g23.Covinfo19.relatorios.repository"
 }
)
public class RelatoriosConfig {
    @Primary
    @Bean(name = "relatoriosDataSource")
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource relatoriosDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Primary
    @Bean(name = "relatoriosEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean
    entityManagerFactory(
        EntityManagerFactoryBuilder builder,
        @Qualifier("relatoriosDataSource") DataSource dataSource
    ) {
    return builder
    .dataSource(dataSource)
    .packages("ua.ies.g23.Covinfo19.relatorios.model")
    .persistenceUnit("db1")
    .build();
    }

    @Primary
    @Bean(name = "relatoriosTransactionManager")
    public PlatformTransactionManager relatoriosTransactionManager(
    @Qualifier("relatoriosEntityManagerFactory") EntityManagerFactory relatoriosEntityManagerFactory
    ) {
    return new JpaTransactionManager(relatoriosEntityManagerFactory);
    }
}
