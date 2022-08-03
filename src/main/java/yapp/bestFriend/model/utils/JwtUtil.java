package yapp.bestFriend.model.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import yapp.bestFriend.model.entity.Role;
import yapp.bestFriend.service.v1.user.UserDetails;

import java.security.Key;
import java.util.Date;
import java.util.Locale;

public class JwtUtil {

    private static Key key;

    //리프레시 토큰 유효시간 2주(ms단위)
    public static Long REFRESH_TOKEN_VALID_TIME = 14 * 1440 * 60 * 1000L;

    //엑세스 토큰 유효시간 15분
//    public static Long ACCESS_TOKEN_VALID_TIME =  15 * 60 * 1000L;
    public static Long ACCESS_TOKEN_VALID_TIME =  15 * 60 * 12 * 1000L; //엑세스 토큰 유효시간 3시간(임시)

    public static String ACCESS_TOKEN_NAME = "ACCESS TOKEN";

    public static String REFRESH_TOKEN_NAME = "REFRESH TOKEN";

    public JwtUtil(String secret) {
        key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    private enum Claim {
        USER_PK
    }

    //엑세스 토큰 발급
    public static String createAccessToken(Long userId){

        Date now = new Date();

        return Jwts.builder()
                .claim("user_pk", userId) //정보 저장
                .claim("user_role", Role.USER)
                .claim("token_name", ACCESS_TOKEN_NAME)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + ACCESS_TOKEN_VALID_TIME))
                .signWith(key, SignatureAlgorithm.HS256) //암호화 알고리즘을 뭐로 할것인지
                .compact();
    }

    //리프레쉬 토큰 발급
    public static String createRefreshToken(Long userId){

        Date now = new Date();

        return Jwts.builder()
                .claim("user_pk", userId) //정보 저장
                .claim("user_role", Role.USER)
                .claim("token_name", REFRESH_TOKEN_NAME)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + REFRESH_TOKEN_VALID_TIME))
                .signWith(key, SignatureAlgorithm.HS256) //암호화 알고리즘을 뭐로 할것인지
                .compact();

    }

    public Long getUserIdFromToken(String token) {
        return getClaimValueFromToken(token, Claim.USER_PK.name().toLowerCase(Locale.ROOT), Long.class);
    }

    private <T> T getClaimValueFromToken(String token, String type, Class<T> requiredType) {
        return getClaimsFromToken(token).get(type, requiredType);
    }

    // 토큰의 유효성 + 만료일자 확인
    public static Claims getClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build().parseClaimsJws(token)
                .getBody();
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        Long userId = getUserIdFromToken(token);
        return userId.equals(userDetails.getUserId()) && notExpiredToken(token);
    }

    public boolean notExpiredToken(String token) {
        return !expiredToken(token);
    }

    public boolean expiredToken(String token) {
        return getExpirationDateFromToken(token).before(new Date());
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimsFromToken(token).getExpiration();
    }

}
