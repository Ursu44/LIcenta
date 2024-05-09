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
        String materie = json.get("materie").textValue();
        System.out.println("Materie "+materie);
        String tip = json.get("tipNota").textValue();
        System.out.println("Tip "+tip);
        String an = json.get("an").textValue();
        System.out.println("An "+an);
        String grupa = json.get("grupa").textValue();
        System.out.println("Grupa "+grupa);
        int nota = json.get("nota").asInt();
        System.out.println("Nota "+nota);
        String materie1 = "";
        if (materie.equals("Fizica")) {
            materie1 = "Fizică";
        }
        if (materie.equals("Matematica")) {
            materie1 =  "Matematică";
        }
        if (materie.equals("Limbasiliteraturaromana")) {
            materie1 =  "Limba si literatura romana";
        }
        if (materie.equals("Limbaengleza")) {
            materie1 =  "Limba engleza";
        }
        String finalMaterie = materie;
        System.out.println("Materie de adaugat "+finalMaterie);
        materii.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot studentSnapshot : dataSnapshot.getChildren()) {
                    DataSnapshot dataSnapshot1 = studentSnapshot;
                    String elevEmail = dataSnapshot1.child("mailElev").getValue(String.class);
                    String elevNume = dataSnapshot1.child("nume").getValue(String.class);
                    String elevPrenume= dataSnapshot1.child("prenume").getValue(String.class);
                    String grupaElev = dataSnapshot1.child("grupa").getValue(String.class);
                    String anElev = dataSnapshot1.child("an").getValue(String.class);
                    elevNume = elevNume+" "+elevPrenume;
                    System.out.println(grupaElev);
                    if (elevEmail.equals(mail) && elevNume.equals(nume) && grupaElev.equals(grupa) && anElev.equals(an))   {
                        System.out.println("Pe aici nu se trece ");
                        Map<String, Object> data = new HashMap<String, Object>();
                        data.put(tip, nota);
                        studentSnapshot.child(finalMaterie).getRef().updateChildren(data, null);
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
