package in.gov.abdm.nmr.mapper;
import in.gov.abdm.nmr.jpa.entity.LanguagesKnown;
import in.gov.abdm.nmr.jpa.entity.LanguagesKnownMaster;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;

import java.util.List;

@Mapper(componentModel = ComponentModel.SPRING)
public interface ILanguagesKnownMasterMapper {

	List<LanguagesKnownMaster> languagesKnownToLanguagesKnownMaster(List<LanguagesKnown> languagesKnown);

}
