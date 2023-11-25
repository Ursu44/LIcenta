package com.example.repository;

import com.example.EncryptDecrypt;
import com.example.SendMail;
import com.example.firebase.FirebaseInitializer;
import com.example.model.Student;
import com.google.firebase.database.*;
import jakarta.inject.Inject;

public class StudentRepository implements FireBaseRepository<Student>{

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

    @Override
    public void create(Student entity) {
        DatabaseReference studentNou = student.push();
        studentNou.setValue(entity, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError == null) {
                    String studentEmail = entity.getMail();
                    sendMail.setMailFrom(studentEmail);
                    sendMail.setMailTo(studentEmail);
                    try {
                        sendMail.sending();
                    }catch (Exception e) {
                        System.out.println("Exception during sending email:");
                        e.printStackTrace();
                    }

                } else {
                    System.err.println("Data could not be saved. " + databaseError.getMessage());
                }
            }
        });
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
}
