package com.example.repository;

import com.example.firebase.FirebaseInitializer;
import com.example.model.Student;
import com.google.firebase.database.*;
import jakarta.inject.Inject;

public class StudentRepository implements FireBaseRepository<Student>{

    @Inject
    private FirebaseInitializer firebaseInitializer;
    private DatabaseReference student = null;


    @Inject
    public StudentRepository() {
        student = FirebaseDatabase.getInstance().getReference("Studenti");
    }

    @Override
    public void create(Student entity) {
        System.out.println("da2");
        DatabaseReference studentNou = student.push();
        studentNou.setValue(entity, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError == null) {
                    System.out.println("Data saved successfully");
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
