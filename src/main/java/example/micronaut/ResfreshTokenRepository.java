package example.micronaut;

import com.google.firebase.database.*;
import jakarta.inject.Inject;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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
        final Boolean[] ok = {false};
        final RefreshTokenEntity[] refreshTokenEntity = new RefreshTokenEntity[1];
        System.out.println("da2");
        login.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println("da3");
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String id = snapshot.getKey();
                    DataSnapshot idSnapshot = snapshot.child(id);
                    refreshTokenEntity[0] = idSnapshot.getValue(RefreshTokenEntity.class);
                    if (refreshTokenEntity[0] != null && refreshTokenEntity[0].getRefreshToken().equals(refreshToken)) {
                        if(!refreshTokenEntity[0].getRevoked()){
                            ok[0] =  true;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("Eroare la citire");
            }
        });
        if(ok[0] == false) {
            return Optional.empty();
        }
        return Optional.of(refreshTokenEntity[0]);
    }
}