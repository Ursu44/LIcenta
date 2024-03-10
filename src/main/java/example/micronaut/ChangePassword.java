package example.micronaut;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.firebase.database.*;
import jakarta.inject.Inject;

import javax.mail.MessagingException;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class ChangePassword {

    @Inject
    private Initializer dbInitializer;

    private DatabaseReference studenti = null;

    private DatabaseReference profesori = null;


    private String mail;


    @Inject
    public ChangePassword() {
        studenti = FirebaseDatabase.getInstance().getReference("Studenti");
        profesori = FirebaseDatabase.getInstance().getReference("Profesori");
    }

    public void Change(String mail,String pass) throws MessagingException, GeneralSecurityException, IOException {
        CountDownLatch latch = new CountDownLatch(2);

        studenti.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String mailValue = snapshot.child("mail").getValue(String.class);

                    if(mailValue != null && mailValue.equals(mail)){
                        Map<String, Object> data = new HashMap<>();
                        data.put("parola", pass);
                        snapshot.getRef().updateChildren(data, null);
                        break;
                    }
                }
                latch.countDown();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                latch.countDown();
            }
        });

        profesori.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String mailValue = snapshot.child("mail").getValue(String.class);

                    if(mailValue != null && mailValue.equals(mail)){
                        Map<String, Object> data = new HashMap<>();
                        data.put("parola", pass);
                        snapshot.getRef().updateChildren(data, null);
                        break;
                    }
                }
                latch.countDown();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                latch.countDown();
            }
        });

        try {
            latch.await(3, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }


}
