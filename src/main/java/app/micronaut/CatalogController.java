package app.micronaut;

import com.google.api.Authentication;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.annotation.QueryValue;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import jakarta.inject.Inject;
import org.json.JSONObject;

import java.security.Principal;


@Secured(SecurityRule.IS_AUTHENTICATED)
@Controller("/catalog")
public class CatalogController {

    @Inject
    private DBCreate dbInitializer;
    private DatabaseReference note = null;
    private RetriveGrades retriveGrades;

    @Inject
    public CatalogController(RetriveGrades retriveGrades) {
        note = FirebaseDatabase.getInstance().getReference("Catalog");
        this.retriveGrades = retriveGrades;
    }

    @Produces(MediaType.TEXT_PLAIN)
    @Get("/{tip}")
    public String index(Principal principal, @QueryValue String tip) {
        String response = principal.getName();
        String gmail = response.split("_")[1];

        JSONObject raspuns = retriveGrades.ExtractGrades(gmail, tip);
       // System.out.println("RASAPUSN "+raspuns.toString());
        return raspuns.toString();
    }
}
