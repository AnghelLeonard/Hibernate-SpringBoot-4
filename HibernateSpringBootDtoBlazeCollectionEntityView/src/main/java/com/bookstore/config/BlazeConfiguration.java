package com.bookstore.config;

import com.blazebit.persistence.Criteria;
import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.integration.view.spring.EnableEntityViews;
import com.blazebit.persistence.spi.CriteriaBuilderConfiguration;
import com.blazebit.persistence.spring.data.repository.config.EnableBlazeRepositories;
import com.blazebit.persistence.view.EntityViewManager;
import com.blazebit.persistence.view.spi.EntityViewConfiguration;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

@Configuration
@EnableBlazeRepositories("com.bookstore")
@EnableEntityViews("com.bookstore")
public class BlazeConfiguration {

    private final LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean;

    public BlazeConfiguration(LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean) {
        this.localContainerEntityManagerFactoryBean = localContainerEntityManagerFactoryBean;
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
    @Lazy(false)
    public CriteriaBuilderFactory createCriteriaBuilderFactory() {
        CriteriaBuilderConfiguration config = Criteria.getDefault();
        return config.createCriteriaBuilderFactory(localContainerEntityManagerFactoryBean.getObject());
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
    @Lazy(false)
    public EntityViewManager createEntityViewManager(CriteriaBuilderFactory cbf,
            EntityViewConfiguration entityViewConfiguration) {
        return entityViewConfiguration.createEntityViewManager(cbf);
    }
}
