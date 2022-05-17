package yapp.bestFriend.model.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "users",
        uniqueConstraints = {
                @UniqueConstraint(name = "UniqueEmail", columnNames = {"email"})
        })
public class User extends BaseTime {
    @Id //pk
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    private String email;

    private String password;

    private String nickName;

    @Enumerated(EnumType.STRING)//DB로 저장할 떄 Enum 값을 어떤 형태로 저장할지 결정
    private Role role;

    private String token;

    @OneToOne(fetch = FetchType.LAZY, targetEntity = UserConnection.class)
    @JoinColumn(name = "user_connection_id")
    private UserConnection userConnection;

    @Builder
    public User(String email, String password, String nickName, UserConnection userConnection, Role role) {
        this.email = email;
        this.password = password;
        this.nickName = nickName;
        this.userConnection = userConnection;
        this.role = role;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", nickName='" + nickName + '\'' +
                super.toString() +
                '}';
    }
  
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "user")
    private List<Product> productList = new ArrayList<Product>();

    public void setRefreshToken(String refreshToken) {
        this.token = refreshToken;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}