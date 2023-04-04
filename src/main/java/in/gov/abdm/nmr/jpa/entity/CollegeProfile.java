package in.gov.abdm.nmr.jpa.entity;

import java.math.BigInteger;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "collegeProfile")
public class CollegeProfile extends CommonAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private BigInteger id;
    private String name;
    
    @OneToOne
    @JoinColumn(name = "userId", referencedColumnName = "id")
    private User user;
    
    @ManyToOne
    @JoinColumn(name = "collegeId", referencedColumnName = "id")
    private CollegeMaster college;
}
