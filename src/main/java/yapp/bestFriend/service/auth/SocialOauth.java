package yapp.bestFriend.service.auth;

import yapp.bestFriend.model.enumClass.SocialLoginType;

public interface SocialOauth {

    /**
     * 각 Soical Login 페이지로 Redirect 처리할 URL Build
     * 사용자로부터 로그인 요청을 받아 Social Login Server 인증용 code 요청
     */
    String getOauthRedirectURL();

    default SocialLoginType type(){
        if(this instanceof KakaoOauth){
            return SocialLoginType.KAKAO;
        }
        return null;
    }
}
