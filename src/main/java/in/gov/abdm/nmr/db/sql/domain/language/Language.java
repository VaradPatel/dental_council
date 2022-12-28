package in.gov.abdm.nmr.db.sql.domain.language;

import java.math.BigInteger;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "language")
public class Language {
	
	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private BigInteger id;
	    private String name;

}
