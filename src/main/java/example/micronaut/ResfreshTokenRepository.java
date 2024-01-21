package example.micronaut;

import com.google.firebase.database.*;
import jakarta.inject.Inject;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.*;

public class ResfreshTokenRepository implements IRefreshTokenRepository {

    @Inject
    private DBInitializer dbInitializer;

    private DatabaseReference login = null;

    @Inject
    public ResfreshTokenRepository() {
        login = FirebaseDatabase.getInstance().getReference("Logare");
    }

    @Override
    public void save(String username, Boolean revoked, String refreshToken) throws NoSuchAlgorithmException {
        Map<String, Object> data = new HashMap<>();

        RefreshTokenEntity refreshTokenEntity = new RefreshTokenEntity(username, revoked, refreshToken);

        DatabaseReference inregistrareNoua = login.push();
        inregistrareNoua.setValue(refreshTokenEntity.toMap(), (entityDatabaseError, entityDatabaseReference) -> {
            if (entityDatabaseError == null) {
                System.out.println("Succes");
            } else {
                System.err.println("Eroare la crearea nodului: " + entityDatabaseError.getMessage());
            }
        });
    }
    @Override
    public Optional<RefreshTokenEntity> findByRefreshToken(String refreshToken) {
        final CountDownLatch latch = new CountDownLatch(1);
        final Boolean[] ok = {false};
        final RefreshTokenEntity[] refreshTokenEntity = {null};
        System.out.println("da45");
        login.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    DataSnapshot tokenSnapshot = snapshot.child("refreshToken");
                    DataSnapshot revokedSnapshot = snapshot.child("revoked");
                    DataSnapshot numeSnapshot = snapshot.child("username");
                    DataSnapshot createdSnapshot = snapshot.child("date created");
                    String refresh = tokenSnapshot.getValue(String.class);
                    Boolean revoked = revokedSnapshot.getValue(Boolean.class);
                    String username = numeSnapshot.getValue(String.class);
                    String dateCreated = numeSnapshot.getValue(String.class);

                    System.out.println("Refresh from database: " + refresh);
                    System.out.println("Refresh token parameter: " + refreshToken);

                    if (refresh != null && refresh.equals(refreshToken)) {
                        System.out.println("Am ajuns in if");
                        System.out.println("Am ajuns in if 1");
                        System.out.println("Matching refresh tokens");
                        ok[0] = true;
                        System.out.println("Valoare ok "+String.valueOf(ok[0]));
                        try {
                            refreshTokenEntity[0] = new RefreshTokenEntity(username, revoked, refresh);
                            if(refreshTokenEntity[0] == null){
                                System.out.println("nu e null");
                            }
                        } catch (NoSuchAlgorithmException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }

                latch.countDown();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                latch.countDown();
            }
        });

        try {
            latch.await(8, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        synchronized (latch) {
            if (!ok[0]) {
                System.out.println("dada123");
                return Optional.empty();
            }

            System.out.println("dada123456");
            return Optional.of(refreshTokenEntity[0]);
        }
    }



    @Override
    public long updateByUsername(String username, boolean revoked) {
        login.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println("da4");
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String usernameDB = snapshot.child("username").getValue(String.class);
                    if(username == usernameDB) {
                        Map<String, Object> data = new HashMap<>();
                        data.put("revoked", true);
                        snapshot.getRef().updateChildren(data, null);
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("Eroare la citire");
            }
        });

        return 0;
    }
}