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
        System.out.println("Raspuns12 "+json);
        System.out.println("Raspuns recieved "+ json);
        System.out.println("Nota3 "+json.get("nota3").asInt());

        materii.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot studentSnapshot : dataSnapshot.getChildren()) {
                    System.out.println("Nota34  "+json.get("nota3").asInt());
                    studentSnapshot.child("mail").getValue(String.class);
                    String elevEmail = studentSnapshot.child("elev").getValue(String.class);
                    if (elevEmail.equals(json.get("elev").asText())) {
                        System.out.println("DA DA DA DA");
                        Map<String, Object> data = new HashMap<>();
                        data.put("nota_1", json.get("nota1").asInt());
                        data.put("nota_2", json.get("nota2").asInt());
                        data.put("nota_3", json.get("nota3").asInt());
                        data.put("medie", json.get("medie").asInt());
                        DatabaseReference elevRef = studentSnapshot.getRef();
                        DatabaseReference fizicaRef = elevRef.child("Fizica");
                        fizicaRef.updateChildren(data ,null);
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
