package yapp.bestFriend.service.auth;

import com.google.gson.JsonParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Component;
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

            //POST ????????? ?????? ???????????? false??? setDoOutput??? true???
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            //POST ????????? ????????? ???????????? ???????????? ???????????? ?????? ??????
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            StringBuilder sb = new StringBuilder();
            sb.append("grant_type=authorization_code");
            sb.append("&client_id=" + KAKAO_SNS_CLIENT_ID);
            sb.append("&redirect_uri=" + KAKAO_SNS_CALLBACK_URL);
            sb.append("&client_secret=" + KAKAO_SNS_CLIENT_SECRET);
            sb.append("&code=" + code);
            bw.write(sb.toString());
            bw.flush();

            //?????? ????????? 200????????? ??????
            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                //????????? ?????? ?????? JSON????????? Response ????????? ????????????
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line = "";
                String result = "";
                while ((line = br.readLine()) != null) {
                    result += line;
                }

                //Gson ?????????????????? ????????? ???????????? JSON?????? ?????? ??????
                access_Token = JsonParser.parseString(result)
                        .getAsJsonObject()
                        .get("access_token")
                        .getAsString();

                br.close();
                bw.close();
            }
        } catch (IOException e) {
            throw new IllegalArgumentException("??? ??? ?????? ????????? ????????? Access Token ?????? URL ????????? :: " + KAKAO_SNS_TOKEN_BASE_URL);
        }
        return access_Token;
    }

    /**
     * ?????????????????? SNS ????????? ????????? ?????? ????????? ??? ????????? ?????? ?????? ??????
     */
    public DefaultRes requestAccessTokenUsingUserData(SocialLoginRequest request) {

        //??????????????? ?????? body ????????? ?????????
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

            User user = User.builder()
                    .email(userConnection.getEmail())
                    .nickName(userConnection.getNickName())
                    .userConnection(userConnection)
                    .role(Role.USER)
                    .build();

            userRepository.save(user);

            //???????????? ??????
            String accessToken = JwtUtil.createAccessToken(user.getId());
            String refreshToken = JwtUtil.createRefreshToken(user.getId());

            userConnection.setAccessToken(accessToken);
            userConnectionRepository.save(userConnection);

            user.setPassword(accessToken);
            user.setRefreshToken(refreshToken);
            userRepository.save(user);

            return DefaultRes.response(HttpStatus.OK.value(), "????????????", new UserSignInResponseDto(accessToken, refreshToken, user.getId(), user.getNickName()));

        }else{
            User userInfo = userRepository.findByEmail(email);
            String accessToken = "", refreshToken= "";
            if(userInfo != null) {
                accessToken = JwtUtil.createAccessToken(userInfo.getId());//???????????? ??????
                refreshToken = JwtUtil.createRefreshToken(userInfo.getId());//???????????? ??????
                info.setAccessToken(accessToken);
                userConnectionRepository.save(info);

                userInfo.setPassword(accessToken);
                userInfo.setRefreshToken(refreshToken);
                userRepository.save(userInfo);
            }

            return DefaultRes.response(HttpStatus.OK.value(), "??????????????????", new UserSignInResponseDto(accessToken, refreshToken, userInfo.getId(), info.getNickName()));
        }
    }

//    public DefaultRes requestAccessTokenUsingURL(String access_token) {
//
//        log.info(">> ?????? ????????? API ??????????????? ?????? access_token :: {}", access_token);
//
//        String reqURL = "https://kapi.kakao.com/v2/user/me";
//
//        try {
//            URL url = new URL(reqURL);
//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//
//            //????????? ????????? Header??? ????????? ??????
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
//                //ObjectMapper??? JSON?????? ????????? ??????
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
//                    String accessToken = JwtUtil.createAccessToken(user.getId());//???????????? ??????
//                    String refreshToken = JwtUtil.createRefreshToken(user.getId());
//                    user.setRefreshToken(refreshToken);
//
//                    return DefaultRes.response(HttpStatus.OK.value(), "????????????", new UserSignInResponseDto(accessToken, refreshToken, user.getId(), user.getNickName()));
//
//                }else{
//                    User userInfo = userRepository.findByEmail(email);
//                    String accessToken = "", refreshToken= "";
//                    if(userInfo != null) {
//                        accessToken = JwtUtil.createAccessToken(userInfo.getId());//???????????? ??????
//                        refreshToken = JwtUtil.createRefreshToken(userInfo.getId());//???????????? ??????
//                        info.setAccessToken(accessToken);
//                        userConnectionRepository.save(info);
//
//                        userInfo.setPassword(accessToken);
//                        userInfo.setRefreshToken(refreshToken);
//                        userRepository.save(userInfo);
//                    }
//
//                    return DefaultRes.response(HttpStatus.OK.value(), "??????????????????", new UserSignInResponseDto(accessToken, refreshToken, info.getId(), info.getNickName()));
//                }
//            }
//
//        } catch (IOException e) {
//            new IllegalArgumentException("??? ??? ?????? ????????? ????????? Access Token ?????? URL ????????? :: " + KAKAO_SNS_TOKEN_BASE_URL);
//        }
//
//        return DefaultRes.response(HttpStatus.OK.value(), "??????????????????");
//    }

}
