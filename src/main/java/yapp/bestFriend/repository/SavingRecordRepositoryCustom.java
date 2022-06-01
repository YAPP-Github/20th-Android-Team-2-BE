package yapp.bestFriend.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import yapp.bestFriend.model.dto.res.SavingRecordDto;
import yapp.bestFriend.model.entity.Product;
import yapp.bestFriend.model.entity.User;

import java.time.LocalDate;
import java.util.List;

import static yapp.bestFriend.model.entity.QSavingRecord.savingRecord;

@RequiredArgsConstructor
@Repository
public class SavingRecordRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public boolean isChecked(User existingUser, LocalDate now, Product product) {
        Integer fetchOne =  queryFactory.selectOne()
                .from(savingRecord)
                .where(savingRecord.user.eq(existingUser)
                        .and(savingRecord.product.eq(product))
                        .and(savingRecord.recordYmd.eq(now)))
                .fetchFirst();

        return fetchOne != null;
    }

    //유저ID, 날짜로 절약 기록 조회
    public List<SavingRecordDto> findByUserId(User existingUser, String now) {
        return queryFactory
                .select(Projections.constructor(SavingRecordDto.class,
                        savingRecord.recordYmd,
                        savingRecord.product.id,
                        savingRecord.product.name,
                        savingRecord.product.price,
                        savingRecord.product.resolution))
                .from(savingRecord)
                .where(savingRecord.user.id.eq(existingUser.getId()))
                .where(savingRecord.recordYmd.yearMonth().eq(Integer.valueOf(now)))
                .fetch();
    }
}
