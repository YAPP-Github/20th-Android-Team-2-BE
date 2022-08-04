package yapp.bestFriend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import yapp.bestFriend.model.entity.Product;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByUserIdAndDeletedYn(Long UserId, Boolean DeletedYn); // where name = ? and ranking = ?

    @Query(value =
            "select A from Product A\n" +
                    "where A.user.id = :userId\n" +
                    "  and :recordYmd between A.startYmd and A.endYmd\n" +
                    "  and (A.freqType in ('1','2') or\n" +
                    "       (A.freqType = '3' and A.freqInterval like ('%'||:freqInterval||'%')))\n" +
                    "  and A.deletedYn = false \n"
    )
    List<Product> findProductList(String recordYmd, Long userId, String freqInterval);
}