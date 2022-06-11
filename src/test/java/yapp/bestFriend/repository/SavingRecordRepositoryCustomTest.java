package yapp.bestFriend.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.ActiveProfiles;
import yapp.bestFriend.config.JpaAuditingConfig;
import yapp.bestFriend.model.dto.res.SavingRecordDto;
import yapp.bestFriend.model.entity.Product;
import yapp.bestFriend.model.entity.SavingRecord;
import yapp.bestFriend.model.entity.User;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;

@DataJpaTest(includeFilters = @ComponentScan.Filter(
        type = FilterType.ASSIGNABLE_TYPE,
        classes = JpaAuditingConfig.class
))
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class SavingRecordRepositoryCustomTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    SavingRecordRepository savingRecordRepository;

    SavingRecordRepositoryCustom savingRecordRepositoryCustom;

    @Autowired EntityManager em;
    JPAQueryFactory queryFactory;

    @BeforeEach
    public void init() {
        queryFactory = new JPAQueryFactory(em);
        savingRecordRepositoryCustom = new SavingRecordRepositoryCustom(queryFactory);
    }

    @Test
    @DisplayName("유저ID, 날짜로 절약 기록 조회")
    public void findByUserId(){
        //given
        String nickName = "test1";
        User user = User.builder()
                .nickName(nickName)
                .email("test1@naver.com")
                .password("123456")
                .build();
        userRepository.save(user);

        //given 2 - product 정보
        Product prodInput = Product.builder().user(user)
                .name("빙수")
                .price("8000")
                .resolution("더워도 참아")
                .build();
        productRepository.save(prodInput);

        //given 3 - 세이빙레코드 정보
        SavingRecord savingRecord = SavingRecord.builder()
                .recordYmd(LocalDate.of(2022,06,01))
                .product(prodInput)
                .user(user)
                .build();
        savingRecordRepository.save(savingRecord);

        //when
        List<SavingRecordDto> result = savingRecordRepositoryCustom.findByUserId(user, "202206");

        //then
        assertThat(result).extracting("productId","name","price","resolution").contains(
                tuple(prodInput.getId(), prodInput.getName(),prodInput.getPrice(),prodInput.getResolution()
        ));
    }
}