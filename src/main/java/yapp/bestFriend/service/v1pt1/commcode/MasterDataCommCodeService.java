package yapp.bestFriend.service.v1pt1.commcode;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import yapp.bestFriend.model.dto.DefaultRes;
import yapp.bestFriend.model.dto.res.v1pt.SimpleProductSampleResponse;
import yapp.bestFriend.repository.MasterDataCommCodeRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MasterDataCommCodeService {

    private final MasterDataCommCodeRepository masterDataCommCodeRepository;

    public DefaultRes<List<SimpleProductSampleResponse>> getCodeNmList() {
        List<SimpleProductSampleResponse> simpleProductSampleResponses
                = masterDataCommCodeRepository.findByTypeCode("PRODUCT_SAMPLE").stream()
                .map(sample->new SimpleProductSampleResponse(sample.getCommCode()))
                .collect(Collectors.toList());

        return DefaultRes.response(HttpStatus.OK.value(), "조회성공", simpleProductSampleResponses);
    }
}
