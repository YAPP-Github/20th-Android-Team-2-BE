package yapp.bestFriend.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDate;

@Getter
@Entity
@NoArgsConstructor
public class Calendar {

    @Id //pk
    private LocalDate de;

    private Integer day_num;

    private String ds;
}
