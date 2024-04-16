package example.micronaut;


import com.fasterxml.jackson.core.JsonProcessingException;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;

@Secured(SecurityRule.IS_AUTHENTICATED)
@Controller("/api")
public class ControllerMongo {

    private TakeQuestions takeQuestions;

    public ControllerMongo(TakeQuestions takeQuestions){
        this.takeQuestions = takeQuestions;
    }

    @Produces(MediaType.TEXT_PLAIN)
    @Post("/")
    public String takeTest(@Body String json) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode raspunsJson = objectMapper.readTree(json);

        return takeQuestions.take(raspunsJson);
    }
}


