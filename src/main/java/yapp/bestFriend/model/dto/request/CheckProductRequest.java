package yapp.bestFriend.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CheckProductRequest {

    private Long userId;

    private Long productId;

    private LocalDate today;
}
