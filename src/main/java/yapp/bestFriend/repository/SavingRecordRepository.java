package yapp.bestFriend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import yapp.bestFriend.model.dto.res.SavingRecordSummaryInterface;
import yapp.bestFriend.model.entity.SavingRecord;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface SavingRecordRepository extends JpaRepository<SavingRecord, Long> {

    // JPQL 파라미터 쿼리, @Param 사용 O
    // 22.06.10 수정 - 운영 DB, 테스트 DB 이원화로 인한 네이티브 쿼리 제거
    @Query(value =
            "select substring(A.recordYmd,1,7) AS recordMm,\n" +
                    "       A.product.name AS productName\n" +
                    "       ,SUM(case substring(A.recordYmd,1,7)\n" +
                    "             when :toDate \n" +
                    "             then 1 \n" +
                    "             else 0 \n" +
                    "         end) AS baseTimes\n" +
                    "       ,SUM(case substring(A.recordYmd,1,7)\n" +
                    "             when :fromDate \n" +
                    "             then 1 \n" +
                    "             else 0 \n" +
                    "         end) AS prevTimes\n" +
                    " from SavingRecord A inner join A.product B\n" +
                    "WHERE A.user.id = :userId\n" +
                    "  AND A.recordYmd BETWEEN :from AND :to\n" +
                    "  AND A.deletedYn = false\n" + //product는 삭제가 되어도 보여야 하므로 false 조건을 걸지 않아야 한다.
                    "GROUP BY substring(A.recordYmd,1,7), A.product.name"
            )
    List<SavingRecordSummaryInterface> selectSummary(@Param(value = "fromDate") String fromDate,
                                                     @Param(value = "toDate") String toDate,
                                                     @Param(value = "from") LocalDate from,
                                                     @Param(value = "to") LocalDate to,
                                                     @Param(value = "userId") Long userId);

    List<SavingRecord> findSavingRecordsByRecordYmdAndProductIdAndUserIdAndDeletedYn(LocalDate recordYmd, Long productId, Long userId, Boolean DeletedYn); // where name = ? and ranking = ?

    List<SavingRecord> findByUserIdAndDeletedYn(long userId, boolean deletedYn);
}