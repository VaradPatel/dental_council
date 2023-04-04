package in.gov.abdm.nmr.mapper;

import in.gov.abdm.nmr.dto.AddressTO;
import in.gov.abdm.nmr.jpa.entity.Address;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;

@Mapper(componentModel = ComponentModel.SPRING)
public interface IAddressMapper {

	AddressTO addressToAddressTo(Address address);

}
