package example.micronaut;

import io.micronaut.core.annotation.Nullable;
import io.micronaut.http.HttpRequest;
import io.micronaut.security.authentication.AuthenticationProvider;
import io.micronaut.security.authentication.AuthenticationRequest;
import io.micronaut.security.authentication.AuthenticationResponse;
import jakarta.inject.Singleton;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Singleton
public class AuthetificationProvider implements AuthenticationProvider<HttpRequest<?>> {

    @Override
    public Publisher<AuthenticationResponse> authenticate(@Nullable HttpRequest<?> httpRequest, AuthenticationRequest<?, ?> authenticationRequest) {
        return Flux.create(emitter -> {
            if (authenticationRequest.getIdentity().equals("sherlock1") &&
                    authenticationRequest.getSecret().equals("password1")) {
                System.out.println("da 1");
                //Collection<String> roles = Arrays.asList("ROLE_STUDENT");
                HashMap<String, Object> roles = new HashMap<>();
                List<String> roleList = new ArrayList<>();
                roleList.add("ROLE_STUDENT");
                roles.put("roles", roleList);

                emitter.next(AuthenticationResponse.success((String) authenticationRequest.getIdentity(), roleList));
                emitter.complete();
            } else {
                System.out.println("nu 1");
                emitter.error(AuthenticationResponse.exception());
            }
        }, FluxSink.OverflowStrategy.ERROR);
    }

}
