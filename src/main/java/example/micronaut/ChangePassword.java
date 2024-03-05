package example.micronaut;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import jakarta.inject.Inject;

import java.util.concurrent.CountDownLatch;

public class ChangePassword {

    @Inject
    private Initializer dbInitializer;

    private DatabaseReference studenti = null;

    private DatabaseReference profesori = null;

    @Inject
    public ChangePassword() {
        studenti = FirebaseDatabase.getInstance().getReference("Studenti");
        profesori = FirebaseDatabase.getInstance().getReference("Profesori");
    }

    public void Change(JsonNode json){
        CountDownLatch latch = new CountDownLatch(1);
        System.out.println("RASPUNS PRIMIT "+json);
        System.out.println("mail "+json.get("mail").asText());

    }
}
