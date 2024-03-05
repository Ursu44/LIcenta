package example.micronaut;

import com.google.firebase.database.*;
import jakarta.inject.Inject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class Repository {
    @Inject
    private Initializer firebaseInitializer;

    @Inject
    private SendMail sendMail;

    private DatabaseReference dataReference = null;
    private DatabaseReference emailIndex = null;
    private DatabaseReference catalog = null;


    public Repository() {
        dataReference = FirebaseDatabase.getInstance().getReference("Profesori");
        emailIndex = FirebaseDatabase.getInstance().getReference().child("UniqueMail");
        catalog = FirebaseDatabase.getInstance().getReference().child("Catalog");

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
        catalog.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    String id = "id";

                    Map<String, Object> data= new HashMap<>();
                    data.put("id", id);

                    catalog.setValue(data, (databaseError, databaseReference) -> {
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

    public void create(Profesor profesor){
        String email = profesor.getMail();
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
                            entityNew.setValue(profesor, (entityDatabaseError, entityDatabaseReference) -> {
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

        System.out.println("Aici ati ajus prof");
        String materie = profesor.getMaterie();
        System.out.println("Materia cu care cautam "+materie);
        System.out.println("Aici ati ajus prof 1");
        CountDownLatch latch = new CountDownLatch(1);
        catalog.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    DatabaseReference elevRef = snapshot.getRef();
                    DatabaseReference fizicaRef1 = elevRef.child("Fizica");
                    System.out.println("Fizicca ref "+fizicaRef1);
                    String fizicaRef = elevRef.child("Fizica").toString();

                    System.out.println("Adevart "+"Fizica".equals(materie));
                    if("Fizica".equals(materie)){
                        Map<String, Object> data = new HashMap<>();
                        data.put("profesor", profesor.getMail());
                        fizicaRef1.updateChildren(data ,null);
                        System.out.println("Ce se gaseste aici "+ snapshot.child("Fizica").getValue(String.class));
                    }
                }
                latch.countDown();

            }
            @Override
            public void onCancelled(DatabaseError error) {
                System.out.println("Citire esuata");
                latch.countDown();

            }
        });
        try {
            latch.await(2, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
