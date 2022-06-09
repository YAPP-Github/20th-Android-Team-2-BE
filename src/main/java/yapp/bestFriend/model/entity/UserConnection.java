package yapp.bestFriend.model.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import yapp.bestFriend.model.enumClass.SocialLoginType;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class UserConnection extends BaseInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "email")
    private String email;

    @Column(name = "provider")
    @Enumerated(EnumType.STRING)
    private SocialLoginType provider;

    @Column(name = "provider_id")
    private Long providerId;

    @Column(name = "nick_name")
    private String nickName;

    @Column(name = "access_token")
    private String accessToken;

    public void setAccessToken(String accessToken){
        this.accessToken = accessToken;
    }

    @Builder
    private UserConnection(String email, SocialLoginType provider, Long providerId, String nickName,
                           String accessToken) {
        this.email = email;
        this.provider = provider;
        this.providerId = providerId;
        this.nickName = nickName;
        this.accessToken = accessToken;
    }

}
