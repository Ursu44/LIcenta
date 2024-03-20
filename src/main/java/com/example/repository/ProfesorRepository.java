package com.example.repository;

import com.example.firebase.FirebaseInitializer;
import com.example.model.Profesor;
import com.google.firebase.database.*;
import jakarta.inject.Inject;

import java.util.HashMap;
import java.util.Map;

public class ProfesorRepository extends AbstractFirebasRepository<Profesor>{
    @Inject
    private FirebaseInitializer firebaseInitializer;
    private DatabaseReference profesor = null;

    @Inject
    public ProfesorRepository() {
        super("Profesori");
        profesor = FirebaseDatabase.getInstance().getReference("Profesori");
    }

    @Override
    public void read() {
        profesor.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    System.out.println(snapshot.getKey() + ": " + snapshot.getValue());
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("Citirea a esuat: " + databaseError.getCode());
            }
        });
    }

    @Override
    public void update(Profesor entity, String identifier) {
        profesor.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }

    @Override
    protected String getEmailFromEntity(Profesor entity) {
        return entity.getMail();
    }

    @Override
    protected String getFirstNameFromEntity(Profesor entity) {
        return null;
    }

    @Override
    protected String getLastNameFromEntity(Profesor entity) {
        return null;
    }

    @Override
    protected String getMaterie(Profesor entity) {
        return entity.getMaterie();
    }

    @Override
    public void updateConfirmation(String token) {
        profesor.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String tokenValue = snapshot.child("token").getValue(String.class);
                    if(tokenValue != null && tokenValue.equals(token)){
                        Map<String, Object> data = new HashMap<>();
                        data.put("verificat", "da");
                        System.out.println("Verificat cu succes");
                        snapshot.getRef().updateChildren(data, null);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }
}

