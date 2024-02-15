package example.micronaut;

import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.errors.OauthErrorResponseException;
import io.micronaut.security.token.event.RefreshTokenGeneratedEvent;
import io.micronaut.security.token.refresh.RefreshTokenPersistence;
import jakarta.inject.Singleton;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static io.micronaut.security.errors.IssuingAnAccessTokenErrorCode.INVALID_GRANT;

@Singleton
public class RefeshToken implements RefreshTokenPersistence {

    private final ResfreshTokenRepository refreshTokenRepository;

    public RefeshToken(ResfreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Override
    public void persistToken(RefreshTokenGeneratedEvent event) {
        if (event != null &&
                event.getRefreshToken() != null &&
                event.getAuthentication() != null &&
                event.getAuthentication().getName() != null) {
            String payload = event.getRefreshToken();
            String regex = "\\[(.*?)\\]";
            String roles = String.valueOf(event.getAuthentication().getRoles());
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(roles);
            String role = "";
            if(matcher.find()) {
                 role = matcher.group(1);
            }
            else{

            }

            System.out.println("Refresh token in DB "+ payload);
            try {
                refreshTokenRepository.save(event.getAuthentication().getName(), false, payload, role);
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
            System.out.println("da");
        }
    }


    @Override
    public Publisher<Authentication> getAuthentication(String refreshToken) {
        System.out.println("Refrsh token primit "+ refreshToken);
        System.out.println("da3");
        return Flux.create(emitter -> {
            Optional<RefreshTokenEntity> tokenOpt = refreshTokenRepository.findByRefreshToken(refreshToken);
            System.out.println("token cautat "+tokenOpt);
            if (tokenOpt.isPresent()) {
                System.out.println("da10");
                RefreshTokenEntity token = tokenOpt.get();
                if (token.getRevoked()) {
                    emitter.error(new OauthErrorResponseException(INVALID_GRANT, "refresh token revoked", null));
                    System.out.println("da8");
                } else {
                    System.out.println("da6");
                    String role = token.getRole();
                    Collection<String> roles = Arrays.asList(role);
                    emitter.next(Authentication.build(token.getUsername(), roles));
                    emitter.complete();
                }
            } else {
                System.out.println("da9");
                emitter.error(new OauthErrorResponseException(INVALID_GRANT, "refresh token not found", null));
            }
        }, FluxSink.OverflowStrategy.ERROR);
    }
}