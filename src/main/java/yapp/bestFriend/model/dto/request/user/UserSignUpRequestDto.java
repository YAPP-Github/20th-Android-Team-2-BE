package yapp.bestFriend.model.dto.request.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import yapp.bestFriend.model.entity.Role;
import yapp.bestFriend.model.entity.User;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserSignUpRequestDto {

    @Email
    @NotEmpty
    private String email;

    @NotEmpty
    private String password;

    @Builder
    public User toEntity(){

        return User.builder()
                .email(email)
                .password(password)
                .role(Role.USER)
                .build();

    }
}
