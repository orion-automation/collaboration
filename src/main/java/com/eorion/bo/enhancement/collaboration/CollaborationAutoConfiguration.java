package com.eorion.bo.enhancement.collaboration;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@AutoConfiguration
@ComponentScan
public class CollaborationAutoConfiguration {
    @Bean
    public GroupedOpenApi collaborationGroup() {
        return GroupedOpenApi.builder()
                .group("collaboration")
                .packagesToScan("com.eorion.bo.enhancement.collaboration.adapter.inbound")
                .pathsToMatch("/enhancement/collaboration/**", "/enhancement/form/**", "/pb/enhancement/collaboration/resource/**")
                .build();
    }
}
