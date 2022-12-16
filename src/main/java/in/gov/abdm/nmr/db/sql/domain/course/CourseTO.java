package in.gov.abdm.nmr.db.sql.domain.course;

import java.math.BigInteger;
import java.util.List;

import in.gov.abdm.nmr.db.sql.domain.district.District;
import lombok.Data;

@Data
public class CourseTO {

    private BigInteger id;
    private String name;
}
