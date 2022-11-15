package in.gov.abdm.nmr.domain.address;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import in.gov.abdm.nmr.domain.address_type.AddressType;
import in.gov.abdm.nmr.domain.common.AuditEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Address extends AuditEntity {

    @Id
    private Long id;
    private String country;
    private String state;
    private String district;
    private String city;
    private String subDistrict;
    private String pincode;
    private String addressLine1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_type")
    private AddressType addressType;
}
