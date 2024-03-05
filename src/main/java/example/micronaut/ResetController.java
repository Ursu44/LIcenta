package example.micronaut;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Put;
import jakarta.inject.Inject;

@Controller("/change")
public class ResetController {

    @Inject
    private Initializer initializer;

    private DatabaseReference studenti = null;

    private DatabaseReference profesori = null;
    private ChangePassword changePassword;

    @Inject
    public ResetController(ChangePassword changePassword) {
        studenti = FirebaseDatabase.getInstance().getReference("Studenti");
        profesori = FirebaseDatabase.getInstance().getReference("Profesori");
        this.changePassword = changePassword;
    }

    @Put("/password/{identifier}")
    public void changePass(@PathVariable String identifier, @Body String json) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode raspunsJson = objectMapper.readTree(json);
        changePassword.Change(raspunsJson);

    }
}
