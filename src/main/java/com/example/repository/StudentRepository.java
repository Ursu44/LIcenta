package com.example.repository;

import com.example.EncryptDecrypt;
import com.example.SendMail;
import com.example.firebase.FirebaseInitializer;
import com.example.model.Student;
import com.google.firebase.database.*;
import jakarta.inject.Inject;

import java.util.HashMap;
import java.util.Map;

public class StudentRepository  {

    @Inject
    private FirebaseInitializer firebaseInitializer;
    @Inject
    private SendMail sendMail;
    private DatabaseReference student = null;

    private EncryptDecrypt encryptor = new EncryptDecrypt();

    @Inject
    public StudentRepository() {
        student = FirebaseDatabase.getInstance().getReference("Studenti");
    }


    protected String getEmailFromEntity(Student entity) {
        return entity.getMail();
    }

    protected String getFirstNameFromEntity(Student entity) {
        return entity.getPrenume();
    }

    protected String getLastNameFromEntity(Student entity) {
        return entity.getNume();
    }

    protected String getMaterie(Student entity) {
        return null;
    }

    public void updateConfirmation(String token) {
        student.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String tokenValue = snapshot.child("token").getValue(String.class);
                    if (tokenValue != null && tokenValue.equals(token)) {
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
