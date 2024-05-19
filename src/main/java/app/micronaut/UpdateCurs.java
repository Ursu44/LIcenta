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

    public void update(String mail,String materie, String an, String capitol, String progres){
        CountDownLatch latch = new CountDownLatch(1);

        System.out.println("Mail "+mail);
        System.out.println("An "+an);
        System.out.println("Materie "+materie);
        System.out.println("Capitol "+capitol);
        int progresInt = Integer.parseInt(progres);;
        System.out.println("Progres  "+progresInt);

        materii.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot studentSnapshot : dataSnapshot.getChildren()) {
                    String extractedMail = studentSnapshot.child("mailElev").getValue(String.class);
                    extractedMail = extractedMail.replace(',', '.');
                    System.out.println("Mail de cautat "+mail);
                    if (!extractedMail.equals("") && extractedMail.equals(mail)) {
                        System.out.println("Gmail " + extractedMail);
                        HashMap<String, Object>  data = new HashMap<>();
                        data.put("progres", progresInt);
                        studentSnapshot.child(an).child(capitol).getRef().updateChildren(data, null);
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

    public void updateTest(String mail, String an, String materie, String ora, String ora1,int incercari, int incercari1){
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
                        data.put("durataTest1", ora1);
                        data.put("incercari", incercari);
                        data.put("incercari1", incercari1);
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

