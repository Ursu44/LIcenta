package example.micronaut;

import io.micronaut.core.annotation.Nullable;
import io.micronaut.http.HttpRequest;
import io.micronaut.security.authentication.AuthenticationProvider;
import io.micronaut.security.authentication.AuthenticationRequest;
import io.micronaut.security.authentication.AuthenticationResponse;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

import java.security.NoSuchAlgorithmException;
import java.util.*;

@Singleton
public class AuthetificationProvider implements AuthenticationProvider<HttpRequest<?>> {

    private IdentityVerification identityVerification;
    private Hashing hashing;

    @Inject
    public AuthetificationProvider(IdentityVerification identityVerification, Hashing hashing){
        this.identityVerification = identityVerification;
        this.hashing = hashing;
    }

    @Override
    public Publisher<AuthenticationResponse> authenticate(@Nullable HttpRequest<?> httpRequest, AuthenticationRequest<?, ?> authenticationRequest) {
        String mail = (String) authenticationRequest.getIdentity();
        String password = (String) authenticationRequest.getSecret();
        String hasshedPassword;
        try {
            hasshedPassword = hashing.toHexString(hashing.getSHA(password));
        } catch (NoSuchAlgorithmException e) {
            return Flux.error(e);
        }
        boolean confirm = identityVerification.ConfirmIdentity(mail, hasshedPassword);
        return Flux.create(emitter -> {
            if (identityVerification.ConfirmIdentity(mail, hasshedPassword)) {
                String username = identityVerification.ConfirmationIdentittyCheck();
                if(username != "") {
                    System.out.println("da5");
                    Collection<String> roles = Arrays.asList("ROLE_STUDENT");
                    username =username+"_"+mail ;
                    emitter.next(AuthenticationResponse.success(username, roles));
                    emitter.complete();
                }
                else{
                    System.out.println("Confirma identitatea");
                    emitter.error(AuthenticationResponse.exception());
                }
            } else {
                System.out.println("Email sau parola gresite");
                emitter.error(AuthenticationResponse.exception());
            }
        }, FluxSink.OverflowStrategy.ERROR);
    }

}