package in.gov.abdm.nmr.dto;
import lombok.Data;
import java.math.BigInteger;
import java.sql.Timestamp;

@Data
public class Template {

    /**
     * ID has a primary key constraint, which restricts duplicate values.
     * A unique TemplateId stored in ID field.
     */
    private BigInteger id;

    /**
     * name field stores the module name that uses this template.
     */
    private String name;

    /**
     * Message Template is stored in message field.
     */
    private String message;

    /**
     * Header Information of Template is stored in header field.
     */
    private String header;

    /**
     * type stores type of the transaction (EMAIL, SMS).
     */
    private String type;

    /**
     * subType stores the category of the content which can be used to prioritize the requests
     * (OTP, INFO, PROMO).
     */
    private String subType;

    /**
     * the version of the template
     */
    private Float version;

    /**
     * indicates whether the current template is active/inactive
     */
    private boolean active;

    /**
     * created is a Timestamp, which stores record created date and time.
     */
    private Timestamp created;

    /**
     * modified is a Timestamp, which stores record modified date and time.
     */
    private Timestamp modified;

    /**
     * isNewTemplate of boolean type that stores the state of an entity object.
     */
    private boolean isNew;
}
