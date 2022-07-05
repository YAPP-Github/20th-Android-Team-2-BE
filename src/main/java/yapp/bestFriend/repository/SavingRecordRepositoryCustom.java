package yapp.bestFriend.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import yapp.bestFriend.model.dto.res.SavingRecordDto;
import yapp.bestFriend.model.dto.res.SimpleProductResponse;
import yapp.bestFriend.model.entity.Product;
import yapp.bestFriend.model.entity.User;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
                        .and(savingRecord.recordYmd.eq(now))
                        .and(savingRecord.deletedYn.eq(false)))
                .fetchFirst();

        return fetchOne != null;
    }

    //유저ID, 날짜(nowYyyyMm)로 절약 기록 조회
    public List<SavingRecordDto> findByUserId(User existingUser, String nowYyyyMm) {
        return queryFactory
                .select(Projections.constructor(SavingRecordDto.class,
                        savingRecord.recordYmd,
                        savingRecord.product.id,
                        savingRecord.product.name,
                        savingRecord.product.price))
                .from(savingRecord)
                .where(savingRecord.user.id.eq(existingUser.getId()))
                .where(savingRecord.recordYmd.yearMonth().eq(Integer.valueOf(nowYyyyMm)))
                .fetch();
    }

    //유저ID, 날짜(nowYyyyMmDd)로 절약 기록 조회
    public List<SimpleProductResponse> findByUserIdByYmd(User existingUser, String nowYyyyMmDd) {
        DateTimeFormatter inputFormat = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate date = LocalDate.parse(nowYyyyMmDd, inputFormat);

        return queryFactory
                .select(Projections.constructor(SimpleProductResponse.class,
                        savingRecord.product.id,
                        savingRecord.product.name,
                        savingRecord.product.price,
                        Expressions.asBoolean(true).isTrue(),
                        savingRecord.recordYmd
                        ))
                .from(savingRecord)
                .where(savingRecord.user.id.eq(existingUser.getId()))
                .where(savingRecord.recordYmd.eq(date))
                .where(savingRecord.deletedYn.eq(false))//절약 상품은 삭제 여부 검사 하지 않음(추후 삭제가 되도 기록은 남아야 하기 때문)
                .fetch();
    }
}
