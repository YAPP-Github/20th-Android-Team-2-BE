package yapp.bestFriend.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yapp.bestFriend.model.dto.DefaultRes;
import yapp.bestFriend.model.entity.Product;
import yapp.bestFriend.model.entity.SavingRecord;
import yapp.bestFriend.model.entity.User;
import yapp.bestFriend.repository.ProductRepository;
import yapp.bestFriend.repository.SavingRecordRepository;
import yapp.bestFriend.repository.UserConnectionRepository;
import yapp.bestFriend.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserConnectionRepository userConnectionRepository;
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

        return DefaultRes.response(HttpStatus.OK.value(), "로그아웃 성공");
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

}
