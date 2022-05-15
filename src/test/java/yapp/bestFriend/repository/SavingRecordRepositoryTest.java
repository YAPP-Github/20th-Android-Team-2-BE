package yapp.bestFriend.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import yapp.bestFriend.model.entity.Product;
import yapp.bestFriend.model.entity.SavingRecord;
import yapp.bestFriend.model.entity.User;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class SavingRecordRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SavingRecordRepository savingRecordRepository;

    @Test
    @DisplayName("절약 정보 저장하기")
    @Rollback(false)
    void save(){
        //given
        User user = User.builder()
                .nickName("kimyubi")
                .email("kimyubi@gmail.com")
                .password("password")
                .build();

        User savedUser = userRepository.save(user);

        Product product = Product.builder()
                .user(savedUser)
                .name("커피")
                .price("4500원")
                .resolution("절약과 친해지자")
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