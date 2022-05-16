package yapp.bestFriend.model.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import yapp.bestFriend.model.entity.Role;

import java.security.Key;
import java.util.Date;

public class JwtUtil {

    private static Key key;

    //리프레시 토큰 유효시간 2주(ms단위)
    public static Long REFRESH_TOKEN_VALID_TIME = 14 * 1440 * 60 * 1000L;

    //엑세스 토큰 유효시간 15분
    public static Long ACCESS_TOKEN_VALID_TIME =  15 * 60 * 1000L;

    public static String ACCESS_TOKEN_NAME = "ACCESS TOKEN";

    public static String REFRESH_TOKEN_NAME = "REFRESH TOKEN";

    public JwtUtil(String secret) {
        key = Keys.hmacShaKeyFor(secret.getBytes());
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
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

    }

    // 토큰의 유효성 + 만료일자 확인
    public static Claims getClaims(String token) {
        try{
            return Jwts.parser()
                    .setSigningKey(key)
                    .parseClaimsJws(token)
                    .getBody();

        }catch (Exception e){
            return null;
        }
    }
}
