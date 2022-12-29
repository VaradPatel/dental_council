package in.gov.abdm.nmr.mapper;

import java.util.List;

import in.gov.abdm.nmr.entity.Language;
import in.gov.abdm.nmr.dto.LanguageTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;

@Mapper(componentModel = ComponentModel.SPRING)
public interface LanguageDtoMapper {

    List<LanguageTO> LanguageDataToDto(List<Language> language);


}
