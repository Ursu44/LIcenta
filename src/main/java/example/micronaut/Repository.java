package example.micronaut;

import com.google.firebase.database.*;
import jakarta.inject.Inject;

import java.util.ArrayList;
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

    private DatabaseReference materii = null;


    public Repository() {
        dataReference = FirebaseDatabase.getInstance().getReference("Profesori");
        emailIndex = FirebaseDatabase.getInstance().getReference().child("UniqueMail");
        catalog = FirebaseDatabase.getInstance().getReference().child("Catalog");
        materii = FirebaseDatabase.getInstance().getReference().child("Materii");

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
        CountDownLatch latch = new CountDownLatch(3);

        ArrayList<String> materii1 = new ArrayList<String>();
        materii1.add("Fizică");
        materii1.add("Geografie");
        materii1.add("Istorie");
        materii1.add("Limba engleza");
        materii1.add("Limba si literatura romana");
        materii1.add("Matematică");
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
                latch.countDown();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.out.println("Citire esuata");
                latch.countDown();
            }
        });


        System.out.println("Aici ati ajus prof");
        String materie = profesor.getMaterie();
        System.out.println("Materia cu care cautam "+materie);
        System.out.println("Aici ati ajus prof 1");
        catalog.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String grupa = snapshot.child("grupa").getValue(String.class);
                    String an = snapshot.child("an").getValue(String.class);
                    String grupe = profesor.getGrupe();
                    HashMap<String, Object> materii2 = profesor.getMaterii();
                    for (String i : materii1) {
                        String materie;
                        materie = i + " - " + an;
                        if (grupe.contains(grupa) && materii2.containsValue(materie)) {
                            DatabaseReference elevRef = snapshot.getRef();
                            DatabaseReference materieRef = elevRef.child(i);
                            Map<String, Object> data = new HashMap<>();
                            String mail = profesor.getMail();
                            data.put("mailProfesor", mail);
                            materieRef.updateChildren(data, null);
                        }
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

        materii.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String grupa = snapshot.child("grupa").getValue(String.class);
                    String an = snapshot.child("an").getValue(String.class);
                    String grupe = profesor.getGrupe();
                    HashMap<String, Object> materii2 = profesor.getMaterii();
                    for (String i : materii1) {
                        String materie;
                        materie = i + " - " + an;
                        if (grupe.contains(grupa) && materii2.containsValue(materie)) {
                            DatabaseReference elevRef = snapshot.getRef();
                            DatabaseReference materieRef = elevRef.child(i);
                            Map<String, Object> data = new HashMap<>();
                            String mail = profesor.getMail();
                            data.put("mailProfesor", mail);
                            materieRef.updateChildren(data, null);
                        }
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

    public void updateConfirmation(String token) {
        dataReference.addListenerForSingleValueEvent(new ValueEventListener() {
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
