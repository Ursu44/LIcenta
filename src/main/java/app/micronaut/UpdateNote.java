package app.micronaut;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.json.Json;
import com.google.firebase.database.*;
import jakarta.inject.Inject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class UpdateNote {

    @Inject
    private DB dbInitializer;

    private DatabaseReference materii = null;

    @Inject
    public UpdateNote() {
        materii = FirebaseDatabase.getInstance().getReference("Catalog");
    }

    public void updateCatalog(JsonNode json) throws JsonProcessingException {
        CountDownLatch latch = new CountDownLatch(1);
        System.out.println("Raspuns recieved "+ json);
        String mail = json.get("mail").textValue();
        System.out.println("Mail "+mail);
        String nume = json.get("nume").textValue();
        System.out.println("Nume "+nume);
        String prenume = json.get("prenume").textValue();
        System.out.println("Prenume "+prenume);
        String tip = json.get("tipNota").textValue();
        int nota = json.get("nota").asInt();
        String materie = json.get("materie").textValue();

        materii.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot studentSnapshot : dataSnapshot.getChildren()) {
                    DataSnapshot dataSnapshot1 = studentSnapshot;
                    String elevEmail = dataSnapshot1.child("mailElev").getValue(String.class);
                    String elevNume = dataSnapshot1.child("numeElev").getValue(String.class);
                    String elevPrenume= dataSnapshot1.child("prenumenumeElev").getValue(String.class);
                    if (elevEmail.equals(mail) && elevNume.equals(nume) && elevPrenume.equals(prenume)) {
                        Map<String, Object> data = new HashMap<String, Object>();
                        data.put(tip, nota);
                        studentSnapshot.child(materie).getRef().updateChildren(data, null);
                        break;
                    }
                }
                latch.countDown();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.out.println("Eroare la extragerea cursurilor: " + error.getMessage());
                latch.countDown();
            }
        });

        try {
            latch.await(2, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
