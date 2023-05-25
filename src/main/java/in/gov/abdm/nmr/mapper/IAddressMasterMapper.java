package in.gov.abdm.nmr.mapper;

import in.gov.abdm.nmr.entity.Address;
import in.gov.abdm.nmr.entity.AddressMaster;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = ComponentModel.SPRING)
public interface IAddressMasterMapper {

	IAddressMasterMapper ADDRESS_MASTER_MAPPER = Mappers.getMapper(IAddressMasterMapper.class);
	AddressMaster addressToAddressMaster(Address address);

}
