package example.micronaut;

import com.google.firebase.database.*;
import jakarta.inject.Inject;
import org.json.JSONObject;

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
    public JSONObject ExtracLectures(String gmail) {
        CountDownLatch latch = new CountDownLatch(1);
        JSONObject raspuns = new JSONObject();

        materii.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot studentSnapshot : dataSnapshot.getChildren()) {
                        String mail = studentSnapshot.child("mail").getValue(String.class);
                        mail = mail.replace(',', '.');
                        if (mail.equals(gmail)) {
                            System.out.println("Gmail " + gmail);
                            DataSnapshot lectieSnapshot = studentSnapshot.child("Lectia1");
                            String nume = lectieSnapshot.child("nume").getValue(String.class);
                            System.out.println("Nume "+nume);
                            int progress = lectieSnapshot.child("progres").getValue(Integer.class);
                            System.out.println("Nume "+progress);
                            raspuns.put("lectie", nume);
                            raspuns.put("Progres", progress);
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
