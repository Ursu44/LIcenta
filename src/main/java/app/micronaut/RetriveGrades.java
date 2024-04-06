package app.micronaut;

import com.google.firebase.database.*;
import jakarta.inject.Inject;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class RetriveGrades {

    @Inject
    private DBCreate dbInitializer;

    private DatabaseReference note = null;

    @Inject
    public RetriveGrades() {
        note = FirebaseDatabase.getInstance().getReference("Catalog");
    }

    public JSONObject ExtractGrades(String gmail,String tip) {
        CountDownLatch latch = new CountDownLatch(1);
        JSONObject raspuns = new JSONObject();
        System.out.println("Tipul "+tip);
        note.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (tip.equals("elev")) {
                    int nr = 0;
                    ArrayList<String> materii = new ArrayList<String>();
                    materii.add("Fizică");
                    materii.add("Matematică");
                    materii.add("Istorie");
                    materii.add("Geografie");
                    materii.add("Limba si literatura romana");
                    materii.add("Limba engleza");
                    String grupaCurenta = "";
                    for (DataSnapshot studentSnapshot : dataSnapshot.getChildren()) {
                        String mail1 = studentSnapshot.child("mailElev").getValue(String.class);
                        if (mail1.equals(gmail)) {
                            grupaCurenta = studentSnapshot.child("grupa").getValue(String.class);
                            break;
                        }
                    }
                    System.out.println("Grupa " + grupaCurenta);
                    for (DataSnapshot studentSnapshot : dataSnapshot.getChildren()) {
                        String mail1 = studentSnapshot.child("mailElev").getValue(String.class);
                        String grupaAici = studentSnapshot.child("grupa").getValue(String.class);
                        if (studentSnapshot.exists() && mail1 != null && grupaAici.equals(grupaCurenta)) {
                            nr++;
                            String mail = studentSnapshot.child("mailElev").getValue(String.class);
                            System.out.println(mail);
                            String nume = studentSnapshot.child("nume").getValue(String.class);
                            System.out.println(nume);
                            String prenume = studentSnapshot.child("prenume").getValue(String.class);
                            System.out.println(prenume);
                            String grupa = studentSnapshot.child("grupa").getValue(String.class);
                            System.out.println(grupa);
                            HashMap<String, Object> data = new HashMap<String, Object>();
                            HashMap<String, Object> data1 = new HashMap<String, Object>();
                            data.put("mail", mail);
                            data.put("nume", nume);
                            data.put("prenume", prenume);
                            data.put("grupa", grupa);
                            System.out.println("data " + data);
                            for (int i = 0; i < materii.size(); i++) {
                                DataSnapshot materieSnapshot = studentSnapshot.child(materii.get(i));
                                if (materieSnapshot.exists()) {
                                    Integer nota1 = materieSnapshot.child("Nota1").getValue(Integer.class);
                                    data1.put("Nota1", nota1);
                                    Integer nota2 = materieSnapshot.child("Nota2").getValue(Integer.class);
                                    data1.put("Nota2", nota2);
                                    Integer nota3 = materieSnapshot.child("Nota3").getValue(Integer.class);
                                    data1.put("Nota3", nota3);
                                    Integer nota4 = materieSnapshot.child("Nota4").getValue(Integer.class);
                                    data1.put("Nota4", nota4);
                                    Integer medie = materieSnapshot.child("medie").getValue(Integer.class);
                                    data1.put("Medie", medie);
                                }
                                if (materii.get(i) == "Fizică") {
                                    materii.set(i, "Fizica");
                                }
                                if (materii.get(i) == "Matematică") {
                                    materii.set(i, "Matematica");
                                }
                                if (materii.get(i) == "Limba si literatura romana") {
                                    materii.set(i, "Limbasiliteraturaromana");
                                }
                                if (materii.get(i) == "Limba engleza") {
                                    materii.set(i, "Limbaengleza");
                                }
                                data.put(materii.get(i), data1);
                            }
                            System.out.println("Elev " + nr);
                            raspuns.put("elev" + nr, data);
                        }
                    }
                    latch.countDown();
                }
                else if (tip.equals("profesor")) {
                    int nr = 0;
                    //ystem.out.println("Am intrat aici");
                    //System.out.println("mail "+gmail);
                    HashMap<String, Object> data = new HashMap<String, Object>();
                    HashMap<String, Object> elevi = new HashMap<String, Object>();
                    ArrayList<String> materii = new ArrayList<String>();
                    materii.add("Fizică");
                    materii.add("Matematică");
                    materii.add("Istorie");
                    materii.add("Geografie");
                    materii.add("Limba si literatura romana");
                    materii.add("Limba engleza");
                    for (DataSnapshot studentSnapshot : dataSnapshot.getChildren()) {
                        data.put("nume", studentSnapshot.child("nume").getValue(String.class));
                        data.put("prenume", studentSnapshot.child("prenume").getValue(String.class));
                        data.put("grupa", studentSnapshot.child("grupa").getValue(String.class));
                        data.put("mail", studentSnapshot.child("mailElev").getValue(String.class));
                        data.put("an", studentSnapshot.child("an").getValue(String.class));
                        HashMap<String, Object> data1 = new HashMap<String, Object>();
                        for (int i = 0; i < materii.size(); i++) {
                            String mailProf = studentSnapshot.child(materii.get(i)).child("mailProfesor").getValue(String.class);
                            if (!mailProf.equals("") && mailProf.equals(gmail)) {
                                //System.out.println("Am intrat aici 123");
                                System.out.println("Mail "+mailProf);
                                System.out.println("Materie "+materii.get(i));
                                System.out.println("Student"+ studentSnapshot.child("nume").getValue(String.class));
                                Integer nota1 = studentSnapshot.child(materii.get(i)).child("Nota1").getValue(Integer.class);
                                data1.put("Nota1", nota1);
                                Integer nota2 = studentSnapshot.child(materii.get(i)).child("Nota2").getValue(Integer.class);
                                data1.put("Nota2", nota2);
                                Integer nota3 = studentSnapshot.child(materii.get(i)).child("Nota3").getValue(Integer.class);
                                data1.put("Nota3", nota3);
                                Integer nota4 = studentSnapshot.child(materii.get(i)).child("Nota4").getValue(Integer.class);
                                data1.put("Nota4", nota4);
                                Integer medie = studentSnapshot.child(materii.get(i)).child("medie").getValue(Integer.class);
                                data1.put("Medie", medie);
                                String materiePusa = materii.get(i) ;
                                if (materii.get(i) == "Fizică") {
                                    materiePusa = "Fizica";
                                }
                                if (materii.get(i) == "Matematică") {
                                    materiePusa = "Matematica";
                                }
                                if (materii.get(i) == "Limba si literatura romana") {
                                    materiePusa = "Limbasiliteraturaromana";
                                }
                                if (materii.get(i) == "Limba engleza") {
                                    materiePusa =  "Limbaengleza";
                                }
                                data.put(materiePusa, data1);
                                nr++;
                                raspuns.put("elev" + nr, data);
                            }
                        }
                    }
                    //System.out.println("Raspuns prof " + raspuns);
                }
            }


            @Override
            public void onCancelled(DatabaseError error) {
                System.out.println("Eroare la extragerea cursurilor: " + error.getMessage());
                latch.countDown();
            }
        });

        try {
            latch.await(2, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("Raspuns "+raspuns);
        return raspuns;
    }
}
