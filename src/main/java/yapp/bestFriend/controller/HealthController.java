package yapp.bestFriend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@ApiIgnore
public class HealthController {
    @GetMapping("/health-check")
    public ResponseEntity<HttpStatus> healthCheck() {
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
