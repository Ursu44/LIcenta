package com.example.repository;

import com.example.SendMail;
import com.example.TeacherSearch;
import com.example.firebase.FirebaseInitializer;
import com.google.firebase.database.*;
import jakarta.inject.Inject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public abstract class AbstractFirebasRepository<T> implements FireBaseRepository<T> {

    @Inject
    private FirebaseInitializer firebaseInitializer;
    @Inject
    private SendMail sendMail;
    private DatabaseReference dataReference = null;
    private DatabaseReference emailIndex = null;
    private DatabaseReference lectures = null;
    private DatabaseReference catalog = null;

    private DatabaseReference profesori = null;

    private TeacherSearch searching = new TeacherSearch();

    public AbstractFirebasRepository(String dataCollectionName) {
        dataReference = FirebaseDatabase.getInstance().getReference(dataCollectionName);
        emailIndex = FirebaseDatabase.getInstance().getReference().child("UniqueMail");
        lectures = FirebaseDatabase.getInstance().getReference().child("Materii");
        catalog = FirebaseDatabase.getInstance().getReference().child("Catalog");
        profesori = FirebaseDatabase.getInstance().getReference().child("Profesori");
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

    public void create(T entity,String role) {
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
        if(role.equals("student")) {
            Map<String, Object> note = new HashMap<>();
            Map<String, Object> detaliiLectie = new HashMap<>();
            System.out.println("Pun lectii");
            final String[] mailPorf = new String[1];
            CountDownLatch latch = new CountDownLatch(1);
            profesori.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String materia = snapshot.child("materie").getValue(String.class);
                        if(materia.equals("Fizica")){
                            mailPorf[0] = snapshot.child("mail").getValue(String.class);
                            System.out.println("Am luat ce trebuia");
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
                latch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            lectures.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Map<String, Object> data = new HashMap<>();
                    Map<String, Object> informatiiLectie1 = new HashMap<>();
                    Map<String, Object> informatiiLectie2 = new HashMap<>();
                    Map<String, Object> informatiiLectie3 = new HashMap<>();
                    detaliiLectie.put("mailProfesor", mailPorf[0]);
                    System.out.println("Mail prof "+  mailPorf[0]);
                    informatiiLectie1.put("Inforamtie1", "bla bla");
                    informatiiLectie1.put("Inforamtie2", "bla bla bla");
                    informatiiLectie1.put("Inforamtie3", "bla bla bla bla bla");
                    informatiiLectie1.put("Inforamtie4", "bla bla bla bla bla bla");
                    informatiiLectie1.put("activat", false);
                    informatiiLectie1.put("progres", 0);

                    informatiiLectie2.put("Inforamtie1", "Legea lui Ohm");
                    informatiiLectie2.put("Inforamtie2", "Formula puterii");
                    informatiiLectie2.put("Inforamtie3", "Intensitatea curentului");
                    informatiiLectie2.put("Inforamtie4", "Baterii ");
                    informatiiLectie2.put("Inforamtie5", "Blaaaa");
                    informatiiLectie2.put("activat", false);
                    informatiiLectie2.put("progres", 0);

                    informatiiLectie3.put("Inforamtie1", "Principiul 1 al termodinamicii");
                    informatiiLectie3.put("Inforamtie2", "Transformare izocora");
                    informatiiLectie3.put("Inforamtie3", "Transformare izobara");
                    informatiiLectie3.put("Inforamtie4", "Transformarea izoterma ");
                    informatiiLectie3.put("Inforamtie5", "Blaaaa Blaaaa");
                    informatiiLectie3.put("activat", false);
                    informatiiLectie3.put("progres", 0);

                    detaliiLectie.put("Lectia1", informatiiLectie1);
                    detaliiLectie.put("Lectia2", informatiiLectie2);
                    detaliiLectie.put("Lectia3", informatiiLectie3);

                    data.put("Fizica", detaliiLectie);
                    data.put("mailElev", encodeEmail);
                    System.out.println("SALVEZ DATELE IN CATALOG 2");

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

            catalog.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    System.out.println("SALVEZ DATELE IN CATALOG");
                    note.put("Nota1", 0);
                    note.put("Nota2", 0);
                    note.put("Nota3", 0);
                    note.put("Nota4", 0);
                    note.put("medie", 0);
                    lectures.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            System.out.println("SALVEZ DATELE IN CATALOG");
                            note.put("Nota1", 0);
                            note.put("Nota2", 0);
                            note.put("Nota3", 0);
                            note.put("Nota4", 0);
                            note.put("medie", 0);
                            note.put("profesor", mailPorf[0]);

                            Map<String, Object> materie = new HashMap<>();
                            materie.put("Fizica", note);
                            Map<String, Object> elev = new HashMap<>();
                            materie.put("mailElev", getEmailFromEntity(entity));
                            materie.put("numeElev", getLastNameFromEntity(entity));
                            materie.put("prenumenumeElev", getFirstNameFromEntity(entity));
                            DatabaseReference catalogReference = catalog.push();
                            catalogReference.setValue(materie, (databaseError, databaseReference) -> {
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

                @Override
                public void onCancelled(DatabaseError error) {
                    System.out.println("Citire esuata");
                }
            });

        } else if(role.equals("profesor")){
            System.out.println("Aici ati ajus prof");
            String materie = getMaterie(entity);
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
                            data.put("profesor", getEmailFromEntity(entity));
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

    public abstract void read();
    public abstract void update(T entity,String identifier);
    protected abstract String getEmailFromEntity(T entity);
    protected abstract  String getFirstNameFromEntity(T entity);
    protected abstract  String getLastNameFromEntity(T entity);
    protected abstract  String getMaterie(T entity);
    public abstract void updateConfirmation(String token);


}
