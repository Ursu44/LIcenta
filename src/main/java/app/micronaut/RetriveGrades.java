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
    private DatabaseReference profesori = null;
    @Inject
    public RetriveGrades() {
        note = FirebaseDatabase.getInstance().getReference("Catalog");
        profesori = FirebaseDatabase.getInstance().getReference("Profesori");
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
                            data.put("mail", mail);
                            data.put("nume", nume);
                            data.put("prenume", prenume);
                            data.put("grupa", grupa);
                            System.out.println("data " + data);
                            for (int i = 0; i < materii.size(); i++) {
                                DataSnapshot materieSnapshot = studentSnapshot.child(materii.get(i));
                                HashMap<String, Object> data1 = new HashMap<String, Object>();
                                if (materieSnapshot.exists()) {
                                    System.out.println("Materia "+materii.get(i));
                                    int nota1 = studentSnapshot.child(materii.get(i)).child("Nota1").getValue(Integer.class);
                                    data1.put("Nota1", studentSnapshot.child(materii.get(i)).child("Nota1").getValue(Integer.class));
                                    System.out.println("Nota 1 "+nota1);
                                    data1.put("Nota1", nota1);
                                    int nota2 = studentSnapshot.child(materii.get(i)).child("Nota2").getValue(Integer.class);
                                    System.out.println("Nota 2 "+nota2);
                                    data1.put("Nota2", nota2);
                                    int nota3 = studentSnapshot.child(materii.get(i)).child("Nota3").getValue(Integer.class);
                                    System.out.println("Nota 3 "+nota3);
                                    data1.put("Nota3", nota3);
                                    int nota4 = studentSnapshot.child(materii.get(i)).child("Nota4").getValue(Integer.class);
                                    System.out.println("Nota 4 "+nota4);
                                    data1.put("Nota4", nota4);
                                    int medie = studentSnapshot.child(materii.get(i)).child("medie").getValue(Integer.class);
                                    data1.put("Medie", studentSnapshot.child(materii.get(i)).child("medie").getValue(Integer.class));
                                }
                                String nouaMaterie ="";
                                if (materii.get(i) == "Fizică") {
                                    nouaMaterie = "Fizica";
                                }
                                else if (materii.get(i) == "Matematică") {
                                    nouaMaterie ="Matematica";
                                }
                                else if (materii.get(i) == "Limba si literatura romana") {
                                    nouaMaterie = "Limbasiliteraturaromana";
                                }
                                else if (materii.get(i) == "Limba engleza") {
                                    nouaMaterie = "Limbaengleza";
                                }
                                else{
                                    nouaMaterie = materii.get(i);
                                }
                                System.out.println("Data 1 "+data1);
                                data.put(nouaMaterie, data1);
                                System.out.println("Elev " + nr);

                                raspuns.put("elev" + nr, data);
                            }
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

                    ArrayList<String> materii1 = new ArrayList<String>();
                    materii1.add("Fizică");
                    materii1.add("Matematică");
                    materii1.add("Istorie");
                    materii1.add("Geografie");
                    materii1.add("Limba si literatura romana");
                    materii1.add("Limba engleza");
                    for (DataSnapshot studentSnapshot : dataSnapshot.getChildren()) {
                        for (int i = 0; i < materii.size(); i++) {
                            String mailProf = studentSnapshot.child(materii.get(i)).child("mailProfesor").getValue(String.class);
                            if (!mailProf.equals("") && mailProf.equals(gmail)) {
                                HashMap<String, Object> data1 = new HashMap<String, Object>();
                                data.put("nume", studentSnapshot.child("nume").getValue(String.class));
                                data.put("prenume", studentSnapshot.child("prenume").getValue(String.class));
                                data.put("grupa", studentSnapshot.child("grupa").getValue(String.class));
                                data.put("mail", studentSnapshot.child("mailElev").getValue(String.class));
                                //System.out.println("Am intrat aici 123");
                                System.out.println("Mail "+mailProf);
                                data.put("an", materii.get(i)+"_"+studentSnapshot.child("an").getValue(String.class));
                                String an = materii.get(i)+"_"+studentSnapshot.child("an").getValue(String.class);
                                System.out.println("Materie "+materii.get(i)+"_"+studentSnapshot.child("an").getValue(String.class));
                                //System.out.println("Student"+ studentSnapshot.child("nume").getValue(String.class));
                                int nota1 = studentSnapshot.child(materii.get(i)).child("Nota1").getValue(Integer.class);
                                data1.put("Nota1", nota1);
                                int nota2 = studentSnapshot.child(materii.get(i)).child("Nota2").getValue(Integer.class);
                                data1.put("Nota2", nota2);
                                int nota3 = studentSnapshot.child(materii.get(i)).child("Nota3").getValue(Integer.class);
                                data1.put("Nota3", nota3);
                                int nota4 = studentSnapshot.child(materii.get(i)).child("Nota4").getValue(Integer.class);
                                data1.put("Nota4", nota4);
                                int medie = studentSnapshot.child(materii.get(i)).child("medie").getValue(Integer.class);
                                data1.put("Medie", medie);
                                String materiePusa = materii.get(i) ;
                                if (materii.get(i) == "Fizică")  {
                                    materiePusa = "Fizica";
                                }
                                else if (materii.get(i) == "Matematică") {
                                    materiePusa = "Matematica";
                                }
                                else if (materii.get(i) == "Limba si literatura romana") {
                                    materiePusa = "Limbasiliteraturaromana";
                                }
                                else if (materii.get(i) == "Limba engleza") {
                                    materiePusa =  "Limbaengleza";
                                }
                                else{
                                    materiePusa = materii.get(i);
                                }
                                System.out.println("Materia "+materiePusa);
                                data.put(materiePusa, data1);
                                nr++;
                                String an1 = (String) data.get("an");
                                System.out.println("AN " + an1);
                                for (int j = 0; j < materii1.size(); j++) {
                                    if (!an.contains(materii1.get(j))) {
                                        System.out.println("S-a intrat ");
                                        String materiePusa1 = materii1.get(j);
                                        if (materii.get(j) == "Fizică") {
                                            materiePusa1 = "Fizica";
                                        } else if (materii.get(j) == "Matematică") {
                                            materiePusa1 = "Matematica";
                                        } else if (materii.get(j) == "Limba si literatura romana") {
                                            materiePusa1 = "Limbasiliteraturaromana";
                                        } else if (materii.get(j) == "Limba engleza") {
                                            materiePusa1 = "Limbaengleza";
                                        } else {
                                            materiePusa1 = materii.get(j);
                                        }
                                        data.remove(materiePusa1);
                                    }
                                }
                                System.out.println("data 1"+data);
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
            latch.await(6, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        //System.out.println("Raspuns "+raspuns);
        return raspuns;
    }


}
