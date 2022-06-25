package yapp.bestFriend.service.auth;

import com.google.gson.JsonParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import yapp.bestFriend.model.dto.DefaultRes;
import yapp.bestFriend.model.dto.request.SocialLoginRequest;
import yapp.bestFriend.model.dto.res.user.UserSignInResponseDto;
import yapp.bestFriend.model.entity.Role;
import yapp.bestFriend.model.entity.User;
import yapp.bestFriend.model.entity.UserConnection;
import yapp.bestFriend.model.enumClass.SocialLoginType;
import yapp.bestFriend.model.utils.JwtUtil;
import yapp.bestFriend.repository.UserConnectionRepository;
import yapp.bestFriend.repository.UserRepository;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class KakaoOauth implements SocialOauth {

    @Value("${sns.kako.url}")
    private String KAKAO_SNS_BASE_URL;

    @Value("${sns.kakao.client.id}")
    private String KAKAO_SNS_CLIENT_ID;

    @Value("${sns.kako.callback.url}")
    private String KAKAO_SNS_CALLBACK_URL;

    @Value("${sns.kako.token.url}")
    private String KAKAO_SNS_TOKEN_BASE_URL;

    @Value("${sns.kako.client.secret}")
    private String KAKAO_SNS_CLIENT_SECRET;

    private final UserConnectionRepository userConnectionRepository;
    private final UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public String getOauthRedirectURL() {
        Map<String, Object> params = new HashMap<>();
        params.put("client_id", KAKAO_SNS_CLIENT_ID);
        params.put("redirect_uri", KAKAO_SNS_CALLBACK_URL);
        params.put("response_type", "code");

        String parameterString = params.entrySet().stream()
                .map(x -> x.getKey() + "=" + x.getValue())
                .collect(Collectors.joining("&"));

        return KAKAO_SNS_BASE_URL + "?" + parameterString;
    }

    @Override
    public String requestAccessToken(String code) {
        String access_Token = "";
        String reqURL = KAKAO_SNS_TOKEN_BASE_URL;
        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            //POST 요청을 위해 기본값이 false인 setDoOutput을 true로
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            //POST 요청에 필요로 요구하는 파라미터 스트림을 통해 전송
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            StringBuilder sb = new StringBuilder();
            sb.append("grant_type=authorization_code");
            sb.append("&client_id=" + KAKAO_SNS_CLIENT_ID);
            sb.append("&redirect_uri=" + KAKAO_SNS_CALLBACK_URL);
            sb.append("&client_secret=" + KAKAO_SNS_CLIENT_SECRET);
            sb.append("&code=" + code);
            bw.write(sb.toString());
            bw.flush();

            //결과 코드가 200이라면 성공
            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                //요청을 통해 얻은 JSON타입의 Response 메세지 읽어오기
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line = "";
                String result = "";
                while ((line = br.readLine()) != null) {
                    result += line;
                }

                //Gson 라이브러리에 포함된 클래스로 JSON파싱 객체 생성
                access_Token = JsonParser.parseString(result)
                        .getAsJsonObject()
                        .get("access_token")
                        .getAsString();

                br.close();
                bw.close();
            }
        } catch (IOException e) {
            throw new IllegalArgumentException("알 수 없는 카카오 로그인 Access Token 요청 URL 입니다 :: " + KAKAO_SNS_TOKEN_BASE_URL);
        }
        return access_Token;
    }

    /**
     * 사용자로부터 SNS 로그인 정보를 받아 로그인 및 액세스 토큰 발급 처리
     */
    @Transactional(rollbackFor = Exception.class)
    public DefaultRes requestAccessTokenUsingUserData(SocialLoginRequest request) {

        //사용자에게 받은 body 변수로 쪼개기
        String email = request.getEmail();
        String nickName = request.getNickName();
        Long id = request.getProviderId();

        UserConnection info = userConnectionRepository.findByEmail(email);

        if(info == null) {

            UserConnection userConnection =
                    UserConnection.builder()
                            .email(email)
                            .nickName(nickName)
                            .provider(SocialLoginType.KAKAO)
                            .providerId(id)
                            .build();

            userConnection = userConnectionRepository.save(userConnection);

            User user = getUserInfo(email, userConnection);

            //신규토큰 생성
            String accessToken = JwtUtil.createAccessToken(user.getId());
            String refreshToken = JwtUtil.createRefreshToken(user.getId());

            userConnection.setAccessToken(accessToken);
            userConnectionRepository.save(userConnection);

            user.setPassword(accessToken);
            user.setRefreshToken(refreshToken);
            user.setUserConnection(userConnection);
            userRepository.save(user);

            return DefaultRes.response(HttpStatus.OK.value(), "등록성공", new UserSignInResponseDto(accessToken, refreshToken, user.getId(), user.getNickName()));

        }else{
            User userInfo = userRepository.findByEmail(email);
            String accessToken = "", refreshToken= "";
            if(userInfo != null) {
                accessToken = JwtUtil.createAccessToken(userInfo.getId());//신규토큰 생성
                refreshToken = JwtUtil.createRefreshToken(userInfo.getId());//신규토큰 생성
                info.setAccessToken(accessToken);
                userConnectionRepository.save(info);

                userInfo.setPassword(accessToken);
                userInfo.setRefreshToken(refreshToken);
                userRepository.save(userInfo);
            }

            return DefaultRes.response(HttpStatus.OK.value(), "토큰수정완료", new UserSignInResponseDto(accessToken, refreshToken, userInfo.getId(), info.getNickName()));
        }
    }

    private User getUserInfo(String email, UserConnection userConnection) {
        User user;
        if(userRepository.findByEmail(email) == null) {
            user = User.builder()
                    .email(userConnection.getEmail())
                    .nickName(userConnection.getNickName())
                    .userConnection(userConnection)
                    .role(Role.USER)
                    .build();
        }else{
            user = userRepository.findByEmail(email);
        }

        userRepository.save(user);
        return user;
    }

