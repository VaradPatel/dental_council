package in.gov.abdm.nmr.api.ext.user_detail;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class UserDetailService implements IUserDetailService {

    private static final Logger LOGGER = LogManager.getLogger();

    private static final String URI_PREFIX = "/userDetails";

    private WebClient dbWebClient;

    public UserDetailService(WebClient webClient) {
        this.dbWebClient = webClient;
    }

    @Override
    public UserDetailTO findByUsername(String username) {
        LOGGER.info("Fetching user details from DB");
        return dbWebClient.method(HttpMethod.GET).uri(URI_PREFIX + "/queryBy/username").bodyValue(username).retrieve().bodyToMono(UserDetailTO.class).blockOptional() //
                .orElse(new UserDetailTO());
    }

    @Override
    public String findRefreshTokenIdByUsername(String username) {
        LOGGER.info("Fetching refresh token id from DB");
        return dbWebClient.method(HttpMethod.GET).uri(URI_PREFIX + "/refreshTokenId").bodyValue(username).retrieve().bodyToMono(String.class).blockOptional().orElse("");
    }

    @Override
    public Integer updateRefreshTokenId(String username, String refreshTokenId) {
        LOGGER.info("Updating refresh token id in DB");
        return dbWebClient.put().uri(URI_PREFIX + "/refreshTokenId").bodyValue(new UpdateRefreshTokenIdRequestTO(username, refreshTokenId)).retrieve() //
                .bodyToMono(Integer.class).blockOptional().orElse(0);
    }
}
