package in.gov.abdm.nmr.entity;

import java.math.BigInteger;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class UniversityMaster extends CommonAuditEntity {
    
    @Id
    @GenericGenerator(name="incrementGenerator" , strategy="increment")
    @GeneratedValue(generator = "incrementGenerator")
    private BigInteger id;
    private String name;
    private BigInteger status;
    private BigInteger visibleStatus;
    private BigInteger collegeId;
}
