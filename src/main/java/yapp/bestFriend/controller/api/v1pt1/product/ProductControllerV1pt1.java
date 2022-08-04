package yapp.bestFriend.controller.api.v1pt1.product;

import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import yapp.bestFriend.model.dto.DefaultRes;
import yapp.bestFriend.model.dto.request.v1pt1.product.CreateProductRequest;
import yapp.bestFriend.model.dto.res.v1pt.SimpleProductResponse;
import yapp.bestFriend.model.utils.UserUtil;
import yapp.bestFriend.service.v1pt1.ProductServiceV1pt1;

import javax.validation.Valid;
import java.util.List;

@Api(tags = {"절약 목록 API(Ver 1.1)"})
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ProductControllerV1pt1 {

    private final ProductServiceV1pt1 productService;

    @ApiOperation(value = "절약 목록 조회 API", notes = "절약 리스트를 조회할 때 사용되는 API입니다")
    @ApiResponses(value ={
            @ApiResponse(code = 200, message = "1. 조회 성공 \t\n 2. 조회 실패(사용자 정보 없음) \t\n 3. 데이터 없음 \t\n"),
    })
    @ApiImplicitParam(name = "recordYmd", value = "기록 일자(YYYYMMDD) ex)20220601", required = true, paramType = "query", defaultValue = "")
    @GetMapping(value = "/products", headers="X-API-VERSION=1.1")
    public ResponseEntity<DefaultRes<List<SimpleProductResponse>>> getProductListV1pt1(@RequestParam("recordYmd") String recordYmd){
        return new ResponseEntity<>(productService.getProductListV1pt1(UserUtil.getId(), recordYmd), HttpStatus.OK);
    }

    @ApiOperation(value = "절약 등록 API", notes = "절약 리스트에 새로운 절약을 등록하여 추가할 때 사용되는 API입니다")
    @ApiResponses(value ={
            @ApiResponse(code = 200, message = "1. 등록 성공 \t\n 2. 등록 실패(사용자 정보 없음) \t\n"),
    })
    @PostMapping(value = "/products", headers="X-API-VERSION=1.1")
    public ResponseEntity<DefaultRes> createProductV1pt1(@Valid @RequestBody CreateProductRequest request){
        return new ResponseEntity<>(productService.createProductV1pt1(UserUtil.getId(), request), HttpStatus.OK);
    }
}
