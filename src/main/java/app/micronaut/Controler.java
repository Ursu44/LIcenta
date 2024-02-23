package app.micronaut;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.annotation.Put;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import jakarta.inject.Inject;

import java.security.Principal;

@Secured(SecurityRule.IS_AUTHENTICATED)
@Controller
public class Controler {
    @Inject
    private DBInitializer dbInitializer;

    private DatabaseReference materii = null;

    private UpdateCurs updateCurs;

    @Inject
    public Controler(UpdateCurs updateCurs) {
        materii = FirebaseDatabase.getInstance().getReference("Materii");
        this.updateCurs = updateCurs;
    }

    @Produces(MediaType.TEXT_PLAIN)
    @Put
    public void index(Principal principal) {
        String response = principal.getName();
        String gmail = response.split("_")[1];
        updateCurs.update(gmail);
    }
}
