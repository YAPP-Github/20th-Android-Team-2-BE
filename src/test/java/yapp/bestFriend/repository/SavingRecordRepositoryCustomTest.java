package yapp.bestFriend.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import liquibase.pro.packaged.D;
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
import java.text.DecimalFormat;
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

    private User user;
    private Product prodInput;

    @BeforeEach
    public void init() {
        queryFactory = new JPAQueryFactory(em);
        savingRecordRepositoryCustom = new SavingRecordRepositoryCustom(queryFactory);

        //given
        String nickName = "test1";
        user = User.builder()
                .nickName(nickName)
                .email("test1@naver.com")
                .password("123456")
                .build();
        userRepository.save(user);

        //given 2 - product 정보
        prodInput = Product.builder().user(user)
                .name("빙수")
                .price("8000")
                .build();
        productRepository.save(prodInput);

        //given 3 - 세이빙레코드 정보
        SavingRecord savingRecord = SavingRecord.builder()
                .product(prodInput)
                .user(user)
                .recordYmd(LocalDate.now())
                .build();
        savingRecordRepository.save(savingRecord);
    }

    @Test
    @DisplayName("유저ID, 날짜로 절약 기록 조회")
    public void findByUserId(){
        DecimalFormat df = new DecimalFormat("00");

        //when
        List<SavingRecordDto> result = savingRecordRepositoryCustom.findByUserId(user, "2022".concat(df.format(LocalDate.now().getMonthValue())));

        //then
        assertThat(result).extracting("productId","name","price").contains(
                tuple(prodInput.getId(), prodInput.getName(),prodInput.getPrice()
        ));
    }

    @Test
    @DisplayName("체크여부 조회 - 체크 정상")
    public void isChecked(){
        //when
        boolean result = savingRecordRepositoryCustom.isChecked(user, LocalDate.now(), prodInput);

        //then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("체크여부 조회 - 어제 체크 후, 미래 조회")
    public void isFutureChecked(){
        //when
        boolean result = savingRecordRepositoryCustom.isChecked(user, LocalDate.now().plusDays(1), prodInput);

        //then
        assertThat(result).isFalse();
    }
}