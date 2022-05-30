package yapp.bestFriend.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import yapp.bestFriend.model.dto.DefaultRes;
import yapp.bestFriend.model.dto.request.CreateProductRequest;
import yapp.bestFriend.model.dto.request.UpdateProductRequest;
import yapp.bestFriend.model.dto.response.SimpleProductResponse;
import yapp.bestFriend.service.ProductService;
import javax.validation.Valid;
import java.util.List;

@Api(tags = {"절약 목록 API"})
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @ApiOperation(value = "절약 등록 API", notes = "절약 리스트에 새로운 절약을 등록하여 추가할 때 사용되는 API입니다")
    @ApiResponses(value ={
            @ApiResponse(code = 200, message = "1. 등록 성공 \t\n 2. 등록 실패(사용자 정보 없음) \t\n"),
    })
    @PostMapping(value = "/products")
    public ResponseEntity<DefaultRes> createProduct(@Valid @RequestBody CreateProductRequest request){
        return new ResponseEntity<>(productService.createProduct(request), HttpStatus.OK);
    }

    @ApiOperation(value = "절약 목록 조회 API", notes = "절약 리스트를 조회할 때 사용되는 API입니다")
    @ApiResponses(value ={
            @ApiResponse(code = 200, message = "1. 조회 성공 \t\n 2. 조회 실패(사용자 정보 없음) \t\n 3. 데이터 없음 \t\n"),
    })
    @GetMapping(value = "/products/{userId}")
    public ResponseEntity<DefaultRes<List<SimpleProductResponse>>> getProductList(@PathVariable("userId") Long userId){
        return new ResponseEntity<>(productService.getProductList(userId), HttpStatus.OK);
    }

    @ApiOperation(value = "절약 수정 API", notes = "절약을 수정할 때 사용되는 API입니다")
    @ApiResponses(value ={
            @ApiResponse(code = 200, message = "1. 수정 성공 \t\n 2. 수정 실패(사용자 정보 없음) \t\n 3. 수정 실패(절약 정보 없음) \t\n"),
    })
    @PatchMapping (value = "/products")
    public ResponseEntity<DefaultRes> updateProduct(@Valid @RequestBody UpdateProductRequest request){
        return new ResponseEntity<>(productService.updateProduct(request), HttpStatus.OK);
    }

    @ApiOperation(value = "절약 삭제 API", notes = "절약을 삭제할 때 사용되는 API입니다")
    @ApiResponses(value ={
            @ApiResponse(code = 200, message = "1. 삭제 성공 \t\n 2. 삭제 실패(사용자 정보 없음) \t\n 3. 삭제 실패(절약 정보 없음) \t\n"),
    })
    @DeleteMapping  (value = "/products/{productId}")
    public ResponseEntity<DefaultRes> deleteProduct(@PathVariable("productId") Long productId, @RequestParam ("userId") Long userId){
        return new ResponseEntity<>(productService.deleteProduct(productId, userId), HttpStatus.OK);
    }

}
