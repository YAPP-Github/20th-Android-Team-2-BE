package yapp.bestFriend.config;

import lombok.NonNull;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import yapp.bestFriend.model.entity.User;

import java.util.Optional;

@Component
public class LoginUserAuditorAware implements AuditorAware<Long> {

    private static final String ANOYMOUS = "anonymousUser";

    @Override
    @NonNull
    public Optional<Long> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication == null || !authentication.isAuthenticated()
                || authentication.getPrincipal().equals(ANOYMOUS)) {
            return Optional.empty();
        }

        return Optional.of(((User)authentication.getPrincipal()).getId());
    }
}