//    public DefaultRes requestAccessTokenUsingURL(String access_token) {
//
//        log.info(">> 소셜 로그인 API 서버로부터 받은 access_token :: {}", access_token);
//
//        String reqURL = "https://kapi.kakao.com/v2/user/me";
//
//        try {
//            URL url = new URL(reqURL);
//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//
//            //요청에 필요한 Header에 포함될 내용
//            conn.setRequestProperty("Authorization", "Bearer " + access_token);
//            conn.setRequestProperty("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
//            int responseCode = conn.getResponseCode();
//            if (responseCode == 200) {
//                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//                String line = "";
//                String result = "";
//                while ((line = br.readLine()) != null) {
//                    result += line;
//                }
//
//                //ObjectMapper로 JSON파싱 객체와 매핑
//                ObjectMapper objectMapper = new ObjectMapper();
//                KakaoProfile kakaoProfile = objectMapper.readValue(result, KakaoProfile.class);
//
//                String email = kakaoProfile.getKakao_account().getEmail();
//                String nickName = kakaoProfile.getProperties().getNickname();
//                Long id = kakaoProfile.getId();
//
//                UserConnection info = userConnectionRepository.findByEmail(email);
//
//                if(info == null) {
//                    UserConnection userConnection =
//                            UserConnection.builder()
//                                    .email(email)
//                                    .accessToken(access_token)
//                                    .nickName(nickName)
//                                    .provider(SocialLoginType.KAKAO)
//                                    .providerId(id)
//                                    .build();
//
//                    userConnection = userConnectionRepository.save(userConnection);
//
//                    User user = User.builder()
//                            .email(userConnection.getEmail())
//                            .password(userConnection.getAccessToken())
//                            .nickName(userConnection.getNickName())
//                            .userConnection(userConnection)
//                            .role(Role.USER)
//                            .build();
//
//                    userRepository.save(user);
//
//                    String accessToken = JwtUtil.createAccessToken(user.getId());//신규토큰 생성
//                    String refreshToken = JwtUtil.createRefreshToken(user.getId());
//                    user.setRefreshToken(refreshToken);
//
//                    return DefaultRes.response(HttpStatus.OK.value(), "등록성공", new UserSignInResponseDto(accessToken, refreshToken, user.getId(), user.getNickName()));
//
//                }else{
//                    User userInfo = userRepository.findByEmail(email);
//                    String accessToken = "", refreshToken= "";
//                    if(userInfo != null) {
//                        accessToken = JwtUtil.createAccessToken(userInfo.getId());//신규토큰 생성
//                        refreshToken = JwtUtil.createRefreshToken(userInfo.getId());//신규토큰 생성
//                        info.setAccessToken(accessToken);
//                        userConnectionRepository.save(info);
//
//                        userInfo.setPassword(accessToken);
//                        userInfo.setRefreshToken(refreshToken);
//                        userRepository.save(userInfo);
//                    }
//
//                    return DefaultRes.response(HttpStatus.OK.value(), "토큰수정완료", new UserSignInResponseDto(accessToken, refreshToken, info.getId(), info.getNickName()));
//                }
//            }
//
//        } catch (IOException e) {
//            new IllegalArgumentException("알 수 없는 카카오 로그인 Access Token 요청 URL 입니다 :: " + KAKAO_SNS_TOKEN_BASE_URL);
//        }
//
//        return DefaultRes.response(HttpStatus.OK.value(), "등록수정실패");
//    }

}
