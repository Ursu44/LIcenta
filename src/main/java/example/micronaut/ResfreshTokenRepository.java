package example.micronaut;

import com.google.firebase.database.*;
import jakarta.inject.Inject;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ResfreshTokenRepository implements IRefreshTokenRepository {

    @Inject
    private DBInitializer initializer;

    private DatabaseReference login = null;

    @Inject
    public ResfreshTokenRepository() {
        login = FirebaseDatabase.getInstance().getReference("Loogare");
    }

    @Override
    public void save(String username, Boolean revoked, String refreshToken) {
        Map<String, Object> data = new HashMap<>();

        data.put("username", username);
        data.put("state", revoked);
        data.put("refresh", refreshToken);

        DatabaseReference newRecordRef = login.push();
        newRecordRef.setValue(data, (entityDatabaseError, entityDatabaseReference) -> {
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
        login.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    refreshTokenEntity[0] = snapshot.getValue(RefreshTokenEntity.class);
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