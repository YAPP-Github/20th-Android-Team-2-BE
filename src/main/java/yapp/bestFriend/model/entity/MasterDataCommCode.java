package yapp.bestFriend.model.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class MasterDataCommCode extends BaseInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String typeCode;

    private String commCode;

    @Builder
    public MasterDataCommCode(String typeCode, String commCode) {
        this.typeCode = typeCode;
        this.commCode = commCode;
    }
}