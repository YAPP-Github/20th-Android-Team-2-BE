package yapp.bestFriend.service.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import yapp.bestFriend.repository.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class SessionServiceTest {

    MockHttpServletRequest request;

    @Mock
    private UserRepository userRepository;

    @Test
    @DisplayName("토큰 없음")
    void no_token(){
        request = new MockHttpServletRequest();

        SessionService sessionService = new SessionService(userRepository);

        assertThat(sessionService.replaceToken(request))
                .hasFieldOrPropertyWithValue("statusCode",401)
                .hasFieldOrPropertyWithValue("Message","요청에러");
    }

    @Test
    @DisplayName("토큰불일치")
    void invalid_token(){
        request = new MockHttpServletRequest();
        request.addHeader("Authorization","Bearer ");

        SessionService sessionService = new SessionService(userRepository);

        assertThat(sessionService.replaceToken(request))
                .hasFieldOrPropertyWithValue("statusCode",401)
                .hasFieldOrPropertyWithValue("Message","토큰불일치");
    }
}