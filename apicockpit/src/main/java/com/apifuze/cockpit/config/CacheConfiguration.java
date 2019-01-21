package com.apifuze.cockpit.config;

import java.time.Duration;

import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;

import io.github.jhipster.config.jcache.BeanClassLoaderAwareJCacheRegionFactory;
import io.github.jhipster.config.JHipsterProperties;

import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.*;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        BeanClassLoaderAwareJCacheRegionFactory.setBeanClassLoader(this.getClass().getClassLoader());
        JHipsterProperties.Cache.Ehcache ehcache =
            jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder.newCacheConfigurationBuilder(Object.class, Object.class,
                ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                .build());
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            cm.createCache(com.apifuze.cockpit.repository.UserRepository.USERS_BY_LOGIN_CACHE, jcacheConfiguration);
            cm.createCache(com.apifuze.cockpit.repository.UserRepository.USERS_BY_EMAIL_CACHE, jcacheConfiguration);
            cm.createCache(com.apifuze.cockpit.domain.User.class.getName(), jcacheConfiguration);
            cm.createCache(com.apifuze.cockpit.domain.Authority.class.getName(), jcacheConfiguration);
            cm.createCache(com.apifuze.cockpit.domain.User.class.getName() + ".authorities", jcacheConfiguration);
            cm.createCache(com.apifuze.cockpit.domain.PlatformUser.class.getName(), jcacheConfiguration);
            cm.createCache(com.apifuze.cockpit.domain.ApiPublisherProfile.class.getName(), jcacheConfiguration);
            cm.createCache(com.apifuze.cockpit.domain.ApiPublisherProfile.class.getName() + ".consumerProfiles", jcacheConfiguration);
            cm.createCache(com.apifuze.cockpit.domain.ApiPublisherProfile.class.getName() + ".serviceConfigs", jcacheConfiguration);
            cm.createCache(com.apifuze.cockpit.domain.ApiPublisherProfile.class.getName() + ".platformUsers", jcacheConfiguration);
            cm.createCache(com.apifuze.cockpit.domain.ApiConsumerProfile.class.getName(), jcacheConfiguration);
            cm.createCache(com.apifuze.cockpit.domain.ApiConsumerProfile.class.getName() + ".projects", jcacheConfiguration);
            cm.createCache(com.apifuze.cockpit.domain.ApiConsumerProfile.class.getName() + ".platformUsers", jcacheConfiguration);
            cm.createCache(com.apifuze.cockpit.domain.ApiProject.class.getName(), jcacheConfiguration);
            cm.createCache(com.apifuze.cockpit.domain.ApiServiceGroup.class.getName(), jcacheConfiguration);
            cm.createCache(com.apifuze.cockpit.domain.ApiServiceConfig.class.getName(), jcacheConfiguration);
            cm.createCache(com.apifuze.cockpit.domain.ApiServiceConfig.class.getName() + ".actions", jcacheConfiguration);
            cm.createCache(com.apifuze.cockpit.domain.ApiProjectService.class.getName(), jcacheConfiguration);
            cm.createCache(com.apifuze.cockpit.domain.ApiProjectAuthConfig.class.getName(), jcacheConfiguration);
            cm.createCache(com.apifuze.cockpit.domain.ApiSvcProcConfig.class.getName(), jcacheConfiguration);
            cm.createCache(com.apifuze.cockpit.domain.DbProcConfig.class.getName(), jcacheConfiguration);
            cm.createCache(com.apifuze.cockpit.domain.GrovyProcConfig.class.getName(), jcacheConfiguration);
            cm.createCache(com.apifuze.cockpit.domain.ApiServiceGroup.class.getName() + ".availableServices", jcacheConfiguration);
            cm.createCache(com.apifuze.cockpit.domain.ApiServiceConfig.class.getName() + ".apiProjectServices", jcacheConfiguration);
            cm.createCache(com.apifuze.cockpit.domain.ApiProjectService.class.getName() + ".serviceConfigs", jcacheConfiguration);
            cm.createCache(com.apifuze.cockpit.domain.ApiServiceConfig.class.getName() + ".serviceGroups", jcacheConfiguration);
            cm.createCache(com.apifuze.cockpit.domain.ApiProjectService.class.getName() + ".projects", jcacheConfiguration);
            cm.createCache(com.apifuze.cockpit.domain.ApiProject.class.getName() + ".", jcacheConfiguration);
            cm.createCache(com.apifuze.cockpit.domain.ApiProject.class.getName() + ".apiProjectServices", jcacheConfiguration);
            cm.createCache(com.apifuze.cockpit.domain.ApiProject.class.getName() + ".apis", jcacheConfiguration);
            cm.createCache(com.apifuze.cockpit.domain.ApiPublisherProfile.class.getName() + ".users", jcacheConfiguration);
            cm.createCache(com.apifuze.cockpit.domain.ApiCallLogs.class.getName(), jcacheConfiguration);
            // jhipster-needle-ehcache-add-entry
        };
    }
}
