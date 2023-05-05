package in.gov.abdm.nmr.service.impl;

import in.gov.abdm.nmr.dto.LanguageTO;
import in.gov.abdm.nmr.mapper.LanguageDtoMapper;
import in.gov.abdm.nmr.repository.LanguageRepository;
import in.gov.abdm.nmr.service.ILanguageService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class LanguageServiceImpl implements ILanguageService {

    private LanguageRepository languageRepository;

    public LanguageServiceImpl(LanguageRepository languageRepository) {
        this.languageRepository = languageRepository;
    }

    @Override
    public List<LanguageTO> getLanguageData() {
        return LanguageDtoMapper.LANGUAGE_DTO_MAPPER.languageDataToDto(languageRepository.getLanguage());

    }
}
