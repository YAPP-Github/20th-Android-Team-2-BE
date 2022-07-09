package yapp.bestFriend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import yapp.bestFriend.model.entity.PushNotiHistory;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PushNotiHistoryRepository extends JpaRepository<PushNotiHistory, Long> {
    List<PushNotiHistory> findByUserIdAndDeletedYnOrderByCreatedAtDesc(long userId, Boolean DeletedYn); // where name = ? and ranking = ?

    @Query(value =
            "select max(createdAt) as CreatedAt\n" +
                    " from PushNotiHistory A\n" +
                    "WHERE A.user.id = :userId\n" +
                    "  AND A.deletedYn = false\n"
    )
    LocalDateTime findMaxCreatedAtByUserId(@Param(value = "userId") long userId);
}
