package yapp.bestFriend.service.auth;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import yapp.bestFriend.model.dto.request.SocialLoginRequest;
import yapp.bestFriend.model.entity.Role;
import yapp.bestFriend.model.entity.User;
import yapp.bestFriend.model.entity.UserConnection;
import yapp.bestFriend.model.enumClass.SocialLoginType;
import yapp.bestFriend.model.utils.JwtUtil;
import yapp.bestFriend.repository.UserConnectionRepository;
import yapp.bestFriend.repository.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class KakaoOauthTest {

    @Mock
    UserConnectionRepository userConnectionRepository;

    @Mock
    UserRepository userRepository;

    @InjectMocks
    private KakaoOauth kakaoOauth;

    @Test
    @DisplayName("로그아웃한 사용자 로그인 시")
    void requestLogin() {
        //given
        SocialLoginRequest socialLoginRequest = new SocialLoginRequest();
        socialLoginRequest.builder().providerId(1L)
                .email("test@naver.com")
                .nickName("best friend");

        UserConnection userConnectionInfo = UserConnection.builder()
                .email("test@naver.com")
                .provider(SocialLoginType.KAKAO)
                .providerId(123456789L)
                .nickName("best friend")
                .accessToken("accessToken")
                .build();

        //when
        when(userConnectionRepository.findById(any())).thenReturn(null);
        when(userConnectionRepository.save(any())).thenReturn(userConnectionInfo);
        when(userRepository.findByEmail(any())).thenReturn(null);
        when(userRepository.save(any())).thenReturn(User.builder()
                .email("test@naver.com")
                .password("123456")
                .nickName("best friend")
                .role(Role.USER)
                .userConnection(userConnectionInfo)
                .build());

        try (MockedStatic<JwtUtil> mockedStatic = mockStatic(JwtUtil.class)) {
            mockedStatic.when(() -> JwtUtil.createAccessToken(any())).thenReturn("accessToken");
            mockedStatic.when(() -> JwtUtil.createRefreshToken(any())).thenReturn("refreshToken");

            //then
            assertThat(kakaoOauth.requestAccessTokenUsingUserData(socialLoginRequest))
                    .hasFieldOrPropertyWithValue("statusCode", HttpStatus.OK.value())
                    .hasFieldOrPropertyWithValue("message", "등록성공");
        }
    }
}