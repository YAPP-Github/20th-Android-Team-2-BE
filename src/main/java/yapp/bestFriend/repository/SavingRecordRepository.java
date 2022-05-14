package yapp.bestFriend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import yapp.bestFriend.model.entity.SavingRecord;

@Repository
public interface SavingRecordRepository extends JpaRepository<SavingRecord, Long> {
}
