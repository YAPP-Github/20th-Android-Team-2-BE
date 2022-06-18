package yapp.bestFriend.controller.api.user;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import yapp.bestFriend.model.dto.DefaultRes;
import yapp.bestFriend.model.utils.UserUtil;
import yapp.bestFriend.service.user.UserService;

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

    @ApiOperation(value = "사용자 로그아웃 API",notes = "사용자 로그아웃 요청 시 토큰 삭제 처리합니다.")
    @ApiResponses(value ={
            @ApiResponse(code=200, message = "로그아웃 성공")
    })
    @GetMapping("/logout")
    public ResponseEntity<DefaultRes> deleteToken() {
        return new ResponseEntity<>(userService.logout(UserUtil.getId()), HttpStatus.OK);
    }
}
