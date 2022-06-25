package yapp.bestFriend.controller.api.token;

import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import yapp.bestFriend.model.dto.DefaultRes;
import yapp.bestFriend.model.dto.res.ReplaceTokenResponseDto;
import yapp.bestFriend.model.utils.JwtUtil;
import yapp.bestFriend.service.user.SessionService;
import yapp.bestFriend.service.user.UserDetailsService;
import yapp.bestFriend.service.user.UserFcmTokenService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ReplaceTokenController.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc(addFilters = false)
class ReplaceTokenControllerTest {

    MockHttpServletRequest request;

    @Autowired
    MockMvc mvc;

    @MockBean
    SessionService sessionService;

    @MockBean
    UserDetailsService userDetailsService;

    @MockBean
    UserFcmTokenService userFcmTokenService;

    @MockBean
    PasswordEncoder passwordEncoder;

    @MockBean
    private JwtUtil jwtUtil;

    @Test
    @DisplayName("액세스 토큰 재발급 Test")
    public void recreate_access_token() throws Exception {
        //given
        String accessToken = "thisIsTestToken";
        request = new MockHttpServletRequest();

        DefaultRes result = new DefaultRes<>(HttpStatus.OK.value(),
                "토큰재발급",
                new ReplaceTokenResponseDto(accessToken));

        //when
        when(sessionService.replaceToken(any(MockHttpServletRequest.class))).thenReturn(result);

        //then
        mvc.perform(get("/api/token", request))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode", CoreMatchers.is(200)))
                .andExpect(jsonPath("$.data.accessToken", CoreMatchers.notNullValue()))
                .andExpect(jsonPath("$.message", CoreMatchers.is("토큰재발급")));
    }

    @Test
    @DisplayName("토큰 없이 액세스 토큰 발급 요청")
    public void request_error() throws Exception {
        //given
        String accessToken = "thisIsTestToken";
        request = new MockHttpServletRequest();

        DefaultRes result = DefaultRes.builder().statusCode(HttpStatus.UNAUTHORIZED.value())
                .Message("요청에러").build();

        //when
        when(sessionService.replaceToken(any(MockHttpServletRequest.class))).thenReturn(result);

        //then
        mvc.perform(get("/api/token", request))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.statusCode", CoreMatchers.is(HttpStatus.UNAUTHORIZED.value())))
                .andExpect(jsonPath("$.message", CoreMatchers.is("요청에러")));
    }

}