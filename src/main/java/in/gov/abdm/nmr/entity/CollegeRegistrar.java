package in.gov.abdm.nmr.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigInteger;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "collegeRegistrar")
public class CollegeRegistrar extends CommonAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private BigInteger id;
    private String name;
    private String phoneNumber;
    private String emailId;

    @OneToOne
    @JoinColumn(name = "college_id")
    private CollegeMaster college;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}
