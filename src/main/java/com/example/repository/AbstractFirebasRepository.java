package com.example.repository;

import com.example.SendMail;
import com.example.TeacherSearch;
import com.example.firebase.FirebaseInitializer;
import com.example.model.Student;
import com.google.firebase.database.*;
import jakarta.inject.Inject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;

public class AbstractFirebasRepository{

    @Inject
    private FirebaseInitializer firebaseInitializer;
    @Inject
    private SendMail sendMail;
    private DatabaseReference emailIndex = null;
    private DatabaseReference profesori = null;
    private DatabaseReference lectures = null;
    private DatabaseReference catalog = null;
    private DatabaseReference studenti = null;
    private DatabaseReference cursuri = null;
    private DatabaseReference grupe = null;

    private TeacherSearch searching = new TeacherSearch();

    public AbstractFirebasRepository() {
        emailIndex = FirebaseDatabase.getInstance().getReference().child("UniqueMail");
        grupe = FirebaseDatabase.getInstance().getReference().child("Grupe");
        lectures = FirebaseDatabase.getInstance().getReference().child("Materii");
        catalog = FirebaseDatabase.getInstance().getReference().child("Catalog");
        studenti = FirebaseDatabase.getInstance().getReference().child("Studenti");
        profesori = FirebaseDatabase.getInstance().getReference().child("Profesori");
        cursuri = FirebaseDatabase.getInstance().getReference("CursuriAni");
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

    public void create(Student student) {
        String email = student.getMail();
        String encodeEmail = email.replace(".",",");
        String nume = student.getNume();
        String prenume = student.getPrenume();
        CountDownLatch latch = new CountDownLatch(2);
        DatabaseReference entityNew = studenti.push();
        DatabaseReference catalogReference = catalog.push();
        DatabaseReference materiiReference = lectures.push();
        String id = entityNew.getKey();
         String[] an = {""};
         String[] grupa = {""};
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
                 boolean[] gasit = {false};
                 String[] an_grupa = {""};
                boolean finalEmailExistence = emailExistence;
                cautStudent(nume, prenume, email)
                        .thenAccept(result -> {
                            if (!result.isEmpty()) {
                                System.out.println("Studentul a fost găsit în grupa: " + result);
                                gasit[0] = true;
                                an_grupa[0] = result;
                                an[0] =an_grupa[0].split("_")[0];
                                grupa[0] = an_grupa[0].split("_")[1];
                                student.setAn(an_grupa[0].split("_")[0]);
                                student.setGrupa(an_grupa[0].split("_")[1]);
                                if (!finalEmailExistence && gasit[0]) {
                                    cautMail(an[0], grupa[0])
                                            .thenAccept(result1 -> {
                                                if (!result1.isEmpty()) {
                                                    System.out.println("Mail ul a fost găsit : " + result1);
                                                    Map<String, Object> data3 = new HashMap<>();
                                                    data3.put("an", an[0]);
                                                    data3.put("grupa", grupa[0]);
                                                    data3.put("mailElev", student.getMail());
                                                    data3.put("nume", student.getNume());
                                                    data3.put("prenume", student.getPrenume());
                                                    for (String i : materii1) {
                                                        Map<String, Object> data4 = new HashMap<>();
                                                        data4.put("Nota1", 0);
                                                        data4.put("Nota2", 0);
                                                        data4.put("Nota3", 0);
                                                        data4.put("Nota4", 0);
                                                        data4.put("medie", 0);
                                                        if(result1.containsKey(i)){
                                                            data4.put("mailProfesor", result1.get(i));
                                                        } else{
                                                            data4.put("mailProfesor", "");
                                                        }
                                                        data3.put(i, data4);
                                                    }
                                                    catalogReference.setValue(data3, (entityDatabaseError, entityDatabaseReference) -> {
                                                        if (entityDatabaseError == null) {
                                                            System.out.println("S-a adaugat ");

                                                        } else {
                                                            System.out.println("Error creating entity: " + entityDatabaseError.getMessage());
                                                        }
                                                    });
                                                    DatabaseReference cursuriReference = cursuri.child("-NuTFYX9gGrHFrMmtVlE").child(an[0]);
                                                    cursuriReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                            System.out.println("ADAUG LA MATERII " + result1);
                                                            Map<String, Object> data7 = new HashMap<>();

                                                            data7.put("an", an[0]);
                                                            data7.put("grupa", grupa[0]);
                                                            data7.put("mailElev", student.getMail());
                                                            for (DataSnapshot materieSnapshot : dataSnapshot.getChildren()) {
                                                                Map<String, Object> data6 = new HashMap<>();
                                                                String materia1 = materieSnapshot.getKey() ;
                                                                    if(materia1.equals("Fizica")){
                                                                        materia1 = "Fizică";
                                                                    } else if(materia1.equals("Matematica")){
                                                                        materia1 = "Matematică";
                                                                    } else{
                                                                        materia1 = materia1;
                                                                    }
                                                                Map<String, Object> data5 = new HashMap<>();

                                                                for (DataSnapshot capitolSnapshot : materieSnapshot.getChildren()) {
                                                                    String capitolKey = capitolSnapshot.getKey();
                                                                    Map<String, Object> capitolData = new HashMap<>();
                                                                    for (DataSnapshot lectieSnapshot : capitolSnapshot.getChildren()) {
                                                                        String lectieKey = lectieSnapshot.getKey();
                                                                        Object lectieValue = lectieSnapshot.getValue();
                                                                        data6.put(lectieKey, lectieValue);
                                                                        data6.put("progres", 0);
                                                                        data6.put("activat", true);
                                                                    }
                                                                    data5.put(capitolKey, data6);
                                                                }
                                                                data5.put("durataTest", "0:30");
                                                                data5.put("durataTest1", "0:45");
                                                                data5.put("incercari", 3);
                                                                data5.put("incercari1", 3);
                                                                data5.put("testDat", false);
                                                                System.out.println("Materie "+materia1);
                                                                System.out.println("ADAUG LA MATERII 1" + result1);
                                                                if(result1.containsKey(materia1)) {
                                                                    System.out.println("Ce mail s-a pus "+result1.get(materia1)+" pentru "+materia1);
                                                                    data5.put("mailProfesor", result1.get(materia1));
                                                                    System.out.println(data5);
                                                                    data7.put(materia1, data5);
                                                                }
                                                                else{
                                                                    data5.put("mailProfesor", "");
                                                                    data7.put(materia1, data5);
                                                                }


                                                            }
                                                            materiiReference.setValue(data7, (entityDatabaseError, entityDatabaseReference) -> {
                                                                if (entityDatabaseError == null) {
                                                                    System.out.println("S-a adaugat ");

                                                                } else {
                                                                    System.out.println("Error creating entity: " + entityDatabaseError.getMessage());
                                                                        }
                                                                    });

                                                                }
                                                                @Override
                                                                public void onCancelled(DatabaseError error) {
                                                                    System.out.println("Citire esuata");
                                                                }
                                                            });
                                                            latch.countDown();

                                                } else {
                                                    System.out.println("Mail ul nu a fost găsit.");
                                                }
                                            })
                                            .exceptionally(ex -> {
                                                System.out.println("A apărut o excepție: " + ex.getMessage());
                                                return null;
                                            });
                                    System.out.println("Gasit "+ an_grupa[0]);
                    Map<String, Object> data= new HashMap<>();
                    data.put("email", encodeEmail);
                    data.put("id", id);
                    DatabaseReference emailIndexNou = emailIndex.push();
                    emailIndexNou.setValue(data, (databaseError, databaseReference) -> {
                        if (databaseError == null) {
                            System.out.println("Email saved successfully");
                            Map<String, Object> verificare = new HashMap<>();
                            verificare.put("verificat","nu");
                            entityNew.setValue(student, (entityDatabaseError, entityDatabaseReference) -> {
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
                            } else {
                                System.out.println("Studentul nu a fost găsit.");
                            }
                        })
                        .exceptionally(ex -> {
                            System.out.println("A apărut o excepție: " + ex.getMessage());
                            return null;
                        });

                latch.countDown();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.out.println("Citire esuata");
                latch.countDown();
            }
        });


        /*if(role.equals("student")) {
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

                }

                @Override
                public void onCancelled(DatabaseError error) {
                    System.out.println("Citire esuata");
                }
            });

            catalog.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    lectures.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

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
            });*/
        }


    CompletableFuture<String> cautStudent(String numeStudent, String prenumeStudent, String mail) {
        CompletableFuture<String> future = new CompletableFuture<>();
        grupe.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot an : dataSnapshot.getChildren()) {
                    for (DataSnapshot grupa : an.getChildren()) {
                        for (DataSnapshot student : grupa.getChildren()) {
                            String nume = student.child("nume").getValue(String.class);
                            String prenume = student.child("prenume").getValue(String.class);
                            String email = student.child("mail").getValue(String.class);
                            if(numeStudent.equals(nume) && prenumeStudent.equals(prenume) && mail.equals(email)){
                                System.out.println("L-am gasit pe studentul "+nume+" "+prenume);
                                future.complete(an.getKey()+"_"+grupa.getKey());
                                break;
                            }
                        }
                    }
                }
                future.complete("");
            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.out.println("Citire esuata: " + error.getMessage());
            }
        });
        return future;
    }

    CompletableFuture<HashMap<String, Object>> cautMail(String an, String grupa) {
        CompletableFuture<HashMap<String, Object>> future = new CompletableFuture<>();
        HashMap<String, Object> profesori1 = new HashMap<String, Object>();
        profesori.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot profesorSnapshot : dataSnapshot.getChildren()) {
                    System.out.println("Am intrat 1");
                    String grupe = profesorSnapshot.child("grupe").getValue(String.class);
                   HashMap<String, Object> ani = (HashMap<String, Object>) profesorSnapshot.child("materii").getValue();
                    System.out.println("Hash ul "+ani);
                    ArrayList<String> materii1 = new ArrayList<String>();
                    materii1.add("Fizică");
                    materii1.add("Geografie");
                    materii1.add("Istorie");
                    materii1.add("Limba engleza");
                    materii1.add("Limba si literatura romana");
                    materii1.add("Matematică");
                    for (String i : materii1) {
                        System.out.println("Am intrat 2");
                        String materia = i +" - "+an;
                        if (grupe.contains(grupa) && ani.containsValue(materia)) {
                            profesori1.put(i, profesorSnapshot.child("mail").getValue(String.class));
                        }
                    }
                }
                future.complete(profesori1);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.out.println("Citire esuata: " + error.getMessage());
            }
        });
        return future;
    }

    public void updateConfirmation(String token) {
        studenti.addListenerForSingleValueEvent(new ValueEventListener() {
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
