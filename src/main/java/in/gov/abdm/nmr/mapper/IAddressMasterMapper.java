package in.gov.abdm.nmr.mapper;

import in.gov.abdm.nmr.jpa.entity.Address;
import in.gov.abdm.nmr.jpa.entity.AddressMaster;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;

@Mapper(componentModel = ComponentModel.SPRING)
public interface IAddressMasterMapper {

	AddressMaster addressToAddressMaster(Address address);

}
