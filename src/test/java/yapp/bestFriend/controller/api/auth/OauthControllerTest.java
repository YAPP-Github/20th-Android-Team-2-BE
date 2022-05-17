package yapp.bestFriend.controller.api.auth;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import yapp.bestFriend.model.enumClass.SocialLoginType;
import yapp.bestFriend.service.auth.OauthService;
import yapp.bestFriend.service.user.UserDetailsService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = OauthController.class)
class OauthControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    OauthService OauthService;

    @MockBean
    UserDetailsService userDetailsService;

    @MockBean
    PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("소셜 로그인 요청 Test")
    public void social_login_controller_test() throws Exception {
        mvc.perform(get("/api/oauth/{socialLoginType}", SocialLoginType.KAKAO))
                .andDo(print())
                .andExpect(status().isOk());
    }

}