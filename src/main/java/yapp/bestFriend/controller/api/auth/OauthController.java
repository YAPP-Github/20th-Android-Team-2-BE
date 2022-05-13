package yapp.bestFriend.controller.api.auth;

import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import yapp.bestFriend.model.dto.DefaultRes;
import yapp.bestFriend.model.enumClass.SocialLoginType;
import yapp.bestFriend.service.auth.OauthService;

@Api(tags = {"소셜 로그인 API"})
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/oauth")
@Slf4j
public class OauthController {
    private final OauthService oauthService;

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
    public ResponseEntity<DefaultRes> socialLoginType(
            @PathVariable(name = "socialLoginType") SocialLoginType socialLoginType) {
        log.info(">> 사용자로부터 SNS 로그인 요청을 받음 :: {} Social Login", socialLoginType);

        return new ResponseEntity<>(oauthService.request(socialLoginType), HttpStatus.OK);
    }
}
