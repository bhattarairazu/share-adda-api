package com.shareadda.api.ShareAdda.audititing;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class DatabaseAuditing implements AuditorAware<String> {
    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String name = null;
        if(authentication!=null){
            name = authentication.getName();

        }
        return Optional.of(name);
        //return Optional.of("razu");
    }
}
