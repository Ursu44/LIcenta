package app.micronaut;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.annotation.Put;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import jakarta.inject.Inject;

import java.security.Principal;

@Secured(SecurityRule.IS_AUTHENTICATED)
@Controller("/update")
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
    @Put("/progres")
    public void index(Principal principal) {
        String response = principal.getName();
        String gmail = response.split("_")[1];
        updateCurs.update(gmail);
    }

    @Produces(MediaType.TEXT_PLAIN)
    @Put("/timp")
    public void index1(Principal principal,  @Body String json) throws JsonProcessingException {
        String response = principal.getName();
        String gmail = response.split("_")[1];
        //System.out.println("Mail "+gmail+" "+json);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode raspunsJson = objectMapper.readTree(json);
        String an = raspunsJson.get("materie").asText().split("_")[1];
        String materie = raspunsJson.get("materie").asText().split("_")[0];
        String ora =  raspunsJson.get("ora").asText();
        int incercari =  raspunsJson.get("incercari").asInt();

        /*System.out.println("An "+an);
        System.out.println("Materie "+materie);
        System.out.println("Ora "+ora);
        System.out.println("Incercari "+incercari);*/

        updateCurs.updateTest(gmail, an, materie, ora, incercari);
    }
}
