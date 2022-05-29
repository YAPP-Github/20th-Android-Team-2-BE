package yapp.bestFriend.service.user;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import yapp.bestFriend.model.dto.DefaultRes;
import yapp.bestFriend.model.dto.res.ReplaceTokenResponseDto;
import yapp.bestFriend.model.entity.User;
import yapp.bestFriend.model.utils.JwtUtil;
import yapp.bestFriend.repository.UserRepository;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SessionService {

    private final UserRepository userRepository;

    public DefaultRes replaceToken(HttpServletRequest request){

        String token = request.getHeader("Authorization");

        if (token == null) {
            return DefaultRes.response(HttpStatus.UNAUTHORIZED.value(), "요청에러");
        }

        String jwtToken = token.substring("Bearer ".length());
        Claims claims = JwtUtil.getClaimsFromToken(jwtToken);
        if (claims == null){
            return DefaultRes.response(HttpStatus.UNAUTHORIZED.value(), "토큰불일치");
        }

        String tokenName = claims.get("token_name", String.class);
        String userGrade = claims.get("user_grade", String.class);

        if(tokenName.equals(JwtUtil.REFRESH_TOKEN_NAME)){

            Long userPk = claims.get("user_pk", Long.class);
            Boolean check = refreshTokenCheck(userPk, jwtToken);
            if(check){
                String accessToken;
                accessToken = JwtUtil.createAccessToken(userPk);
                return DefaultRes.response(HttpStatus.OK.value(), "토큰재발급", new ReplaceTokenResponseDto(accessToken));
            }else{
                return DefaultRes.response(HttpStatus.UNAUTHORIZED.value(), "토큰불일치");
            }

        }else{
            return DefaultRes.response(HttpStatus.UNAUTHORIZED.value(), "토큰불일치");
        }
    }

    public Boolean refreshTokenCheck(Long id, String refreshToken){
        Optional<User> optionalUser = userRepository.findById(id);

        return optionalUser.map(user -> {
            if(user.getToken().equals(refreshToken)){
                return true;
            }else{
                return false;
            }
        }).orElse(false);
    }
}
