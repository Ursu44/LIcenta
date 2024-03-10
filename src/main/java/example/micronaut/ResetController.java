package example.micronaut;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import io.micronaut.http.annotation.*;
import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;

import javax.mail.MessagingException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;

@Controller("/change")
public class ResetController {

    @Inject
    private Initializer initializer;

    private DatabaseReference studenti = null;

    private DatabaseReference profesori = null;
    private ChangePassword changePassword;

    private SendMail sendMail;

    private String mail;

    private boolean match=false;

    private String codGenerat;

    private Hashing hashing;

    @Inject
    public ResetController(ChangePassword changePassword, SendMail sendMail, Hashing hashing) {
        studenti = FirebaseDatabase.getInstance().getReference("Studenti");
        profesori = FirebaseDatabase.getInstance().getReference("Profesori");
        this.changePassword = changePassword;
        this.sendMail = sendMail;
        this.hashing = hashing;
    }


    @Post("/")
    public void obtainCode(@Body String json) throws IOException, GeneralSecurityException, MessagingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode raspunsJson = objectMapper.readTree(json);
        System.out.println("Raspuns "+raspunsJson);
        mail = raspunsJson.get("mail").asText();
        sendMail.setMailTo(raspunsJson.get("mail").asText());
        codGenerat = sendMail.sending();
    }

    @Post("/pass")
    public void verifyCode(@Body String json) throws IOException, GeneralSecurityException, MessagingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode raspunsJson = objectMapper.readTree(json);
        System.out.println("Raspuns1 "+raspunsJson);
        String cod = raspunsJson.get("cod").asText();
        if(cod.equals(codGenerat)) {
            match = true;
        }
    }

    @Post("/pass/reset")
    public void restting(@Body String json) throws IOException, GeneralSecurityException, MessagingException {
        if(match) {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode raspunsJson = objectMapper.readTree(json);
            System.out.println("Raspuns2 "+raspunsJson);
            String parola = raspunsJson.get("parola").asText();
            System.out.println("Noua parola "+parola);
            String parolaHash =  hashing.toHexString(hashing.getSHA(parola));
            changePassword.Change(mail, parolaHash);
        }
    }


}
