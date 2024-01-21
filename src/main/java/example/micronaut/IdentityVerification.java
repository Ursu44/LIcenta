package example.micronaut;

import com.google.firebase.database.*;
import jakarta.inject.Inject;

import java.util.concurrent.CountDownLatch;

public class IdentityVerification {
    @Inject
    private DBInitializer dbInitializer;
    private DatabaseReference studenti = null;
    private DatabaseReference profesori = null;

    @Inject
    public IdentityVerification(Hashing hashing) {
        studenti = FirebaseDatabase.getInstance().getReference("Studenti");
        profesori = FirebaseDatabase.getInstance().getReference("Profesori");
    }

    public boolean ConfirmIdentity(String mail,String pass){
        CountDownLatch latch = new CountDownLatch(1);
        boolean[] rezultat = {false};

        studenti.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String mailValue = snapshot.child("mail").getValue(String.class);
                    String passValue = snapshot.child("parola").getValue(String.class);

                    if(mailValue != null && mailValue.equals(mail) && passValue !=null && passValue.equals(pass)){
                            rezultat[0] = true;
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
            latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        return  rezultat[0];
    }

    public String ConfirmationIdentittyCheck(){
        String[] username = {""};
        CountDownLatch latch = new CountDownLatch(1);

        studenti.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                     String verificat =  snapshot.child("verificat").getValue(String.class);
                     if(verificat.equals("da")){
                         username[0] = snapshot.child("prenume").getValue(String.class);
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
            latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        return username[0];
    }
}