package in.gov.abdm.nmr.config;

import brave.Tracer;
import in.gov.abdm.nmr.common.CustomHeaders;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Order(Ordered.HIGHEST_PRECEDENCE)
@Component
public class TraceFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = LogManager.getLogger();

    private Tracer tracer;

    public TraceFilter(Tracer tracer) {
        this.tracer = tracer;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        LOGGER.info("Adding trace-id to response");
        LOGGER.info("Request size {}", request.getContentLength());
        response.addHeader(CustomHeaders.CORRELATION_ID, tracer.currentSpan().context().traceIdString());
        super.doFilter(request, response, filterChain);
    }
}
