package edu.udemy.micronaut.config;

import io.micronaut.context.annotation.ConfigurationProperties;
import io.micronaut.serde.annotation.Serdeable;

@ConfigurationProperties("translations")
@Serdeable
public interface TranslationConfig {

    String getEs();
    String getEn();
    String getDe();
}
