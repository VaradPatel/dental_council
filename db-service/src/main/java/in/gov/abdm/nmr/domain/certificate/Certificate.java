package in.gov.abdm.nmr.domain.certificate;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import in.gov.abdm.nmr.domain.hp_profile.HpProfile;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Certificate {

	@Id
    private Long id;
	
	@OneToOne
	@JoinColumn(name = "hpProfile")
    private HpProfile hpProfileId; 
	
	private String url;
	private String name;
}
