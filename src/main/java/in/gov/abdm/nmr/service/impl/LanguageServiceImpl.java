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

    private LanguageDtoMapper languageDtoMapper;

    public LanguageServiceImpl(LanguageRepository languageRepository, LanguageDtoMapper languageDtoMapper) {
        this.languageRepository = languageRepository;
        this.languageDtoMapper = languageDtoMapper;
    }

    @Override
    public List<LanguageTO> getLanguageData() {
        return languageDtoMapper.languageDataToDto(languageRepository.getLanguage());

    }
}
