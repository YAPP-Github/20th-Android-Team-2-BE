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

    // SQL 일반 파라미터 쿼리, @Param 사용 O
    @Query(value =
            "WITH SUMMMARY AS (\n" +
            "    SELECT SUBSTR(A.RECORD_YMD,1,7) AS RECORD_MM,\n" +
            "           B.PRODUCT_NAME,\n" +
            "           SUM(DECODE(SUBSTR(A.RECORD_YMD,1,7),:toDate,1,0)) BASE_TIMES,\n" +
            "           SUM(DECODE(SUBSTR(A.RECORD_YMD,1,7),:fromDate,1,0)) PREV_TIMES\n" +
            "    From SAVING_RECORD A, PRODUCT B\n" +
            "    WHERE A.PRODUCT_ID = B.PRODUCT_ID\n" +
            "      AND A.USER_ID = B.USER_ID\n" +
            "      AND A.USER_ID = :userId\n" +
            "      AND A.RECORD_YMD BETWEEN PARSEDATETIME (:fromDate||'-01','yyyy-MM-dd') and PARSEDATETIME (:toDate||'-31','yyyy-MM-dd')\n" +
            "    GROUP BY SUBSTR(A.RECORD_YMD,1,7), B.PRODUCT_NAME\n" +
            ")\n" +
            "SELECT RECORD_MM as recordMm, PRODUCT_NAME as productName, BASE_TIMES as baseTimes, PREV_TIMES as prevTimes, BASE_TIMES-PREV_TIMES AS timesComparedToPrev\n" +
            "  FROM (\n" +
            "    SELECT RECORD_MM, PRODUCT_NAME, CAST(NVL(SUM(BASE_TIMES),0) AS INT) AS BASE_TIMES,\n" +
            "           CAST(NVL(SUM((SELECT PREV_TIMES FROM SUMMMARY WHERE RECORD_MM = :fromDate AND PRODUCT_NAME = A.PRODUCT_NAME)),0) AS INT) AS PREV_TIMES\n" +
            "      FROM SUMMMARY A\n" +
            "    WHERE RECORD_MM = :toDate\n" +
            "    GROUP BY PRODUCT_NAME\n" +
            ")",
            nativeQuery = true)
    List<SavingRecordSummaryInterface> selectSummary(@Param(value = "fromDate") String fromDate, @Param(value = "toDate") String toDate, @Param(value = "userId") Long userId);

    List<SavingRecord> findSavingRecordsByProductIdAndUserIdAndRecordYmdAndDeletedYn(LocalDate recordYmd, Long productId, Long userId, Boolean DeletedYn); // where name = ? and ranking = ?
}