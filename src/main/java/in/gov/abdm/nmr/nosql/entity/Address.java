package in.gov.abdm.nmr.nosql.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Address {

    private String type;
    @Field(name = "full_name")
    private String fullName;
    @Field(name = "address_line1")
    private String addressLine1;
    private String country;
    private String state;
    private String district;
    @Field(name = "sub_districts")
    private String subDistricts;
    private String city;
    private String pincode;
    @Field(name = "created_date")
    private String createdDate;
    @Field(name = "updated_date")
    private String updatedDate;
}
