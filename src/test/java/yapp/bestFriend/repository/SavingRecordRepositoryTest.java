package yapp.bestFriend.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.ActiveProfiles;
import yapp.bestFriend.config.JpaAuditingConfig;
import yapp.bestFriend.config.LoginUserAuditorAware;
import yapp.bestFriend.model.entity.Product;
import yapp.bestFriend.model.entity.SavingRecord;
import yapp.bestFriend.model.entity.User;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest(includeFilters = @ComponentScan.Filter(
        type = FilterType.ASSIGNABLE_TYPE, //클래스를 기준으로 객체를 가져온다. classes에 할당할 수 있는 클래스, 즉 상속이나 구현한 클래스까지 포함한다.
        classes = {JpaAuditingConfig.class, LoginUserAuditorAware.class}
))
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class SavingRecordRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SavingRecordRepository savingRecordRepository;

    @Test
    @DisplayName("절약 정보 저장하기")
    //@Rollback(false)
    void save(){
        //given
        User user = User.builder()
                .nickName("kimyubi")
                .password("password")
                .build();

        User savedUser = userRepository.save(user);

        Product product = Product.builder()
                .user(savedUser)
                .name("커피")
                .price("4500원")
                .build();

        Product savedProduct = productRepository.save(product);

        SavingRecord savingRecord = SavingRecord.builder()
                        .user(savedUser)
                        .product(savedProduct)
                        .build();

        //when
        SavingRecord savedSavingRecord =  savingRecordRepository.save(savingRecord);

        //then
        assertThat(product).usingRecursiveComparison()
                .ignoringFields("createdDate")
                .ignoringFields("modifiedDate")
                .isEqualTo(savedProduct);
    }

}