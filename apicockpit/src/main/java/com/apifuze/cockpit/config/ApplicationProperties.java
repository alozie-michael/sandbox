package com.apifuze.cockpit.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties specific to Apicockpit.
 * <p>
 * Properties are configured in the application.yml file.
 * See {@link io.github.jhipster.config.JHipsterProperties} for a good example.
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {

    private final Recaptcha recaptcha=new Recaptcha();

    public Recaptcha getRecaptcha() {
        return recaptcha;
    }

    public static class Recaptcha {

        private  String url;
        private  String  key;
        private  String  secret;
        private  boolean enabled=true;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getSecret() {
            return secret;
        }

        public void setSecret(String secret) {
            this.secret = secret;
        }

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }


    }


}
