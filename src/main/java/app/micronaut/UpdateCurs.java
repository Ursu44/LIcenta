package app.micronaut;

import com.google.firebase.database.*;
import jakarta.inject.Inject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class UpdateCurs {

    @Inject
    private DBInitializer dbInitializer;

    private DatabaseReference materii = null;

    @Inject
    public UpdateCurs() {
        materii = FirebaseDatabase.getInstance().getReference("Materii");
    }

    public void update(String mail){
        CountDownLatch latch = new CountDownLatch(1);

        materii.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot studentSnapshot : dataSnapshot.getChildren()) {
                    String extractedMail = studentSnapshot.child("mail").getValue(String.class);
                    extractedMail = extractedMail.replace(',', '.');
                    System.out.println("Mail de cautat "+mail);
                    if (extractedMail.equals(mail)) {
                        System.out.println("Gmail " + extractedMail);
                            DataSnapshot lectieSnapshot = studentSnapshot.child("Fizica");
                            int progress = lectieSnapshot.child("progres").getValue(Integer.class);
                            progress+=10;
                            HashMap<String, Object>  data = new HashMap<>();
                            data.put("progres", progress);
                            lectieSnapshot.getRef().updateChildren(data, null);
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

    public void updateTest(String mail, String an, String materie, String ora,int incercari){
        CountDownLatch latch = new CountDownLatch(1);
        materii.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot studentSnapshot : dataSnapshot.getChildren()) {
                    //System.out.println("Mail de cautat "+studentSnapshot.child(materie).child("mailProfesor"));
                    String extractedMail = studentSnapshot.child(materie).child("mailProfesor").getValue(String.class);
                    String extractedAn = studentSnapshot.child("an").getValue(String.class);
                    if (!extractedMail.equals("") && extractedMail.equals(mail) && extractedAn.equals(an)) {
                        extractedMail = extractedMail.replace(',', '.');
                        System.out.println("Am intrat" + extractedMail);
                        Map<String, Object> data = new HashMap<>();
                        data.put("durataTest", ora);
                        data.put("incercari", incercari);
                        studentSnapshot.child(materie).getRef().updateChildren(data, null);
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

