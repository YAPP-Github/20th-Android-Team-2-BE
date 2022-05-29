package yapp.bestFriend.controller.api.auth;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import yapp.bestFriend.model.dto.DefaultRes;
import yapp.bestFriend.model.enumClass.SocialLoginType;
import yapp.bestFriend.model.utils.JwtUtil;
import yapp.bestFriend.service.auth.OauthService;
import yapp.bestFriend.service.user.UserDetailsService;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = OauthController.class, includeFilters = {
        // to include JwtUtil in spring context
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtUtil.class)})
class OauthControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    OauthService OauthService;

    @MockBean
    UserDetailsService userDetailsService;

    @MockBean
    PasswordEncoder passwordEncoder;

    @MockBean
    private JwtUtil jwtUtil;

    @Test
    @DisplayName("소셜 로그인 요청 Test")
    public void social_login_controller_test() throws Exception {
        DefaultRes<String> result = new DefaultRes<>(HttpStatus.OK.value(),
                "리다이렉트주소",
                "testRedirectUrl");

        when(OauthService.request(SocialLoginType.KAKAO)).thenReturn(result);

        mvc.perform(get("/api/oauth/{socialLoginType}", SocialLoginType.KAKAO))
                .andDo(print())
                .andExpect(status().isOk());
    }

}