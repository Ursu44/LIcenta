package example.micronaut;

import com.google.firebase.database.*;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import jakarta.inject.Inject;
import org.json.JSONObject;
import java.security.Principal;

@Secured(SecurityRule.IS_AUTHENTICATED)
@Controller
public class Contoller {

    @Inject
    private InitializeDB dbInitializer;
    private DatabaseReference materii = null;

    private TakeLectures takeLectures;

    @Inject
    public Contoller(TakeLectures takeLectures) {
        materii = FirebaseDatabase.getInstance().getReference("Materii");
        this.takeLectures = takeLectures;
    }

    @Produces(MediaType.TEXT_PLAIN)
    @Get
    public String index(Principal principal) {
        String response = principal.getName();
        String gmail = response.split("_")[1];
        JSONObject raspuns = takeLectures.ExtracLectures(gmail);
        String raspuns1 = raspuns.toString()+"r";
        System.out.println("Raspuns : " + raspuns);
        return  raspuns1;
    }
}
