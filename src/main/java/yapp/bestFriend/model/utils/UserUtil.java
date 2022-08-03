package yapp.bestFriend.model.utils;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import yapp.bestFriend.model.entity.User;
import yapp.bestFriend.service.v1.user.UserDetails;

public class UserUtil {

    public static long getId() {
        return getEntity().getId();
    }

    public static User getEntity() {
        return getEntity(true);
    }

    public static User getEntity(boolean shouldHidePassword) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        User User = getDetails().getUser();
        if (shouldHidePassword) {
            User.setPassword(null);
        }
        return User;
    }

    public static UserDetails getDetails() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return (UserDetails) securityContext.getAuthentication().getPrincipal();
    }

}
