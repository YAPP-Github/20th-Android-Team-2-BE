package yapp.bestFriend.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import yapp.bestFriend.model.entity.Product;
import yapp.bestFriend.model.entity.User;

import java.time.LocalDate;
import java.util.Optional;

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
                        .and(savingRecord.record_ymd.eq(now)))
                .fetchFirst();

        return fetchOne != null;
    }
}
