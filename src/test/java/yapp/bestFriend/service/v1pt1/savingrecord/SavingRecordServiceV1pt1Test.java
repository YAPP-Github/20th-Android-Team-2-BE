package yapp.bestFriend.service.v1pt1.savingrecord;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import yapp.bestFriend.model.dto.request.v1pt1.product.UpdateSavingRecordsRequest;
import yapp.bestFriend.model.entity.Product;
import yapp.bestFriend.model.entity.SavingRecord;
import yapp.bestFriend.model.entity.User;
import yapp.bestFriend.repository.ProductRepository;
import yapp.bestFriend.repository.SavingRecordRepository;
import yapp.bestFriend.repository.UserRepository;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class SavingRecordServiceV1pt1Test {
    @Mock
    private UserRepository userRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private SavingRecordRepository savingRecordRepository;

    @InjectMocks
    private SavingRecordServiceV1pt1 savingRecordService;

    private static User user;
    private static Product prodInput;
    private static SavingRecord savingRecord;

    @BeforeAll
    static void BeforeAll(){
        //given 1 - user 정보
        user = User.builder()
                .nickName("test1")
                .email("test@naver.com")
                .password("123456")
                .build();

        //given 2 - product 정보
        prodInput = Product.builder().user(user)
                .name("빙수")
                .freqType("3")
                .freqInterval("1,3,5")
                .startYmd("2022-07-31")
                .endYmd("2022-08-31")
                .build();

        //given 3 - savingRecord 정보
        savingRecord = SavingRecord.builder()
                .user(user)
                .product(prodInput)
                .recordYmd(LocalDate.of(2022,8,1))
                .build();

        //given 3 - user product 정보 update
        user.setSavingRecord(Arrays.asList(savingRecord));
    }

    @Test
    @DisplayName("절약 금액 수정 API(Ver 1.1) - 과거일자 금액 수정(정상)")
    void updateSavingRecord(){
        //given
        UpdateSavingRecordsRequest request = UpdateSavingRecordsRequest.builder()
                .productId(user.getSavingList().get(0).getProduct().getId())
                .price("4000")
                .requestYmd(LocalDate.of(2022,8,1))
                .build();

        //when
        when(userRepository.findById(any())).thenReturn(Optional.ofNullable(user));
        when(productRepository.findById(request.getProductId())).thenReturn(Optional.ofNullable(prodInput));
        when(savingRecordRepository.findByUserIdAndRecordYmdAndProductId(1L, request.getRequestYmd(), request.getProductId()))
                .thenReturn(Optional.ofNullable(savingRecord));
        given(savingRecordRepository.save(any())).willReturn(savingRecord);

        //then
        assertThat(savingRecordService.updateSavingRecord(1L, request))
                .hasFieldOrPropertyWithValue("statusCode", HttpStatus.OK.value())
                .hasFieldOrPropertyWithValue("Message","수정 성공");
    }
}