package yapp.bestFriend.model.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
public class Product extends BaseTime {

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

    private String resolution;

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", userId='" + user.getId() + '\'' +
                ", name='" + name + '\'' +
                ", price='" + price + '\'' +
                ", resolution='" + resolution + '\'' +
                super.toString() +
                '}';
    }

    @Builder
    public Product(User user, String name, String price, String resolution){
        this.user = user;
        this.name = name;
        this.price = price;
        this.resolution = resolution;
    }

}
