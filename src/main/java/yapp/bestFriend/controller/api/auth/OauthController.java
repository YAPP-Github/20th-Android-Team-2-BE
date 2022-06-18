package yapp.bestFriend.controller.api.auth;

import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;
import yapp.bestFriend.model.dto.DefaultRes;
import yapp.bestFriend.model.dto.request.SocialLoginRequest;
import yapp.bestFriend.model.enumClass.SocialLoginType;
import yapp.bestFriend.service.auth.OauthService;

import javax.validation.Valid;

/* 22-06-05 : Rest API 방식 -> 안드로이드 SDK 방식으로 변경
 * */
@Api(tags = {"소셜 로그인 API"})
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/oauth")
@Slf4j
public class OauthController {
    private final OauthService oauthService;

    @ApiOperation(value = "소셜로그인 API",notes = "소셜로그인 API 입니다.")
    @ApiResponses(value ={
            @ApiResponse(code = 200, message = "등록 성공"),
    })
    @PostMapping(value = "/sign-in")
    public ResponseEntity<DefaultRes> SocialLoginRequest(@Valid @RequestBody SocialLoginRequest request){
        return new ResponseEntity<>(oauthService.requestLogin(request), HttpStatus.OK);
    }

    /**
     * 사용자로부터 SNS 로그인 요청을 Social Login Type 을 받아 처리
     * @param socialLoginType (KAKAO)
     */
    @ApiOperation(value = "소셜로그인 API",notes = "소셜로그인 API 입니다.")
    @ApiResponses(value ={
            @ApiResponse(code=200, message = "리다이렉트주소")
    })
    @ApiParam("KAKAO")
    @GetMapping(value = "/{socialLoginType}")
    @ApiIgnore
    public ResponseEntity<DefaultRes> socialLoginType(
            @PathVariable(name = "socialLoginType") SocialLoginType socialLoginType) {
        log.info(">> 사용자로부터 SNS 로그인 요청을 받음 :: {} Social Login", socialLoginType);

        return new ResponseEntity<>(oauthService.request(socialLoginType), HttpStatus.OK);
    }

//    /**
//     * Social Login API Server 요청에 의한 callback 을 처리
//     * @param socialLoginType (GOOGLE, FACEBOOK)
//     * @param code API Server 로부터 넘어노는 code
//     * @return SNS Login 요청 결과로 받은 Json 형태의 String 문자열 (access_token, refresh_token 등)
//     */
//    @GetMapping(value = "/callback/{socialLoginType}")
//    @ApiIgnore
//    public ResponseEntity<DefaultRes> callback(
//            @PathVariable(name = "socialLoginType") SocialLoginType socialLoginType,
//            @RequestParam(name = "code") String code) {
//        log.info(">> 소셜 로그인 API 서버로부터 받은 code :: {}", code);
//        return new ResponseEntity<>(oauthService.requestAccessToken(socialLoginType, code), HttpStatus.OK);
//    }
}
