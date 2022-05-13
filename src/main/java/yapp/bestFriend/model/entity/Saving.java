package yapp.bestFriend.model.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Saving {

    @Id //pk
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SAVING_ID")
    private Long id;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime create_at;

    @LastModifiedDate
    private LocalDateTime updated_at;

    private boolean isCompleted;

    private String productName;

    private String price;

    // 다짐
    private String resolution;


    @Builder
    public Saving(boolean isCompleted, String productName, String price, String resolution){
        this.isCompleted = isCompleted;
        this.productName = productName;
        this.price = price;
        this.resolution = resolution;
    }
}
