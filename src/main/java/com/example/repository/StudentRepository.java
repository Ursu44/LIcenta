package com.example.repository;

import com.example.EncryptDecrypt;
import com.example.SendMail;
import com.example.firebase.FirebaseInitializer;
import com.example.model.Student;
import com.google.firebase.database.*;
import jakarta.inject.Inject;

import java.util.HashMap;
import java.util.Map;

public class StudentRepository extends AbstractFirebasRepository<Student>{

    @Inject
    private FirebaseInitializer firebaseInitializer;
    @Inject
    private SendMail sendMail;
    private DatabaseReference student = null;

    private EncryptDecrypt encryptor = new EncryptDecrypt();
    @Inject
    public StudentRepository() {
        super("Studenti");
        student = FirebaseDatabase.getInstance().getReference("Studenti");
    }

    @Override
    protected String getEmailFromEntity(Student entity) {
        return entity.getMail();
    }

    @Override
    public void updateCobnfirmation() {

    }

    @Override
    public void read() {
        student.addValueEventListener(new ValueEventListener() {
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
    public void update(Student entity, String identifier) {
        student.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String emailValue = snapshot.child("mail").getValue(String.class);
                    if (emailValue != null && emailValue.equals(identifier)) {
                        Map<String, Object> data = new HashMap<>();
                        data.put("parola", entity.getParola());
                        snapshot.getRef().updateChildren(data, null);
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }
}
