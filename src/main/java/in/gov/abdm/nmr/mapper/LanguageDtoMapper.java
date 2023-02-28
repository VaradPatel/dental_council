package in.gov.abdm.nmr.mapper;

import in.gov.abdm.nmr.dto.LanguageTO;
import in.gov.abdm.nmr.entity.Language;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;

import java.util.List;

@Mapper(componentModel = ComponentModel.SPRING)
public interface LanguageDtoMapper {

    List<LanguageTO> LanguageDataToDto(List<Language> language);


}
