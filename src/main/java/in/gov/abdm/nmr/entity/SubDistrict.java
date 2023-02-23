package in.gov.abdm.nmr.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigInteger;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class SubDistrict extends CommonAuditEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private BigInteger id;
    private String name;
    private String isoCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "districtCode", referencedColumnName = "id")
    private District districtCode;
}
