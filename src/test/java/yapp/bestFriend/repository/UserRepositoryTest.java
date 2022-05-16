package yapp.bestFriend.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import yapp.bestFriend.config.JpaAuditingConfig;
import yapp.bestFriend.config.LoginUserAuditorAware;
import yapp.bestFriend.model.entity.User;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(includeFilters = @ComponentScan.Filter(
        type = FilterType.ASSIGNABLE_TYPE, //클래스를 기준으로 객체를 가져온다. classes에 할당할 수 있는 클래스, 즉 상속이나 구현한 클래스까지 포함한다.
        classes = {JpaAuditingConfig.class, LoginUserAuditorAware.class}
))
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)//embeddedDatabase를 할지 안할지
//@Rollback(false)
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("회원 저장하기")
    void save(){
        //given
        User user = User.builder()
                .nickName("IceChoco")
                .email("dkfk2685@naver.com")
                .password("123456")
                .build();

        //when
        User saveUser = userRepository.save(user);

        //then
        assertThat(user).usingRecursiveComparison()
                .ignoringFields("createdDate")
                .ignoringFields("modifiedDate")
                .isEqualTo(saveUser);
    }

    @Test
    @DisplayName("이메일로 회원 찾기")
    void findByEmail(){
        //given
        User user = User.builder()
                .nickName("IceChoco")
                .email("dkfk2685@naver.com")
                .password("123456")
                .build();

        User saveUser = userRepository.save(user);

        //when
        String result = userRepository.findByEmail("dkfk2685@naver.com").getEmail();

        //then
        assertThat(result).isEqualTo(user.getEmail());
    }
}