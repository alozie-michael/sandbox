package com.apifuze.cockpit.initializer.service;


import com.apifuze.cockpit.config.ApplicationProperties;
import com.apifuze.cockpit.config.AsyncConfiguration;
import com.apifuze.cockpit.service.dto.RecaptchaResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;


@Service
public class CaptchaService {

    private final RestTemplate restTemplate;

    private final ApplicationProperties applicationProperties;

    public CaptchaService(ApplicationProperties applicationProperties ,RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
        this.applicationProperties=applicationProperties;
    }


    private final Logger log = LoggerFactory.getLogger(AsyncConfiguration.class);

    public boolean verify(String response) {
        log.debug("Initiating captcha verification");
        if (applicationProperties.getRecaptcha().isEnabled()) {
            log.debug("Sending captcha verification");
            MultiValueMap param = new LinkedMultiValueMap<>();
            param.add("secret", applicationProperties.getRecaptcha().getSecret());
            param.add("response", response);
            RecaptchaResponse recaptchaResponse = null;
            try {
                recaptchaResponse = this.restTemplate.postForObject(applicationProperties.getRecaptcha().getUrl(), param, RecaptchaResponse.class);
            } catch (RestClientException e) {
                log.error(e.getMessage());
            }
            if (recaptchaResponse.isSuccess()) {
                return true;
            } else {
                return false;
            }
        }else{
            log.debug("captcha verification disabled");
        }
        return true;
    }
}
