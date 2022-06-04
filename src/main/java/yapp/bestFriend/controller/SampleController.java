package yapp.bestFriend.controller;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;
import yapp.bestFriend.model.dto.DefaultRes;
import yapp.bestFriend.model.entity.User;

@Api(tags = {"sample"})
@RestController
@Slf4j
@ApiIgnore
public class SampleController {

    @GetMapping("/sample")
    public ResponseEntity sample() {
        User user = new User();

        return ResponseEntity
                .ok(DefaultRes.response(HttpStatus.OK.value(), "정상", user));
    }

    @GetMapping("/sample/param")
    public ResponseEntity sampleParam(
            @RequestParam("email") String email,
            @RequestParam("password") String password
    ) {
        User user = User.builder()
                .email(email)
                .password(password)
                .build();

        return ResponseEntity
                .ok(DefaultRes.response(HttpStatus.OK.value(), "정상", user));
    }
}

