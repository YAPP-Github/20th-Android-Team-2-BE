package yapp.bestFriend.service.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import yapp.bestFriend.model.entity.Role;
import yapp.bestFriend.model.entity.User;
import yapp.bestFriend.model.entity.UserConnection;
import yapp.bestFriend.model.enumClass.SocialLoginType;
import yapp.bestFriend.repository.UserConnectionRepository;
import yapp.bestFriend.repository.UserFcmTokenRepository;
import yapp.bestFriend.repository.UserRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class UserServiceTest {

    @Mock
    UserConnectionRepository userConnectionRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    UserFcmTokenRepository userFcmTokenRepository;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("사용자 정상 로그아웃")
    void logout() {
        User user = User.builder()
                .email("test@naver.com")
                .password("123456")
                .nickName("best friend")
                .role(Role.USER)
                .userConnection(null)
                .build();

        UserConnection userConnectionInfo = UserConnection.builder()
                .email("test@naver.com")
                .provider(SocialLoginType.KAKAO)
                .providerId("123456789")
                .nickName("best friend")
                .accessToken("accessToken")
                .build();

        //when
        when(userRepository.findById(any())).thenReturn(Optional.ofNullable(user));
        when(userRepository.save(any())).thenReturn(null);
        when(userConnectionRepository.getById(any())).thenReturn(userConnectionInfo);
        when(userConnectionRepository.save(any())).thenReturn(1);
        when(userFcmTokenRepository.findByUserId(any())).thenReturn(Optional.empty());

        //then
        assertThat(userService.logout(1L))
                .hasFieldOrPropertyWithValue("statusCode", HttpStatus.OK.value())
                .hasFieldOrPropertyWithValue("message", "로그아웃 성공");

    }

    @Test
    @DisplayName("사용자 정보가 없는 경우 로그아웃")
    void logout_no_user_info() {
        //when
        when(userRepository.findById(any())).thenReturn(Optional.empty());

        //then
        assertThat(userService.logout(1L))
                .hasFieldOrPropertyWithValue("statusCode", HttpStatus.NOT_FOUND.value())
                .hasFieldOrPropertyWithValue("message", "사용자 정보가 존재하지 않습니다.");
    }
}