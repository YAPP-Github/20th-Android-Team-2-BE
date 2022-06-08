package yapp.bestFriend.service.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import yapp.bestFriend.model.dto.DefaultRes;
import yapp.bestFriend.model.dto.request.SocialLoginRequest;
import yapp.bestFriend.model.enumClass.SocialLoginType;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OauthService {
    private final List<SocialOauth> socialOauthList;
    private final KakaoOauth kakaoOauth;

    public DefaultRes requestLogin(SocialLoginType socialLoginType, SocialLoginRequest request) {
        if(socialLoginType == SocialLoginType.KAKAO){
            return kakaoOauth.requestAccessTokenUsingUserData(request);
        }

        return DefaultRes.response(HttpStatus.OK.value(), "등록실패 (소셜 로그인 타입 없음)");
    }

    private SocialOauth findSocialOauthByType(SocialLoginType socialLoginType) {
        return socialOauthList.stream()
                .filter(x -> x.type() == socialLoginType)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("알 수 없는 SocialLoginType 입니다."));
    }

//    public DefaultRes requestAccessToken(SocialLoginType socialLoginType, String code) {
//        SocialOauth socialOauth = this.findSocialOauthByType(socialLoginType);
//
//        if(socialLoginType == SocialLoginType.KAKAO){
//            String token = socialOauth.requestAccessToken(code);
//            return kakaoOauth.requestAccessTokenUsingURL(token);
//        }
//
//        return DefaultRes.response(HttpStatus.OK.value(), "등록실패");
//    }

    public DefaultRes request(SocialLoginType socialLoginType) {
        SocialOauth socialOauth = this.findSocialOauthByType(socialLoginType);
        String redirectURL = socialOauth.getOauthRedirectURL();

        return DefaultRes.response(HttpStatus.OK.value(), "리다이렉트주소", redirectURL);
    }
}
