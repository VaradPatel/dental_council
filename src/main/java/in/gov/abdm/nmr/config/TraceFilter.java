package in.gov.abdm.nmr.config;

import brave.Tracer;
import in.gov.abdm.nmr.common.CustomHeaders;
import in.gov.abdm.nmr.security.ChecksumUtil;
import in.gov.abdm.nmr.util.NMRConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingResponseWrapper;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Order(Ordered.LOWEST_PRECEDENCE)
@Component
public class TraceFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = LogManager.getLogger();

    private Tracer tracer;

    public TraceFilter(Tracer tracer) {
        this.tracer = tracer;
    }

    @Autowired
    ChecksumUtil checksumUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        LOGGER.info("Adding trace-id to response");
        response.addHeader(CustomHeaders.CORRELATION_ID, tracer.currentSpan().context().traceIdString());

        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);
        filterChain.doFilter(request, responseWrapper);

        byte[] responseBody = responseWrapper.getContentAsByteArray();

        String responseString= new String(responseBody, StandardCharsets.UTF_8);
        if(!responseString.equals("")) {
            response.addHeader(NMRConstants.CHECKSUM_HEADER, checksumUtil.generateChecksum(responseString));
        }
        responseWrapper.copyBodyToResponse();
    }
}
