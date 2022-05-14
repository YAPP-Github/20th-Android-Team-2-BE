package yapp.bestFriend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import yapp.bestFriend.model.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}
