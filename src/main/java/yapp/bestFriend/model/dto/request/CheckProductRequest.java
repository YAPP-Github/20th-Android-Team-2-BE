package yapp.bestFriend.model.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class CheckProductRequest {

    private Long userId;

    private Long productId;

    private LocalDate today;
}
