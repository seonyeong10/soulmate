package com.soulmate.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

@Configuration
public class AuditorConfig implements AuditorAware {
    @Override
    public Optional getCurrentAuditor() {
        return Optional.empty();
    }
}
