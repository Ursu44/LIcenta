package com.example.repository;

import com.example.SendMail;
import com.example.firebase.FirebaseInitializer;
import com.google.firebase.database.*;
import jakarta.inject.Inject;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractFirebasRepository<T> implements FireBaseRepository<T> {

    @Inject
    private FirebaseInitializer firebaseInitializer;
    @Inject
    private SendMail sendMail;
    private DatabaseReference dataReference = null;
    private DatabaseReference emailIndex = null;
    private DatabaseReference lectures = null;

    public AbstractFirebasRepository(String dataCollectionName) {
        dataReference = FirebaseDatabase.getInstance().getReference(dataCollectionName);
        emailIndex = FirebaseDatabase.getInstance().getReference().child("UniqueMail");
        lectures = FirebaseDatabase.getInstance().getReference().child("Materii");;
        emailIndex.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    String id = "id";
                    String mail = "mail@example.com";

                    Map<String, Object> data= new HashMap<>();
                    data.put("id", id);
                    data.put("email", mail);

                    emailIndex.setValue(data, (databaseError, databaseReference) -> {
                        if (databaseError == null) {
                            System.out.println("UniqueMail node creat cu succes");
                        } else {
                            System.err.println("Eroare la crearea nodului: " + databaseError.getMessage());
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.err.println("Read operation cancelled: " + databaseError.getMessage());
            }
        });

        lectures.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    String id = "id";

                    Map<String, Object> data= new HashMap<>();
                    data.put("id", id);

                    lectures.setValue(data, (databaseError, databaseReference) -> {
                        if (databaseError == null) {
                            System.out.println("UniqueMail node creat cu succes");
                        } else {
                            System.err.println("Eroare la crearea nodului: " + databaseError.getMessage());
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.err.println("Read operation cancelled: " + databaseError.getMessage());
            }
        });
    }

    public void create(T entity) {
        String email = getEmailFromEntity(entity);
        String encodeEmail = email.replace(".",",");
        DatabaseReference entityNew = dataReference.push();
        String id = entityNew.getKey();
        emailIndex.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                boolean emailExistence = false;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String emailValue = snapshot.child("email").getValue(String.class);
                    if (snapshot.child("email").getValue(String.class) != null && emailValue.equals(encodeEmail)) {
                        emailExistence = true;
                        break;
                    }
                }
                if (!emailExistence) {
                    Map<String, Object> data= new HashMap<>();
                    data.put("id", id);
                    data.put("email", encodeEmail);
                    DatabaseReference emailIndexNou = emailIndex.push();
                    emailIndexNou.setValue(data, (databaseError, databaseReference) -> {
                        if (databaseError == null) {
                            System.out.println("Email saved successfully");
                            Map<String, Object> verificare = new HashMap<>();
                            verificare.put("verificat","nu");
                            entityNew.setValue(entity, (entityDatabaseError, entityDatabaseReference) -> {
                                if (entityDatabaseError == null) {
                                    System.out.println("Entity saved successfully");
                                    sendMail.setMailTo(email);
                                    System.out.println("token "+sendMail.getConfirmatonToken());
                                    try {
                                        sendMail.sending();
                                        System.out.println("token "+sendMail.getConfirmatonToken());
                                        Map<String, Object> confirmation = new HashMap<>();
                                        confirmation.put("token", sendMail.getConfirmatonToken());
                                        entityNew.updateChildren(confirmation, (confirmationError, confirmationReference) -> {
                                            if (confirmationError == null) {
                                                System.out.println("Confirmation updated successfully");
                                            } else {
                                                System.err.println("Error updating confirmation: " + confirmationError.getMessage());
                                            }
                                        });

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    System.out.println("Error creating entity: " + entityDatabaseError.getMessage());
                                }
                            });
                            entityNew.updateChildren(verificare, null);
                        } else {
                            System.out.println("Email could not be saved");
                        }
                    });

                } else {System.out.println("Email already exists in the database");
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.out.println("Citire esuata");
            }
        });
        lectures.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String,  Object> data= new HashMap<>();
                Map<String, Object> detaliiLectie= new HashMap<>();
                detaliiLectie.put("nume", "Lectie_1");
                detaliiLectie.put("Inforamtie_1", "bla bla");
                detaliiLectie.put("Inforamtie_2", "bla bla bla");
                detaliiLectie.put("progres", 0);
                data.put("Lectii",detaliiLectie);
                data.put("mail",encodeEmail);

                DatabaseReference newLectureReference = lectures.push();
                newLectureReference.setValue(data, (databaseError, databaseReference) -> {
                    if (databaseError == null) {
                        System.out.println("Datele lectiei au fost salvate bine");
                    } else {
                        System.err.println("Datele lectiei nu au fost salvate bine : " + databaseError.getMessage());
                    }
                });


            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.out.println("Citire esuata");
            }
        });
    }

    public abstract void read();
    public abstract void update(T entity,String identifier);
    protected abstract String getEmailFromEntity(T entity);
    public abstract void updateConfirmation(String token);


}
