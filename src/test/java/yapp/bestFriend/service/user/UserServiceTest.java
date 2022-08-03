package yapp.bestFriend.service.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import yapp.bestFriend.model.dto.request.user.UserSignUpRequestDto;
import yapp.bestFriend.model.entity.Role;
import yapp.bestFriend.model.entity.User;
import yapp.bestFriend.model.entity.UserConnection;
import yapp.bestFriend.model.enumClass.SocialLoginType;
import yapp.bestFriend.model.utils.JwtUtil;
import yapp.bestFriend.repository.UserConnectionRepository;
import yapp.bestFriend.repository.UserFcmTokenRepository;
import yapp.bestFriend.repository.UserRepository;
import yapp.bestFriend.service.v1.user.UserService;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;
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

    @Test
    @DisplayName("이미 있는 이메일인경우 회원가입 불가")
    void login_email_duplicate() {
        //when
        when(userRepository.findFirstByEmail(any())).thenReturn(Optional.of("test@naver.com"));

        UserSignUpRequestDto userSignUpRequestDto =
                new UserSignUpRequestDto("test@naver.com","MTIzNDU2");

        //then
        assertThat(userService.signUp(userSignUpRequestDto))
                .hasFieldOrPropertyWithValue("statusCode", HttpStatus.OK.value())
                .hasFieldOrPropertyWithValue("message", "이메일중복");
    }

    @Test
    @DisplayName("자체회원가입 정상")
    void login_success_normally() {
        //when
        when(userRepository.findFirstByEmail(any())).thenReturn(Optional.empty());

        UserSignUpRequestDto userSignUpRequestDto =
                new UserSignUpRequestDto("test@naver.com","MTIzNDU2");

        //then
        assertThat(userService.signUp(userSignUpRequestDto))
                .hasFieldOrPropertyWithValue("statusCode", HttpStatus.OK.value())
                .hasFieldOrPropertyWithValue("message", "성공");
    }

    @Test
    @DisplayName("자체 로그인 정상")
    void signIn_normally() {
        //given
        User user = User.builder()
                .email("test@naver.com")
                .password("123456")
                .nickName("best friend")
                .role(Role.USER)
                .userConnection(null)
                .build();

        //when
        when(userRepository.findFirstByEmail(any())).thenReturn(Optional.of("test@naver.com"));
        when(userRepository.findByUser(any(), any())).thenReturn(Optional.of(user));

        UserSignUpRequestDto userSignUpRequestDto =
                new UserSignUpRequestDto("test@naver.com","MTIzNDU2");

        try (MockedStatic<JwtUtil> mockedStatic = mockStatic(JwtUtil.class)) {
            mockedStatic.when(() -> JwtUtil.createAccessToken(any())).thenReturn("accessToken");
            mockedStatic.when(() -> JwtUtil.createRefreshToken(any())).thenReturn("refreshToken");

            //then
            assertThat(userService.signIn(userSignUpRequestDto))
                    .hasFieldOrPropertyWithValue("statusCode", HttpStatus.OK.value())
                    .hasFieldOrPropertyWithValue("message", "등록성공");
        }
    }

    @Test
    @DisplayName("자체 로그인 비정상 - 비밀번호 불일치")
    void signIn_wrong_password() {
        //when
        when(userRepository.findFirstByEmail(any())).thenReturn(Optional.of("test@naver.com"));
        when(userRepository.findByUser(any(), any())).thenReturn(Optional.empty());

        UserSignUpRequestDto userSignUpRequestDto =
                new UserSignUpRequestDto("test@naver.com","MTIzNDU2");

        //then
        assertThat(userService.signIn(userSignUpRequestDto))
                .hasFieldOrPropertyWithValue("statusCode", HttpStatus.OK.value())
                .hasFieldOrPropertyWithValue("message", "비밀번호불일치");
    }

    @Test
    @DisplayName("자체 로그인 비정상 - 아이디 불일치")
    void signIn_wrong_Id() {
        //when
        when(userRepository.findFirstByEmail(any())).thenReturn(Optional.empty());

        UserSignUpRequestDto userSignUpRequestDto =
                new UserSignUpRequestDto("test@naver.com","MTIzNDU2");

        //then
        assertThat(userService.signIn(userSignUpRequestDto))
                .hasFieldOrPropertyWithValue("statusCode", HttpStatus.OK.value())
                .hasFieldOrPropertyWithValue("message", "아이디불일치");
    }
}