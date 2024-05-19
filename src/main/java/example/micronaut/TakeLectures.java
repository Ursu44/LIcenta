package example.micronaut;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.firebase.database.*;
import jakarta.inject.Inject;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class TakeLectures {

    @Inject
    private InitializeDB dbInitializer;
    private DatabaseReference materii = null;
    private DatabaseReference studenti = null;

    @Inject
    public TakeLectures() {
        materii = FirebaseDatabase.getInstance().getReference("Materii");
        studenti = FirebaseDatabase.getInstance().getReference("Studenti");
    }

    public JSONObject ExtracLectures(String gmail, String nume, String tip) {
        CountDownLatch latch = new CountDownLatch(1);
        JSONObject raspuns = new JSONObject();
        JSONObject lectie1 = new JSONObject();

        System.out.println("Materei " + nume);
        materii.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot studentSnapshot : dataSnapshot.getChildren()) {
                    String mail = studentSnapshot.child("mailElev").getValue(String.class);
                    mail = mail.replace(',', '.');
                    if (tip.equals("elev")) {
                        String numeMaterie = nume.split("_")[0];
                        if (mail.equals(gmail)) {
                            System.out.println("Mail " + mail);
                            System.out.println("Materie " + numeMaterie);
                            DataSnapshot materieSnapshot = studentSnapshot.child(numeMaterie);
                            lectie1.put("durataTest", studentSnapshot.child(numeMaterie).child("durataTest").getValue(String.class));
                            lectie1.put("durataTest1", studentSnapshot.child(numeMaterie).child("durataTest1").getValue(String.class));
                            lectie1.put("incercari", studentSnapshot.child(numeMaterie).child("incercari").getValue(Integer.class));
                            lectie1.put("incercari1", studentSnapshot.child(numeMaterie).child("incercari1").getValue(Integer.class));
                            lectie1.put("testDat", studentSnapshot.child(numeMaterie).child("testDat").getValue(Boolean.class));
                            if (materieSnapshot.exists()) {
                                System.out.println("Aici 123");
                                DataSnapshot lectieSnapshot = materieSnapshot.child("Capitlolul 1");
                                if (lectieSnapshot.exists()) {
                                    System.out.println("Aici 12345");
                                    JSONObject lectie = new JSONObject();
                                    JSONObject lectie1 = new JSONObject();
                                    JSONObject lectie2 = new JSONObject();
                                    JSONObject lectie3 = new JSONObject();
                                    JSONObject lectie4 = new JSONObject();
                                    lectie1.put("nume", lectieSnapshot.child("Lectia1").child("nume").getValue(String.class));
                                    lectie1.put("informatie", lectieSnapshot.child("Lectia1").child("info").getValue(String.class));
                                    lectie.put("Lectia1", lectie1);
                                    lectie2.put("nume", lectieSnapshot.child("Lectia2").child("nume").getValue(String.class));
                                    lectie2.put("informatie", lectieSnapshot.child("Lectia2").child("info").getValue(String.class));
                                    lectie.put("Lectia2", lectie2);
                                    lectie3.put("nume", lectieSnapshot.child("Lectia3").child("nume").getValue(String.class));
                                    lectie3.put("informatie", lectieSnapshot.child("Lectia3").child("info").getValue(String.class));
                                    lectie.put("Lectia3", lectie3);
                                    lectie4.put("nume", lectieSnapshot.child("Lectia4").child("nume").getValue(String.class));
                                    lectie4.put("informatie", lectieSnapshot.child("Lectia4").child("info").getValue(String.class));
                                    lectie.put("Lectia4", lectie4);
                                    lectie.put("activat", lectieSnapshot.child("activat").getValue(Boolean.class));
                                    lectie.put("progres", lectieSnapshot.child("progres").getValue(Integer.class));
                                    lectie.put("numeCapitol", lectieSnapshot.child("Nume").getValue(String.class));
                                    raspuns.put("Capitol1", lectie);
                                }
                                DataSnapshot lectieSnapshot1 = materieSnapshot.child("Capitlolul 2");
                                if (lectieSnapshot1.exists()) {
                                    JSONObject lectie12 = new JSONObject();
                                    JSONObject lectie11 = new JSONObject();
                                    JSONObject lectie22 = new JSONObject();
                                    JSONObject lectie33 = new JSONObject();
                                    JSONObject lectie44 = new JSONObject();
                                    JSONObject lectie55 = new JSONObject();

                                    lectie11.put("nume", lectieSnapshot1.child("Lectia1").child("nume").getValue(String.class));
                                    lectie11.put("informatie", lectieSnapshot1.child("Lectia1").child("info").getValue(String.class));
                                    lectie12.put("Lectia1", lectie11);
                                    lectie22.put("nume", lectieSnapshot1.child("Lectia2").child("nume").getValue(String.class));
                                    lectie22.put("informatie", lectieSnapshot1.child("Lectia2").child("info").getValue(String.class));
                                    lectie12.put("Lectia2", lectie22);
                                    lectie33.put("nume", lectieSnapshot1.child("Lectia3").child("nume").getValue(String.class));
                                    lectie33.put("informatie", lectieSnapshot1.child("Lectia3").child("info").getValue(String.class));
                                    lectie12.put("Lectia3", lectie33);
                                    lectie44.put("nume", lectieSnapshot1.child("Lectia4").child("nume").getValue(String.class));
                                    lectie44.put("informatie", lectieSnapshot1.child("Lectia4").child("info").getValue(String.class));
                                    lectie12.put("Lectia4", lectie44);
                                    lectie55.put("nume", lectieSnapshot1.child("Lectia5").child("nume").getValue(String.class));
                                    lectie55.put("informatie", lectieSnapshot1.child("Lectia5").child("info").getValue(String.class));
                                    lectie12.put("Lectia5", lectie55);
                                    lectie12.put("activat", lectieSnapshot1.child("activat").getValue(Boolean.class));
                                    lectie12.put("progres", lectieSnapshot1.child("progres").getValue(Integer.class));
                                    lectie12.put("numeCapitol", lectieSnapshot1.child("Nume").getValue(String.class));
                                    raspuns.put("Capitol2", lectie12);
                                }

                                DataSnapshot lectieSnapshot2 = materieSnapshot.child("Capitlolul 3");
                                if (lectieSnapshot2.exists()) {
                                    JSONObject lectie12 = new JSONObject();
                                    JSONObject lectie11 = new JSONObject();
                                    JSONObject lectie22 = new JSONObject();
                                    JSONObject lectie33 = new JSONObject();
                                    JSONObject lectie44 = new JSONObject();
                                    JSONObject lectie55 = new JSONObject();

                                    lectie11.put("nume", lectieSnapshot2.child("Lectia1").child("nume").getValue(String.class));
                                    lectie11.put("informatie", lectieSnapshot2.child("Lectia1").child("info").getValue(String.class));
                                    lectie12.put("Lectia1", lectie11);
                                    lectie22.put("nume", lectieSnapshot2.child("Lectia2").child("nume").getValue(String.class));
                                    lectie22.put("informatie", lectieSnapshot2.child("Lectia2").child("info").getValue(String.class));
                                    lectie12.put("Lectia2", lectie22);
                                    lectie33.put("nume", lectieSnapshot2.child("Lectia3").child("nume").getValue(String.class));
                                    lectie33.put("informatie", lectieSnapshot2.child("Lectia3").child("info").getValue(String.class));
                                    lectie12.put("Lectia3", lectie33);
                                    lectie44.put("nume", lectieSnapshot2.child("Lectia4").child("nume").getValue(String.class));
                                    lectie44.put("informatie", lectieSnapshot2.child("Lectia4").child("info").getValue(String.class));
                                    lectie12.put("Lectia4", lectie44);
                                    lectie55.put("nume", lectieSnapshot2.child("Lectia5").child("nume").getValue(String.class));
                                    lectie55.put("informatie", lectieSnapshot2.child("Lectia5").child("info").getValue(String.class));
                                    lectie12.put("Lectia5", lectie55);
                                    lectie12.put("activat", lectieSnapshot2.child("activat").getValue(Boolean.class));
                                    lectie12.put("progres", lectieSnapshot2.child("progres").getValue(Integer.class));
                                    lectie12.put("numeCapitol", lectieSnapshot2.child("Nume").getValue(String.class));
                                    raspuns.put("Capitol3", lectie12);
                                }

                                DataSnapshot lectieSnapshot3 = materieSnapshot.child("Capitlolul 4");
                                if (lectieSnapshot3.exists()) {
                                    JSONObject lectie12 = new JSONObject();
                                    JSONObject lectie11 = new JSONObject();
                                    JSONObject lectie22 = new JSONObject();
                                    JSONObject lectie33 = new JSONObject();
                                    JSONObject lectie44 = new JSONObject();
                                    JSONObject lectie55 = new JSONObject();

                                    lectie11.put("nume", lectieSnapshot3.child("Lectia1").child("nume").getValue(String.class));
                                    lectie11.put("informatie", lectieSnapshot3.child("Lectia1").child("info").getValue(String.class));
                                    lectie12.put("Lectia1", lectie11);
                                    lectie22.put("nume", lectieSnapshot3.child("Lectia2").child("nume").getValue(String.class));
                                    lectie22.put("informatie", lectieSnapshot3.child("Lectia2").child("info").getValue(String.class));
                                    lectie12.put("Lectia2", lectie22);
                                    lectie33.put("nume", lectieSnapshot3.child("Lectia3").child("nume").getValue(String.class));
                                    lectie33.put("informatie", lectieSnapshot3.child("Lectia3").child("info").getValue(String.class));
                                    lectie12.put("Lectia3", lectie33);
                                    lectie44.put("nume", lectieSnapshot3.child("Lectia4").child("nume").getValue(String.class));
                                    lectie44.put("informatie", lectieSnapshot3.child("Lectia4").child("info").getValue(String.class));
                                    lectie12.put("Lectia4", lectie44);
                                    lectie12.put("activat", lectieSnapshot3.child("activat").getValue(Boolean.class));
                                    lectie12.put("progres", lectieSnapshot3.child("progres").getValue(Integer.class));
                                    lectie12.put("numeCapitol", lectieSnapshot3.child("Nume").getValue(String.class));
                                    raspuns.put("Capitol4", lectie12);
                                }
                                //System.out.println("Raspuns primit "+raspuns);
                            }
                            raspuns.put("Detalii_test", lectie1);
                        }
                    } else if (tip.equals("profesor")) {
                        System.out.println("Nume pentru prof " + nume);
                        String numeMaterie = nume.split("_")[0];
                        String mailProf = studentSnapshot.child(numeMaterie).child("mailProfesor").getValue(String.class);
                        if (mailProf.equals(gmail)) {
                            DataSnapshot materieSnapshot = studentSnapshot.child(numeMaterie);
                            DataSnapshot lectieSnapshot = materieSnapshot.child("Capitlolul 1");
                            System.out.println("DEtalii test " + studentSnapshot.child(numeMaterie).child("durataTest").getValue(String.class));
                            System.out.println("DEtalii test 1 " + studentSnapshot.child(numeMaterie).child("incercari").getValue(Integer.class));
                            System.out.println("DEtalii test 2" + studentSnapshot.child(numeMaterie).child("testDat").getValue(Boolean.class));
                            lectie1.put("durataTest", studentSnapshot.child(numeMaterie).child("durataTest").getValue(String.class));
                            lectie1.put("durataTest1", studentSnapshot.child(numeMaterie).child("durataTest1").getValue(String.class));

                            lectie1.put("incercari", studentSnapshot.child(numeMaterie).child("incercari").getValue(Integer.class));
                            lectie1.put("incercari1", studentSnapshot.child(numeMaterie).child("incercari1").getValue(Integer.class));
                            lectie1.put("testDat", studentSnapshot.child(numeMaterie).child("testDat").getValue(Boolean.class));
                            if (lectieSnapshot.exists()) {
                                JSONObject lectie = new JSONObject();
                                lectie.put("progres", lectieSnapshot.child("progres").getValue(Integer.class));
                                lectie.put("numeCapitol", lectieSnapshot.child("Nume").getValue(String.class));
                                lectie.put("activat", lectieSnapshot.child("activat").getValue(Boolean.class));
                                raspuns.put("Capitol1", lectie);
                            }
                            DataSnapshot lectieSnapshot1 = materieSnapshot.child("Capitlolul 2");
                            if (lectieSnapshot1.exists()) {
                                JSONObject lectie = new JSONObject();
                                lectie.put("progres", lectieSnapshot1.child("progres").getValue(Integer.class));
                                lectie.put("numeCapitol", lectieSnapshot1.child("Nume").getValue(String.class));
                                lectie.put("activat", lectieSnapshot1.child("activat").getValue(Boolean.class));
                                raspuns.put("Capitol2", lectie);
                            }

                            DataSnapshot lectieSnapshot2 = materieSnapshot.child("Capitlolul 3");
                            if (lectieSnapshot2.exists()) {
                                JSONObject lectie = new JSONObject();
                                lectie.put("progres", lectieSnapshot2.child("progres").getValue(Integer.class));
                                lectie.put("numeCapitol", lectieSnapshot2.child("Nume").getValue(String.class));
                                lectie.put("activat", lectieSnapshot2.child("activat").getValue(Boolean.class));
                                raspuns.put("Capitol3", lectie);
                            }

                            DataSnapshot lectieSnapshot3 = materieSnapshot.child("Capitlolul 4");
                            if (lectieSnapshot2.exists()) {
                                JSONObject lectie = new JSONObject();
                                lectie.put("activat", lectieSnapshot3.child("activat").getValue(Boolean.class));
                                lectie.put("progres", lectieSnapshot3.child("progres").getValue(Integer.class));
                                lectie.put("numeCapitol", lectieSnapshot3.child("Nume").getValue(String.class));
                                raspuns.put("Capitol4", lectie);
                            }
                            System.out.println("Json raspuns detalii test " + lectie1);
                            raspuns.put("Detalii_test", lectie1);
                            break;
                        }
                    }
                }
                latch.countDown();
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
        // System.out.println("Raspuns "+raspuns);
        return raspuns;
    }

    public void updateStare(JsonNode json, String nume, String mail) throws JsonProcessingException {
        CountDownLatch latch = new CountDownLatch(1);
        String lectie = json.get("numeLectie").asText();
        Boolean stare = json.get("activat").asBoolean();
        System.out.println("Mail " + mail);
        String mailProf = mail.split("_")[1];
        materii.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot studentSnapshot : dataSnapshot.getChildren()) {
                    String numeMaterie = nume.split("_")[0];
                    DataSnapshot materieSnapshot = studentSnapshot.child(numeMaterie);
                    System.out.println("mail prof " + mailProf);
                    System.out.println("mail prof 123" + materieSnapshot.child("mailProfesor").getValue(String.class));
                    System.out.println("mail " + mailProf.equals(materieSnapshot.child("mailProfesor").getValue(String.class)));
                    if (materieSnapshot.exists() && mailProf.equals(materieSnapshot.child("mailProfesor").getValue(String.class))) {
                        HashMap<String, Object> data = new HashMap<String, Object>();
                        data.put("activat", stare);
                        DataSnapshot lectieeSnapshot = materieSnapshot.child(lectie);
                        System.out.println(lectieeSnapshot.getRef());
                        lectieeSnapshot.getRef().updateChildren(data, null);
                    }

                }
                latch.countDown();
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
    }

    public String takeYear(String gmail, String nume) {
        CountDownLatch latch = new CountDownLatch(2);
        JSONObject raspuns = new JSONObject();
        JSONObject lectie1 = new JSONObject();
        ArrayList<String> materiii = new ArrayList<String>();
        materiii.add("Fizică");
        materiii.add("Matematică");
        materiii.add("Istorie");
        materiii.add("Geografie");
        materiii.add("Limba si literatura romana");
        materiii.add("Limba engleza");
        final String[] an = {""};

        System.out.println("Materei " + nume);
        materii.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot studentSnapshot : dataSnapshot.getChildren()) {
                    String mail = studentSnapshot.child("mailElev").getValue(String.class);
                    mail = mail.replace(',', '.');
                    if (nume.equals("elev")) {
                        System.out.println("Da 142");
                        if (gmail.equals(mail)) {
                            System.out.println("Da 132");
                            an[0] = studentSnapshot.child("an").getValue(String.class);
                            System.out.println("AN 1" + an[0]);
                            break;
                        }
                    } else if (nume.equals("profesor")) {
                        an[0] = "";
                        for (DataSnapshot profesorSnapshot : dataSnapshot.getChildren()) {
                            for (int i = 0; i < materiii.size(); i++) {
                                String mailProf = profesorSnapshot.child(materiii.get(i)).child("mailProfesor").getValue(String.class);
                                if (mailProf.equals(gmail)) {
                                    System.out.println(" " + profesorSnapshot.child("an").getValue(String.class));
                                    String materia;
                                    if (materiii.get(i).equals("Limba si literatura romana")) {
                                        materia = "Limbasiliteraturaromana";
                                    } else if (materiii.get(i).equals("Limba engleza")) {
                                        materia = "Limbaengleza";
                                    } else {
                                        materia = materiii.get(i);
                                    }
                                    an[0] += materia + "_" + profesorSnapshot.child("an").getValue(String.class) + " ";
                                }
                            }
                        }
                    }
                }
                latch.countDown();

            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.out.println("Eroare la extragerea cursurilor: " + error.getMessage());
                latch.countDown();
            }
        });

        try {
            latch.await(3, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        return an[0];
    }

    public JSONObject takeStatistic(String gmail, String tip) {
        CountDownLatch latch = new CountDownLatch(1);
        final int[] nr = {1};
        JSONObject data = new JSONObject();
        ArrayList<String> materii1 = new ArrayList<String>();
        materii1.add("Fizică");
        materii1.add("Matematică");
        materii1.add("Istorie");
        materii1.add("Geografie");
        materii1.add("Limba si literatura romana");
        materii1.add("Limba engleza");
        HashMap<String, Object> numesiprenume= new HashMap<String, Object>();
        materii.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (tip.equals("statisticiProfesor")) {
                    for (DataSnapshot studentSnapshot : dataSnapshot.getChildren()) {
                        for (int i = 0; i < materii1.size(); i++) {
                            String mailProf = studentSnapshot.child(materii1.get(i)).child("mailProfesor").getValue(String.class);
                            if (!mailProf.equals("") && mailProf.equals(gmail)) {
                                HashMap<String, Object> data1 = new HashMap<String, Object>();
                                data1.put("materie", materii1.get(i));
                                data1.put("mail", studentSnapshot.child("mailElev").getValue(String.class));
                                String mail = studentSnapshot.child("mailElev").getValue(String.class);

                                data1.put("nume", numesiprenume.get(mail));
                                data1.put("progres1", studentSnapshot.child(materii1.get(i)).child("Capitlolul 1").child("progres").getValue(Integer.class));
                                data1.put("progres2", studentSnapshot.child(materii1.get(i)).child("Capitlolul 2").child("progres").getValue(Integer.class));
                                data1.put("progres3", studentSnapshot.child(materii1.get(i)).child("Capitlolul 3").child("progres").getValue(Integer.class));
                                data1.put("progres4", studentSnapshot.child(materii1.get(i)).child("Capitlolul 4").child("progres").getValue(Integer.class));
                                data.put("elev" + nr[0], data1);
                                nr[0]++;
                            }
                        }
                    }

                } else if (tip.equals("statisticiElev")) {
                    for (DataSnapshot studentSnapshot : dataSnapshot.getChildren()) {
                        String mailProf = studentSnapshot.child("mailElev").getValue(String.class);
                        for (int i = 0; i < materii1.size(); i++) {
                            if (!mailProf.equals("") && mailProf.equals(gmail)) {
                                HashMap<String, Object> data1 = new HashMap<String, Object>();

                                data1.put("mail", studentSnapshot.child("mailElev").getValue(String.class));
                                data1.put("progres1", studentSnapshot.child(materii1.get(i)).child("Capitlolul 1").child("progres").getValue(Integer.class));
                                data1.put("progres2", studentSnapshot.child(materii1.get(i)).child("Capitlolul 2").child("progres").getValue(Integer.class));
                                data1.put("progres3", studentSnapshot.child(materii1.get(i)).child("Capitlolul 3").child("progres").getValue(Integer.class));
                                data1.put("progres4", studentSnapshot.child(materii1.get(i)).child("Capitlolul 4").child("progres").getValue(Integer.class));

                                data.put(materii1.get(i), data1);
                            }
                        }
                    }
                }
                latch.countDown();
            }


            @Override
            public void onCancelled(DatabaseError error) {
                System.out.println("Eroare la extragerea cursurilor: " + error.getMessage());
                latch.countDown();
            }
        });

        try {
            latch.await(9, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return data;

    }
}
