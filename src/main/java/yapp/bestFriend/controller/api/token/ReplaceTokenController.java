package yapp.bestFriend.controller.api.token;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import yapp.bestFriend.model.dto.DefaultRes;
import yapp.bestFriend.service.user.SessionService;

import javax.servlet.http.HttpServletRequest;

@Api(tags = {"엑세스 토큰 재발급 API"})
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ReplaceTokenController {

    private final SessionService sessionService;

    @ApiOperation(value = "엑세스 토큰 재발급 API",notes = "리프레시 토큰이 유효하다면 엑세스 토큰을 재발급 합니다.(유효하지 않다면 로그아웃 처리")
    @ApiResponses(value ={
            @ApiResponse(code=200, message = "1. 토큰재발급"),
            @ApiResponse(code=401, message = "1. 요청에러 \n 2. 토큰불일치 \n 3.토큰만료")
    })
    @GetMapping("/token")
    public ResponseEntity<DefaultRes> replaceToken(
            HttpServletRequest request
    ){
        DefaultRes defaultRes = sessionService.replaceToken(request);

        if (defaultRes.getStatusCode() == HttpStatus.UNAUTHORIZED.value()){
            return new ResponseEntity<>(defaultRes, HttpStatus.UNAUTHORIZED);
        }else{
            return new ResponseEntity<>(defaultRes, HttpStatus.OK);
        }

    }
}
