package yapp.bestFriend.service.notification;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import yapp.bestFriend.model.TokenInfo;
import yapp.bestFriend.model.entity.PushNotiHistory;
import yapp.bestFriend.model.enumClass.RequestPushMessage;
import yapp.bestFriend.service.user.UserFcmTokenService;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static yapp.bestFriend.model.enumClass.RequestPushMessage.*;

@Slf4j
@Service
public class NotificationScheduler {
    @Value("${fcm.key.path}")
    private String FCM_PRIVATE_KEY_PATH;

    // 메시징만 권한 설정
    @Value("${fcm.key.scope}")
    private String fireBaseScope;

    private FirebaseMessaging instance;

    @Autowired
    private UserFcmTokenService userFcmTokenService;

    private List<String> tokenList;
    private List<TokenInfo> tokenInfoList;
    private List<PushNotiHistory> pushNotiHList = new ArrayList<>();

    /**
     * 먼저 처음에 저장해둔 파이어베이스 비공개 키 파일을 통해 백엔드에서 파이어베이스에 접속한다.
     */
    @PostConstruct // 의존성 주입이 이루어진 후 초기화를 수행하는 메서드
    public void firebaseSetting(){
        try {
            GoogleCredentials googleCredentials = GoogleCredentials.fromStream(
                    new ClassPathResource(FCM_PRIVATE_KEY_PATH).getInputStream())
                    .createScoped((List.of(fireBaseScope)))
                    ;
            googleCredentials.getAccessToken();
            //SDK 초기화
            FirebaseOptions secondaryAppConfig = FirebaseOptions.builder()
                    .setCredentials(googleCredentials)
                    .build();
            FirebaseApp app = FirebaseApp.initializeApp(secondaryAppConfig);
            this.instance = FirebaseMessaging.getInstance(app);
        }catch (IOException e){
            log.error("파이어베이스 초기화 실패", e.getMessage());
        }
    }

    //@Scheduled(fixedRate = 10000) //10초마다 실행
    @Scheduled(cron="0 0 12 1 * ?") //매월 1일 12시에 실행
    public void pushMorningDietAlarm(){
        log.info("절약 리스트 추가 알림");

        refreshUserList();

        List<Message> messages = new ArrayList<>();
        messages.addAll(getMessage(ADD_SAVING_LIST));//절약 리스트를 추가해보세요!
        messages.addAll(getCustomMessage(REPORT_WAITING));//{}월 리포트가 {}님을 기다려요!

        pushAlarmByTokenList(messages);

        userFcmTokenService.savePushNotiHistory(pushNotiHList);
        pushNotiHList.clear();
    }

    @Scheduled(cron="0 0 12 ? * SAT") //매주 토요일 12:00 푸시
    public void pushCheerUp(){
        log.info("절약 어렵지않죠? 알림");

        refreshUserList();

        List<Message> messages = new ArrayList<>(getCustomMessage(WEEKEND_CHEER_UP));//절약 어렵지않죠?

        pushAlarmByTokenList(messages);

        userFcmTokenService.savePushNotiHistory(pushNotiHList);
        pushNotiHList.clear();
    }

    private void refreshUserList() {
        tokenInfoList = userFcmTokenService.getPushUserWithTokenList();
        tokenList = tokenInfoList.stream().map(TokenInfo::getFcmToken).collect(Collectors.toList());
    }


    private void pushAlarmByTokenList(List<Message> message){
        BatchResponse batchResponse = sendMessage(message);
        List<SendResponse> responsesList = batchResponse.getResponses();
        for(SendResponse sr:responsesList){
            log.info("Message: "+sr.getMessageId());
            log.info("Exception: "+sr.getException());
        }
    }

    /* 메시지 만들기 메서드 - 시작 */
    private List<Message> getMessage(RequestPushMessage data) {
        //알림 보낼 유저 목록
        List<Message> messages = tokenList.stream().map(token -> Message.builder()
                .putData("title", data.getTitle())
                .putData("body", data.getBody())
                .setToken(token)
                .build()).collect(java.util.stream.Collectors.toList());

        for (TokenInfo tokenInfo : tokenInfoList) {
            pushNotiHList.add(userFcmTokenService.makePushNotiHistory(tokenInfo.getUserId(), data.getTitle(), data.getBody()));
        }

        return messages;
    }

    private List<Message> getCustomMessage(RequestPushMessage msg) {
        List<Message> messageList = new ArrayList<>();

        //알림 보낼 유저 목록
        String customMessage = msg.getBody();
        for(TokenInfo tokenInfo: tokenInfoList){
            customMessage = customMessage
                    .replaceFirst("[m]", Integer.toString(LocalDate.now().getMonthValue()))
                    .replaceFirst("[u]", tokenInfo.getNickName());

            Message message = Message.builder()
                    .setNotification(Notification.builder()
                            .setTitle(msg.getTitle())
                            .setBody(customMessage)
                            .build())
                    .setToken(tokenInfo.getFcmToken())
                    .build();
            messageList.add(message);

            pushNotiHList.add(userFcmTokenService.makePushNotiHistory(tokenInfo.getUserId(), msg.getTitle(), customMessage));
        }

        return messageList;
    }
    /* 메시지 만들기 메서드 - 종료 */

    public BatchResponse sendMessage(List<Message> message){
        try{
            return this.instance.sendAll(message);
        }catch (FirebaseMessagingException e) {
            log.error("cannot send to userList push message. error info: {}", e.getMessage());
            return null;
        }
    }
}
