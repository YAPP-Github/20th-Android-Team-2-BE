package yapp.bestFriend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import yapp.bestFriend.model.entity.User;
import yapp.bestFriend.model.enumClass.SocialLoginType;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);

    @Query("SELECT user.email FROM User user WHERE user.email = :email")
    Optional<String> findFirstByEmail(String email);

    @Query(value = "SELECT A FROM User A WHERE A.id = :userId and A.deletedYn = false")
    Optional<User> findById(@Param(value = "userId") Long id);

    User findByEmailAndProviderAndProviderId(String email, SocialLoginType provider, String providerId);

    @Query("SELECT user FROM User user WHERE user.email = :email and user.password =:password")
    Optional<User> findByUser(@Param("email") String email, @Param("password") String password);
}
