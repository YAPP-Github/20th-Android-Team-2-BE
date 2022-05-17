package yapp.bestFriend.model.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 스프링 세큐리티에서는 권한코드에 항상 ROLE_이 앞에 있어야만 한다.
 */
@Getter
@RequiredArgsConstructor
public enum Role {
    GUEST("ROLE_GUEST","손님"),
    USER("ROLE_USER","일반 사용자");

    private final String key;
    private final String title;
}
