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
@Table(name = "users")
public class User extends BaseTime {
    @Id //pk
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String password;

    private String nickName;

    @Builder
    public User(Long id, String email, String password, String nickName){
        this.id = id;
        this.email = email;
        this.password = password;
        this.nickName = nickName;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", nickName='" + nickName + '\'' +
                super.toString()+
                '}';
    }

    // 2022-05-14 김유비
    // User - Saving 일대다 단방향 연관관계 매핑
    @OneToMany
    @JoinColumn(name = "SAVING_ID")
    private List<Saving> savingList = new ArrayList<Saving>();

}