package yapp.bestFriend.service.auth;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import yapp.bestFriend.model.dto.DefaultRes;
import yapp.bestFriend.model.enumClass.SocialLoginType;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class OauthServiceTest {

    @Autowired
    private OauthService oauthService;

    @Test
    @DisplayName("리다이렉션 URI 정상 반환 확인")
    void request(){
        DefaultRes defaultRes = oauthService.request(SocialLoginType.KAKAO);
        assertThat(defaultRes).toString().contains("kauth.kakao.com/oauth");
    }
}