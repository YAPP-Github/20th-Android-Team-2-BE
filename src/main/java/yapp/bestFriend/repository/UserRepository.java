package yapp.bestFriend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;
import yapp.bestFriend.model.entity.User;

import java.util.Optional;

@Component
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT user.email FROM User user WHERE user.email = :email")
    Optional<String> findByEmail(@Param("email") String email);

}
