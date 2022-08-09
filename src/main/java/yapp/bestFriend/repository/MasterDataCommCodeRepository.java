package yapp.bestFriend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import yapp.bestFriend.model.entity.MasterDataCommCode;

import java.util.List;

@Repository
public interface MasterDataCommCodeRepository extends JpaRepository<MasterDataCommCode, Long> {

    List<MasterDataCommCode> findByTypeCode(String typeCode);
}