package in.gov.abdm.nmr.db.sql.domain.sub_district;

import java.math.BigInteger;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import in.gov.abdm.nmr.db.sql.domain.city.City;
import in.gov.abdm.nmr.db.sql.domain.district.District;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class SubDistrict {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private BigInteger id;
    private String name;
    private String code;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "district", referencedColumnName = "id")
    private District district;

    @OneToMany(mappedBy = "subdistrict", fetch = FetchType.LAZY)
    private List<City> cities;
}
