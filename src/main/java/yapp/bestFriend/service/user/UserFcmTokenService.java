package yapp.bestFriend.service.user;

import com.google.firebase.messaging.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import yapp.bestFriend.model.TokenInfo;
import yapp.bestFriend.model.dto.DefaultRes;
import yapp.bestFriend.model.dto.request.FmcRegisterRequest;
import yapp.bestFriend.model.entity.PushNotiHistory;
import yapp.bestFriend.model.entity.User;
import yapp.bestFriend.model.entity.UserFcmToken;
import yapp.bestFriend.model.utils.UserUtil;
import yapp.bestFriend.repository.PushNotiHistoryRepository;
import yapp.bestFriend.repository.UserFcmTokenRepository;
import yapp.bestFriend.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserFcmTokenService {

    private final UserRepository userRepository;
    private final UserFcmTokenRepository userFcmTokenRepository;
    private final PushNotiHistoryRepository pushNotiHistoryRepository;

    public DefaultRes saveFcmToken(FmcRegisterRequest request) {
        // 사용자 토큰 저장 처리
        String fcmToken = request.getFcmToken();
        User user = userRepository.getById(UserUtil.getId());
        UserFcmToken toBeFcmToken;

        Optional<UserFcmToken> existingUserToken = userFcmTokenRepository.findByUserId(user.getId());
        toBeFcmToken = getUserFcmToken(fcmToken, user, existingUserToken);
        userFcmTokenRepository.save(toBeFcmToken);

        return DefaultRes.response(HttpStatus.OK.value(), "저장 성공");
    }

    private UserFcmToken getUserFcmToken(String fcmToken, User user, Optional<UserFcmToken> existingUserToken) {
        UserFcmToken toBeFcmToken;
        if (existingUserToken.isEmpty()) {
            toBeFcmToken = UserFcmToken.builder()
                    .user(user)
                    .fcmToken(fcmToken)
                    .build();
        } else {
            toBeFcmToken = existingUserToken.get();
            toBeFcmToken.setFcmToken(fcmToken);
        }
        return toBeFcmToken;
    }

    public void savePushNotiHistory(List<PushNotiHistory> saveList) {
        // 푸시 알림 이력 저장 처리
        pushNotiHistoryRepository.saveAll(saveList);
    }

    public PushNotiHistory makePushNotiHistory(String userId, String title, String customMessage) {
        return PushNotiHistory.builder()
                .user(userRepository.getById(Long.parseLong(userId)))
                .title(title)
                .body(customMessage)
                .build();
    }

    public List<TokenInfo> getPushUserWithTokenList() {
        List<UserFcmToken> userFcmList = userFcmTokenRepository.findAll();

        List<TokenInfo> resultList = new ArrayList<>();
        for (UserFcmToken uft : userFcmList) {
            resultList.add(TokenInfo.builder()
                    .userId(uft.getUser().getId().toString())
                    .nickName(uft.getUser().getNickName())
                    .fcmToken(uft.getFcmToken())
                    .build());
        }

        return resultList;
    }
}