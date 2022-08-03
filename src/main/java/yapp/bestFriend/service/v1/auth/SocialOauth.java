package yapp.bestFriend.service.v1.auth;

import yapp.bestFriend.model.enumClass.SocialLoginType;

public interface SocialOauth {

    /**
     * 각 Soical Login 페이지로 Redirect 처리할 URL Build
     * 사용자로부터 로그인 요청을 받아 Social Login Server 인증용 code 요청
     */
    String getOauthRedirectURL();

    /**
     * API Server로부터 받은 code를 활용하여 사용자 인증 정보 요청
     * @param code API Server 에서 받아온 code
     * @return API 서버로 부터 응답받은 Json 형태의 결과를 string으로 반
     */
    String requestAccessToken(String code);

    default SocialLoginType type(){
        if(this instanceof KakaoOauth){
            return SocialLoginType.KAKAO;
        }
        return null;
    }
}
