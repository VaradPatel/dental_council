package in.gov.abdm.nmr.mapper;

import in.gov.abdm.nmr.dto.LanguageTO;
import in.gov.abdm.nmr.entity.Language;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = ComponentModel.SPRING)
public interface LanguageDtoMapper {

    LanguageDtoMapper  LANGUAGE_DTO_MAPPER = Mappers.getMapper(LanguageDtoMapper.class);

    List<LanguageTO> languageDataToDto(List<Language> language);


}
