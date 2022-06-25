package yapp.bestFriend.model.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class PushNotiHistory extends BaseInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String title;

    private String body;

    @Builder
    public PushNotiHistory(User user, String title, String body) {
        this.user = user;
        this.title = title;
        this.body = body;
    }
}