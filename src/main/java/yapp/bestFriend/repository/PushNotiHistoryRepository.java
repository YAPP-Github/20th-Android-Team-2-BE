package yapp.bestFriend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import yapp.bestFriend.model.entity.PushNotiHistory;

@Repository
public interface PushNotiHistoryRepository extends JpaRepository<PushNotiHistory, Long> {
}
