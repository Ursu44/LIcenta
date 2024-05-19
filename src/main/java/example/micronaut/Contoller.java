package example.micronaut;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.database.*;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
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

    public String numeLectie;

    @Inject
    public Contoller(TakeLectures takeLectures) {
        materii = FirebaseDatabase.getInstance().getReference("Materii");
        this.takeLectures = takeLectures;
    }

    @Get("/{nume}/{tip}")
    @Produces(MediaType.TEXT_PLAIN)
    public String index(Principal principal, @QueryValue String nume, @QueryValue String tip) {
        String response = principal.getName();
        String gmail = response.split("_")[1];
        System.out.println("Afizsse numele materiei "+nume);
        JSONObject raspuns = takeLectures.ExtracLectures(gmail, nume, tip);
        String raspuns1 = raspuns.toString()+"r";
        System.out.println("Raspuns : " + raspuns);
        return  raspuns1;
    }

    @Put("/activare")
    @Produces(MediaType.TEXT_PLAIN)
    public void schimba(Principal principal, @Body String json) throws JsonProcessingException {
        System.out.println("Raspuns primit de la " +json);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode raspunsJson = objectMapper.readTree(json);
        numeLectie = raspunsJson.get("numeMaterie").asText();
        takeLectures.updateStare(raspunsJson, numeLectie, principal.getName());
        System.out.println("Nume "+numeLectie);
    }

    @Get("/{nume}")
    @Produces(MediaType.TEXT_PLAIN)
    public String iauAni(Principal principal, @QueryValue String nume) {
        String response = principal.getName();
        String gmail = response.split("_")[1];
        String an = takeLectures.takeYear(gmail, nume);
        System.out.println("An "+an);
        return  an;
    }

}
