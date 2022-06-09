package yapp.bestFriend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import yapp.bestFriend.model.entity.UserConnection;

public interface UserConnectionRepository extends JpaRepository<UserConnection, Long> {

    UserConnection findByEmail(String email);

}
