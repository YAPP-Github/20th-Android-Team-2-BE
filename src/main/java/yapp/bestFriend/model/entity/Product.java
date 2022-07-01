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

    private String price;

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
    public Product(User user, String name, String price){
        this.user = user;
        this.name = name;
        this.price = price;
    }

    public Product updateBoard(UpdateProductRequest request) {
        this.name = request.getName();
        this.price = request.getPrice();

        return this;
    }
}
