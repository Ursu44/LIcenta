package example.micronaut;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.ArrayNode;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.inject.Inject;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;


@Controller("/corectare")
public class ControllerCorectare {

    @Inject
    private CorectareFirebase dbInitializer;
    private VerifyAnswears verifyAnswears;

    public ControllerCorectare(VerifyAnswears verifyAnswears){
        this.verifyAnswears = verifyAnswears;
    }

    @Post("/")
    public String takeAnswears(@Body String json) throws JsonProcessingException {
        System.out.println("Raspuns primit "+json);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode raspuns = objectMapper.readTree(json);
        JsonNode date = raspuns.get("intrebariTrimis");
        JsonNode intrebari = raspuns.get("dateTrimis");
        System.out.println("Date elev "+date);
        String notaFinal = verifyAnswears.corecteaza(date, intrebari);
        System.out.println("Rezultatul calculat si final "+notaFinal);
        verifyAnswears.salvezNota(date, notaFinal);
        return notaFinal;
    }

    @Post("/grafic")
    public String takeNote(@Body String json) throws JsonProcessingException, ExecutionException, InterruptedException {
        System.out.println("Raspuns primit "+json);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode date = objectMapper.readTree(json);
        String result = verifyAnswears.prreiauGrafic(date);

        System.out.println("Rezultat primit: " + result);
        return result;
    }
}

