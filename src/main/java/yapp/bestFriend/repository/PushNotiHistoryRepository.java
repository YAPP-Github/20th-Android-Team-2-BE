package yapp.bestFriend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import yapp.bestFriend.model.entity.PushNotiHistory;

import java.util.List;

@Repository
public interface PushNotiHistoryRepository extends JpaRepository<PushNotiHistory, Long> {
    List<PushNotiHistory> findByUserIdAndDeletedYn(long userId, Boolean DeletedYn); // where name = ? and ranking = ?
}
