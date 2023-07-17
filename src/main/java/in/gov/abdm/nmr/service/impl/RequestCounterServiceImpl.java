package in.gov.abdm.nmr.service.impl;

import in.gov.abdm.nmr.entity.RequestCounter;
import in.gov.abdm.nmr.exception.NMRError;
import in.gov.abdm.nmr.exception.WorkFlowException;
import in.gov.abdm.nmr.repository.IRequestCounterRepository;
import in.gov.abdm.nmr.service.IRequestCounterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.Optional;

@Service
public class RequestCounterServiceImpl implements IRequestCounterService {
    @Autowired
    IRequestCounterRepository requestCounterRepository;

    @Override
    public RequestCounter incrementAndRetrieveCount(BigInteger applicationTypeId) throws WorkFlowException {
        Optional<RequestCounter> requestCounter = requestCounterRepository.findById(applicationTypeId);
        if(requestCounter.isPresent()) {
            BigInteger counter = requestCounter.get().getCounter().add(BigInteger.ONE);
            requestCounter.get().setCounter(counter);
            return requestCounter.get();
        }
        throw new WorkFlowException(NMRError.INVALID_ID_EXCEPTION.getCode(), NMRError.INVALID_ID_EXCEPTION.getMessage());
    }
}
