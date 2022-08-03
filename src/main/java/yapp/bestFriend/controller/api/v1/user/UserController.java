package yapp.bestFriend.controller.api.v1.user;

import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import yapp.bestFriend.model.dto.DefaultRes;
import yapp.bestFriend.model.dto.request.user.UserSignUpRequestDto;
import yapp.bestFriend.model.utils.UserUtil;
import yapp.bestFriend.service.v1.user.UserService;

import javax.validation.Valid;

@Api(tags = {"사용자 API"})
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/user")
@Slf4j
public class UserController {
    private final UserService userService;

    @ApiOperation(value = "사용자 탈퇴 API",notes = "사용자 탈퇴 처리 시에 사용합니다.")
    @ApiResponses(value ={
            @ApiResponse(code=200, message = "탈퇴 성공")
    })
    @GetMapping("/withdrawal")
    public ResponseEntity<DefaultRes> withdrawal() {
        return new ResponseEntity<>(userService.withdrawal(UserUtil.getId()), HttpStatus.OK);
    }

    @ApiOperation(value = "회원 가입 API",notes = "회원 가입 API 입니다.")
    @ApiResponses(value ={
            @ApiResponse(code=200, message = "1.성공 \t\n 2.이메일중복")
    })
    @PostMapping("/signup")
    public ResponseEntity<DefaultRes> signUp(
            @ApiParam(value = "필수 : 모든 항목" +
                    " \t\n 비밀번호는 인코딩 처리 ")
            @Valid @RequestBody UserSignUpRequestDto requestDto
    ){
        return new ResponseEntity<>(userService.signUp(requestDto), HttpStatus.OK);
    }

    @ApiOperation(value = "로그인 API",notes = "로그인 API 입니다.")
    @ApiResponses(value ={
            @ApiResponse(code=200, message = "1.성공 \t\n 2.아이디불일치 \t\n 3.비밀번호불일치")
    })
    @PostMapping("/signin")
    public ResponseEntity<DefaultRes> signin(
            @Valid @RequestBody UserSignUpRequestDto requestDto
    ){
        return new ResponseEntity<>(userService.signIn(requestDto), HttpStatus.OK);
    }

    @ApiOperation(value = "사용자 로그아웃 API",notes = "사용자 로그아웃 요청 시 토큰 삭제 처리합니다.")
    @ApiResponses(value ={
            @ApiResponse(code=200, message = "로그아웃 성공")
    })
    @GetMapping("/logout")
    public ResponseEntity<DefaultRes> deleteToken() {
        return new ResponseEntity<>(userService.logout(UserUtil.getId()), HttpStatus.OK);
    }
}
