package in.gov.abdm.nmr.entity;
import lombok.*;
import org.hibernate.annotations.Type;
import javax.persistence.*;
import java.math.BigInteger;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class UserAttachments extends CommonAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private BigInteger id;
    private BigInteger attachmentTypeId;
    private BigInteger userId;
    private String requestId;
    private String name;
    @Type(type="org.hibernate.type.BinaryType")
    private byte[] fileBytes;
    String filePath;
}
