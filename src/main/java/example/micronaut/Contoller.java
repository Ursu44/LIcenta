package example.micronaut;

import io.micronaut.http.HttpHeaders;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.cookie.Cookies;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import io.micronaut.security.utils.SecurityService;
import jakarta.inject.Inject;

import java.security.Principal;


@Secured(SecurityRule.IS_AUTHENTICATED)
@Controller("/controller")
public class Contoller {

    @Inject
    private SecurityService securityService;

    @Produces(MediaType.TEXT_PLAIN)
    @Get("/access")
    public String index(Principal principal, HttpHeaders headers, Cookies cookies) {
        System.out.println("Hello, " + principal.getName() + "! You have access to the secured endpoint.");

        return "Hello, " + principal.getName() + "! You have access to the secured endpoint.";
    }
}
