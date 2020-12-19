package ua.ies.g23.Covinfo19.pacientes_med_hosp.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;


import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

//Classe de configuração da base de dados responsável por armazenar Pacientes, Médicos e Hospitais

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "pacientes_med_hospEntityManagerFactory",
        transactionManagerRef = "pacientes_med_hospTransactionManager",
        basePackages = {"ua.ies.g23.Covinfo19.pacientes_med_hosp.repository"}
)
public class Pacientes_Med_HospConfig {

    @Bean(name = "pacientes_med_hospDataSource")
    @ConfigurationProperties(prefix = "db2.datasource")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "pacientes_med_hospEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean
    barEntityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("pacientes_med_hospDataSource") DataSource dataSource
    ) {
        return
                builder
                        .dataSource(dataSource)
                        .packages("ua.ies.g23.Covinfo19.pacientes_med_hosp.model")
                        .persistenceUnit("db2")
                        .build();
    }

    @Bean(name = "pacientes_med_hospTransactionManager")
    public PlatformTransactionManager pacientes_med_hospTransactionManager(
            @Qualifier("pacientes_med_hospEntityManagerFactory") EntityManagerFactory
                    pacientes_med_hospEntityManagerFactory
    ) {
        return new JpaTransactionManager(pacientes_med_hospEntityManagerFactory);
    }
}
