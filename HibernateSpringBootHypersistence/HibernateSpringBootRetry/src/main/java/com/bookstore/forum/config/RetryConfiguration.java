package com.bookstore.forum.config;

import io.hypersistence.utils.spring.aop.RetryAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * Registers the Hypersistence Utils {@link RetryAspect} and turns on AspectJ
 * auto-proxying so that {@code @Retry}-annotated methods are advised. The aspect
 * lives in the library's own package, so it is contributed as an explicit bean
 * rather than picked up by component scanning.
 */
@Configuration
@EnableAspectJAutoProxy
public class RetryConfiguration {

    @Bean
    public RetryAspect retryAspect() {
        return new RetryAspect();
    }
}
