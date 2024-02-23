package app.micronaut;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.json.Json;
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
@Controller
public class UpdateCatalogController {

    @Inject
    private DB dbInitializer;

    private DatabaseReference materii = null;

    private UpdateNote updateNote;

    @Inject
    public UpdateCatalogController(UpdateNote updateNote) {
        materii = FirebaseDatabase.getInstance().getReference("Catalog");
        this.updateNote = updateNote;
    }

    @Produces(MediaType.TEXT_PLAIN)
    @Put
    public void index(Principal principal, @Body String json) throws JsonProcessingException {
        String response = principal.getName();
        String gmail = response.split("_")[1];
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode raspunsJson = objectMapper.readTree(json);
        updateNote.updateCatalog(raspunsJson);
        System.out.println("Raspuns "+json);
    }
}
