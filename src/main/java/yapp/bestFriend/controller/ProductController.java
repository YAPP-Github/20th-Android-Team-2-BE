package yapp.bestFriend.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import yapp.bestFriend.model.dto.DefaultRes;
import yapp.bestFriend.model.dto.request.CreateProductRequest;
import yapp.bestFriend.service.ProductService;
import javax.validation.Valid;

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
}
