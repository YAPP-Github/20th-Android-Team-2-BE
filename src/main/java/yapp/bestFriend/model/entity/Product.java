package yapp.bestFriend.model.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import yapp.bestFriend.model.dto.request.UpdateProductRequest;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
public class Product extends BaseInfo {

    @Id //pk
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "product_name")
    private String name;

    private String price;//version 1에서만 사용

    //version2 추가 시작
    private String startYmd;

    private String endYmd;

    private String freqType;

    private String freqInterval;
    //version2 추가 종료

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", userId='" + user.getId() + '\'' +
                ", name='" + name + '\'' +
                ", price='" + price + '\'' +
                super.toString() +
                '}';
    }

    @Builder
    public Product(User user, String name, String price, String startYmd, String endYmd, String freqType, String freqInterval) {
        this.user = user;
        this.name = name;
        this.price = price;
        this.startYmd = startYmd;
        this.endYmd = endYmd;
        this.freqType = freqType;
        this.freqInterval = freqInterval;
    }

    public Product updateBoard(UpdateProductRequest request) {
        this.name = request.getName();
        this.price = request.getPrice();

        return this;
    }

    public Product updateBoard(yapp.bestFriend.model.dto.request.v1pt1.product.UpdateProductRequest request) {
        this.name = request.getName();
        this.price = request.getFreqType();
        this.freqInterval = request.getFreqInterval();
        this.startYmd = request.getStartYmd();
        this.endYmd = request.getEndYmd();

        return this;
    }
}
