package app.micronaut;

import com.google.firebase.database.*;
import jakarta.inject.Inject;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class RetriveGrades {

    @Inject
    private DBCreate dbInitializer;

    private DatabaseReference note = null;

    @Inject
    public RetriveGrades() {
        note = FirebaseDatabase.getInstance().getReference("Catalog");
    }

    public JSONObject ExtractGrades(String gmail) {
        CountDownLatch latch = new CountDownLatch(1);
        JSONObject raspuns = new JSONObject();

        note.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int nr=0;
                for (DataSnapshot studentSnapshot : dataSnapshot.getChildren()) {
                    String mail1 = studentSnapshot.child("mailElev").getValue(String.class);
                    if (studentSnapshot.exists() && mail1!=null) {
                        nr++;
                        String mail = studentSnapshot.child("mailElev").getValue(String.class);
                        System.out.println(mail);
                        String nume = studentSnapshot.child("numeElev").getValue(String.class);
                        System.out.println(nume);
                        String prenume = studentSnapshot.child("prenumenumeElev").getValue(String.class);
                        System.out.println(prenume);
                        HashMap<String, Object> data = new HashMap<String, Object>();
                        HashMap<String, Object> data1 = new HashMap<String, Object>();
                        data.put("mail", mail);
                        data.put("nume", nume);
                        data.put("prenume", prenume);
                        System.out.println("data " + data);
                        DataSnapshot materieSnapshot = studentSnapshot.child("Fizica");
                        if (materieSnapshot.exists()) {
                            Integer nota1 = materieSnapshot.child("Nota1").getValue(Integer.class);
                            data1.put("Nota1", nota1);
                            Integer nota2 = materieSnapshot.child("Nota2").getValue(Integer.class);
                            data1.put("Nota2", nota2);
                            Integer nota3 = materieSnapshot.child("Nota3").getValue(Integer.class);
                            data1.put("Nota3", nota3);
                            Integer nota4 = materieSnapshot.child("Nota4").getValue(Integer.class);
                            data1.put("Nota4", nota4);
                            Integer medie = materieSnapshot.child("medie").getValue(Integer.class);
                            data1.put("Medie", medie);
                            //data.put("Fizica", data1);
                        }

                        data.put("Fizica", data1);
                        System.out.println("Elev " + nr);
                        raspuns.put("elev" + nr, data);
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
}
