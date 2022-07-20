package yapp.bestFriend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import yapp.bestFriend.model.entity.UserConnection;
import yapp.bestFriend.model.enumClass.SocialLoginType;

public interface UserConnectionRepository extends JpaRepository<UserConnection, Long> {

    UserConnection findByEmail(String email);

    Integer deleteByEmail(String email);

    UserConnection findByEmailAndProviderAndProviderId(String email, SocialLoginType provider, String providerId);
}
