package yapp.bestFriend.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import yapp.bestFriend.model.dto.DefaultRes;
import yapp.bestFriend.model.dto.request.FmcRegisterRequest;
import yapp.bestFriend.model.entity.PushNotiHistory;
import yapp.bestFriend.model.entity.User;
import yapp.bestFriend.model.entity.UserFcmToken;
import yapp.bestFriend.model.enumClass.RequestPushMessage;
import yapp.bestFriend.model.utils.UserUtil;
import yapp.bestFriend.repository.user.PushNotiHistoryRepository;
import yapp.bestFriend.repository.UserFcmTokenRepository;
import yapp.bestFriend.repository.user.UserRepository;

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
        if(!existingUserToken.isPresent()){
            toBeFcmToken = UserFcmToken.builder()
                    .user(user)
                    .fcmToken(fcmToken)
                    .build();
        }else{
            toBeFcmToken = existingUserToken.get();
            toBeFcmToken.setFcmToken(fcmToken);
        }
        return toBeFcmToken;
    }

    public void savePushNotiHistory(List<String> tokenList, RequestPushMessage messageType) {
        // 푸시 알림 이력 저장 처리
        for(String token : tokenList){
            UserFcmToken userFcmToken = userFcmTokenRepository.findByFcmToken(token);
            PushNotiHistory pushNotiHistory = PushNotiHistory.builder()
                    .user(userFcmToken.getUser())
                    .title(messageType.getTitle())
                    .title(messageType.getBody())
                    .build();
            pushNotiHistoryRepository.save(pushNotiHistory);
        }
    }

    public List<String> getPushUserList() {
        return userFcmTokenRepository.findAll().stream().map(UserFcmToken::getFcmToken).collect(
                java.util.stream.Collectors.toList());
    }
}


//        this.user = user;
//                this.title = title;
//                this.body = body;