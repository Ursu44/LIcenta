package example.micronaut;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.firebase.database.*;
import jakarta.inject.Inject;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class TakeLectures {

    @Inject
    private InitializeDB dbInitializer;
    private DatabaseReference materii = null;

    @Inject
    public TakeLectures() {
        materii = FirebaseDatabase.getInstance().getReference("Materii");
    }
    public JSONObject ExtracLectures(String gmail, String nume,String tip) {
        CountDownLatch latch = new CountDownLatch(1);
        JSONObject raspuns = new JSONObject();
        JSONObject lectie1 = new JSONObject();


        materii.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot studentSnapshot : dataSnapshot.getChildren()) {
                        String mail = studentSnapshot.child("mailElev").getValue(String.class);
                        mail = mail.replace(',', '.');
                        if(tip.equals("elev")) {
                            if (mail.equals(gmail)) {
                                System.out.println("Mail " + mail);
                                DataSnapshot materieSnapshot = studentSnapshot.child(nume);
                                if (materieSnapshot.exists()) {
                                    DataSnapshot lectieSnapshot = materieSnapshot.child("Lectia1");
                                    if (lectieSnapshot.exists()) {
                                        JSONObject lectie = new JSONObject();
                                        lectie.put("Inforamtie1", lectieSnapshot.child("Inforamtie1").getValue(String.class));
                                        lectie.put("Inforamtie2", lectieSnapshot.child("Inforamtie2").getValue(String.class));
                                        lectie.put("Inforamtie3", lectieSnapshot.child("Inforamtie3").getValue(String.class));
                                        lectie.put("Inforamtie4", lectieSnapshot.child("Inforamtie4").getValue(String.class));
                                        lectie.put("activat", lectieSnapshot.child("activat").getValue(Boolean.class));
                                        lectie.put("progres", lectieSnapshot.child("progres").getValue(Integer.class));
                                        raspuns.put("Lectia1", lectie);
                                    }
                                    DataSnapshot lectieSnapshot1 = materieSnapshot.child("Lectia2");
                                    if (lectieSnapshot1.exists()) {
                                        JSONObject lectie = new JSONObject();
                                        lectie.put("Inforamtie1", lectieSnapshot1.child("Inforamtie1").getValue(String.class));
                                        lectie.put("Inforamtie2", lectieSnapshot1.child("Inforamtie2").getValue(String.class));
                                        lectie.put("Inforamtie3", lectieSnapshot1.child("Inforamtie3").getValue(String.class));
                                        lectie.put("Inforamtie4", lectieSnapshot1.child("Inforamtie4").getValue(String.class));
                                        lectie.put("Inforamtie5", lectieSnapshot1.child("Inforamtie5").getValue(String.class));
                                        lectie.put("activat", lectieSnapshot1.child("activat").getValue(Boolean.class));
                                        lectie.put("progres", lectieSnapshot1.child("progres").getValue(Integer.class));
                                        raspuns.put("Lectia2", lectie);
                                    }

                                    DataSnapshot lectieSnapshot2 = materieSnapshot.child("Lectia3");
                                    if (lectieSnapshot2.exists()) {
                                        JSONObject lectie = new JSONObject();
                                        lectie.put("Inforamtie1", lectieSnapshot2.child("Inforamtie1").getValue(String.class));
                                        lectie.put("Inforamtie2", lectieSnapshot2.child("Inforamtie2").getValue(String.class));
                                        lectie.put("Inforamtie3", lectieSnapshot2.child("Inforamtie3").getValue(String.class));
                                        lectie.put("Inforamtie4", lectieSnapshot2.child("Inforamtie4").getValue(String.class));
                                        lectie.put("Inforamtie5", lectieSnapshot2.child("Inforamtie5").getValue(String.class));
                                        lectie.put("activat", lectieSnapshot2.child("activat").getValue(Boolean.class));
                                        lectie.put("progres", lectieSnapshot2.child("progres").getValue(Integer.class));
                                        raspuns.put("Lectia3", lectie);
                                    }
                                }
                            }
                        }
                        else if(tip.equals("profesor")) {
                            DataSnapshot materieSnapshot = studentSnapshot.child(nume);
                            DataSnapshot lectieSnapshot = materieSnapshot.child("Lectia1");
                            if (lectieSnapshot.exists()) {
                                JSONObject lectie = new JSONObject();
                                lectie.put("activat", lectieSnapshot.child("activat").getValue(Boolean.class));
                                raspuns.put("Lectia1", lectie);
                            }
                            DataSnapshot lectieSnapshot1 = materieSnapshot.child("Lectia2");
                            if (lectieSnapshot1.exists()) {
                                JSONObject lectie = new JSONObject();;
                                lectie.put("activat", lectieSnapshot1.child("activat").getValue(Boolean.class));
                                raspuns.put("Lectia2", lectie);
                            }

                            DataSnapshot lectieSnapshot2 = materieSnapshot.child("Lectia3");
                            if (lectieSnapshot2.exists()) {
                                JSONObject lectie = new JSONObject();
                                lectie.put("activat", lectieSnapshot2.child("activat").getValue(Boolean.class));
                                raspuns.put("Lectia3", lectie);
                            }
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
        System.out.println("Raspuns "+raspuns);
        return raspuns;
    }

    public void updateStare(JsonNode json,String nume) throws JsonProcessingException {
        CountDownLatch latch = new CountDownLatch(1);
        String lectie = json.get("numeLectie").asText();
        Boolean stare = json.get("activat").asBoolean();
        materii.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot studentSnapshot : dataSnapshot.getChildren()) {
                    DataSnapshot materieSnapshot = studentSnapshot.child(nume);
                    if (materieSnapshot.exists()) {
                        HashMap<String, Object> data = new HashMap<String, Object>();
                        data.put("activat", stare);
                        DataSnapshot lectieeSnapshot = materieSnapshot.child(lectie);
                        System.out.println(lectieeSnapshot.getRef());
                        lectieeSnapshot.getRef().updateChildren(data, null);
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
