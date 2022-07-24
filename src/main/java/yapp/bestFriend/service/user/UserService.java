package yapp.bestFriend.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yapp.bestFriend.model.dto.DefaultRes;
import yapp.bestFriend.model.dto.request.user.UserSignUpRequestDto;
import yapp.bestFriend.model.dto.res.user.UserSignInResponseDto;
import yapp.bestFriend.model.entity.Product;
import yapp.bestFriend.model.entity.SavingRecord;
import yapp.bestFriend.model.entity.User;
import yapp.bestFriend.model.entity.UserFcmToken;
import yapp.bestFriend.model.utils.JwtUtil;
import yapp.bestFriend.repository.*;
import yapp.bestFriend.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserConnectionRepository userConnectionRepository;
    private final UserFcmTokenRepository userFcmTokenRepository;
    private final ProductRepository productRepository;
    private final SavingRecordRepository savingRecordRepository;

    @Transactional(rollbackFor = Exception.class)
    public DefaultRes withdrawal(long userId) {
        // 사용자 탈퇴 처리
        Optional<User> user = userRepository.findById(userId);
        if(user.isPresent()){
            User existingUser = user.get();

            softDeleteUserInfo(existingUser);
            deleteUserConnectionInfo(existingUser);
            deleteProduct(userId);
            softDeleteSavingRecord(userId);
            deleteFcmToken(userId);
        }

        return DefaultRes.response(HttpStatus.OK.value(), "탈퇴 성공");
    }

    @Transactional(rollbackFor = Exception.class)
    public DefaultRes logout(long userId) {
        // 사용자 로그아웃 처리
        Optional<User> user = userRepository.findById(userId);
        if(!user.isPresent()){
            return DefaultRes.response(HttpStatus.NOT_FOUND.value(), "사용자 정보가 존재하지 않습니다.");
        }

        User existingUser = user.get();
        existingUser.deleteUserConnection();
        userRepository.save(existingUser);

        if(userConnectionRepository.getById(userId) != null){
            deleteUserConnectionInfo(existingUser);
        }

        deleteFcmToken(userId);

        return DefaultRes.response(HttpStatus.OK.value(), "로그아웃 성공");
    }

    private void deleteFcmToken(long userId) {
        Optional<UserFcmToken> userFcmToken = userFcmTokenRepository.findByUserId(userId);
        if(userFcmToken.isPresent()){
            userFcmTokenRepository.delete(userFcmToken.get());
        }
    }

    private void softDeleteSavingRecord(long userId) {
        List<SavingRecord> savingRecords = savingRecordRepository.findByUserIdAndDeletedYn(userId, false);
        for(SavingRecord sr : savingRecords){
            sr.delete();
        }
        savingRecordRepository.saveAll(savingRecords);
    }

    private void deleteProduct(long userId) {
        List<Product> product = productRepository.findByUserIdAndDeletedYn(userId, false);
        for(Product p : product){
            p.delete();
        }
        productRepository.saveAll(product);
    }

    private void deleteUserConnectionInfo(User existingUser) {
        //사용자 커넥션 정보 삭제(토큰 정보는 hard-delete 가 좋음)
        userConnectionRepository.deleteByEmail(existingUser.getEmail());
    }

    private void softDeleteUserInfo(User existingUser) {
        existingUser.delete();
        existingUser.deleteUserConnection();
        userRepository.save(existingUser);
    }

    public DefaultRes signUp(UserSignUpRequestDto requestDto){

        String email = requestDto.getEmail();
        Optional<String> optionalEmail = userRepository.findFirstByEmail(email);

        return optionalEmail
                .map(e -> DefaultRes.response(HttpStatus.OK.value(), "이메일중복"))
                .orElseGet(()->{
                    userRepository.save(requestDto.toEntity());
                    return DefaultRes.response(HttpStatus.OK.value(),"성공");
                });
    }

    @Transactional
    public DefaultRes<UserSignInResponseDto> signIn(UserSignUpRequestDto requestDto){

        Optional<String> optionalEmail = userRepository.findFirstByEmail(requestDto.getEmail());

        return optionalEmail.map(email ->{

            Optional<User> optionalUser = userRepository.findByUser(email, requestDto.getPassword());

            return optionalUser.map(user -> {
                String accessToken = JwtUtil.createAccessToken(user.getId());
                String refreshToken = JwtUtil.createRefreshToken(user.getId());
                user.setRefreshToken(refreshToken);

                return DefaultRes.response(HttpStatus.OK.value(), "등록성공",
                        new UserSignInResponseDto(accessToken, refreshToken, user.getId(), user.getNickName(), user.getEmail(), user.getCreatedAt()));

            }).orElseGet(()-> DefaultRes.response(HttpStatus.OK.value(), "비밀번호불일치"));

        }).orElseGet(()->DefaultRes.response(HttpStatus.OK.value(), "아이디불일치"));

    }
}
