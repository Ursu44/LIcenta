package example.micronaut;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.google.firebase.database.*;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import jakarta.inject.Inject;
import org.bson.Document;
import org.json.simple.JSONObject;

import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class VerifyAnswears  {

    @Inject
    private CorectareFirebase dbInitializer;

    private DatabaseReference materii = null;

    @Inject
    Conectare conectare;

    @Inject
    public VerifyAnswears() {
        materii = FirebaseDatabase.getInstance().getReference("Catalog");
    }

    public String corecteaza(JsonNode date,JsonNode intrebari){

        System.out.println("An "+date.get("an").asText());
        String an1 = date.get("an").asText();
        System.out.println("Materie "+date.get("materie").asText());
        String materie = date.get("materie").asText();
        System.out.println("Materie "+date.get("capitole").asText());
        String capitole = date.get("capitole").asText();

        String capitol1 = "Capitol"+capitole.split("_")[0];
        String capitol2 = "Capitol"+capitole.split("_")[1];
        System.out.println("Captol 1 "+capitol1);
        System.out.println("Captol 2 "+capitol2);

        //Iterator<Map.Entry<String, JsonNode>> fields = intrebari.fields();

        int index = 0;

        /*while(fields.hasNext()) {
            Map.Entry<String, JsonNode> field = fields.next();
            String   fieldName  = field.getKey();
            ArrayNode fieldValue = (ArrayNode) field.getValue();

            System.out.println("Intrebare: " + fieldName);
            System.out.println("Intrebare 123: " + fieldValue);
        }*/

        if(materie.equals("Matematică")){
            materie = "Matematica";
        }

        if(materie.equals("Istorie")){
            materie = "Isotrie";
        }

        if(materie.equals("Limba engleza")){
            materie = "Limbaengleza";
        }
        if(materie.equals("Limba si literatura romana")){
            materie = "Limbasiliteraturaromana";
        }

        String rutaCaptol1 = an1+"."+materie+"."+capitol1;
        String rutaCaptol2 = an1+"."+materie+"."+capitol2;
        String finalMaterie = materie;
         double[] punctaj = {0};
         double[] punctajFinal = {0};
        double[] punctajFinal1 = {0};
        MongoClientSettings settings = conectare.getSettings();
        try (MongoClient mongoClient = MongoClients.create(settings)) {
            MongoDatabase database = mongoClient.getDatabase("test");
            MongoCollection<Document> collection = database.getCollection("IntrebariTest");

            JSONObject jsonObject1 = new JSONObject();
            List<Document> pipeline = Arrays.asList(
                    new Document("$project", new Document(rutaCaptol1, 1))
            );

            List<Document> results = new ArrayList<>();
            collection.aggregate(pipeline).into(results);

            Thread asyncTask1 = new Thread(() -> {
                for (Document document : results) {
                    Document an = (Document) document.get(an1);
                    //System.out.println("an " + an);
                    if (an != null) {
                        Document materia = (Document) an.get(finalMaterie);
                        Document capitol = (Document) materia.get(capitol1);
                        int lungimeLectie = capitol.size();
                        for (int i = 0; i < lungimeLectie - 1; i++) {
                            int j = i + 1;
                            String bank1 = "bank" + j;
                            //System.out.println("Bank " + bank1);
                            Random rand = new Random();
                            Document bank2 = (Document) capitol.get(bank1);
                            int bankSize = bank2.size();
                            //int randomPoz = rand.nextInt(bankSize + 1);

                            //Document intrebare = ((ArrayList<Document>) bank2.get("intrebari")).get(randomPoz);
                            int lungimeIntrebari = ((ArrayList<Document>) bank2.get("intrebari")).size();
                            for(int l = 0;l<lungimeIntrebari ;l++) {
                                Document intrebare = ((ArrayList<Document>) bank2.get("intrebari")).get(l);

                                String intrbarePus = intrebare.getString("intrebare");
                                //System.out.println("Intrebare123: " + intrbarePus);

                                double punctajIntrebare = 0 ;
                                Iterator<Map.Entry<String, JsonNode>> fields = intrebari.fields();
                                while(fields.hasNext()) {
                                    Map.Entry<String, JsonNode> field = fields.next();
                                    String   fieldName  = field.getKey();
                                    ArrayNode fieldValue = (ArrayNode) field.getValue();
                                    //System.out.println("Raspunsuri mongo "+raspuns);
                                    //System.out.println("Intrebare123: " + fieldName);
                                    //System.out.println("Intrebare: " + intrbarePus);
                                    double punctajProvizoriu =0.0;
                                    double punctajProvizoriu1 =0.0;
                                    if(intrbarePus.equals(fieldName)){
                                        Object punctajValue = intrebare.get("punctaj");
                                        String tip = intrebare.getString("tip");
                                        //System.out.println("Intrebare "+intrbarePus);
                                        //System.out.println("Tip intrebare "+tip);
                                        ArrayList<String> raspuns1 = (ArrayList<String>) intrebare.get("raspuns", List.class);
                                        //System.out.println("Raspunsuri mongo "+raspuns1);
                                        if (punctajValue instanceof Integer) {
                                            punctaj[0] = ((Integer) punctajValue).doubleValue();
                                        } else if (punctajValue instanceof Double) {
                                            punctaj[0] = (double) punctajValue;
                                        }
                                        System.out.println("Punctaj "+punctaj[0]);
                                        //System.out.println("Am intrat in verificare");
                                        for(int k=0;k<fieldValue.size(); k++){
                                            ArrayList<String> raspuns = (ArrayList<String>) intrebare.get("raspuns", List.class);
                                            //System.out.println("Raspunsuri mongo "+raspuns);
                                            //System.out.println("Raspuns primit "+fieldValue.get(k));
                                            String varianta = fieldValue.get(k).toString();
                                            String raspsunsString = raspuns.toString();
                                           // System.out.println(raspsunsString.contains(varianta));
                                            System.out.println("Intrebare de pus "+intrbarePus);
                                            if(tip.equals("multiplu") || punctaj[0]==0.33){
                                                if (raspsunsString.contains(varianta) || punctaj[0]==0.33) {
                                                    punctajProvizoriu += punctaj[0];
                                                    System.out.println("Punctaj adaugat varaiana multipla "+intrbarePus+" "+ punctajProvizoriu);
                                                } else{
                                                    punctajProvizoriu1 +=  punctaj[0];
                                                    System.out.println("Punctaj scadat varaiana multipla1 "+intrbarePus+" "+ punctajProvizoriu);
                                                }

                                                if(punctajProvizoriu < 0){
                                                    punctajProvizoriu =0;
                                                }
                                            }
                                            else {
                                                if (raspsunsString.contains(varianta)) {
                                                    punctajProvizoriu += punctaj[0];
                                                    //System.out.println("Punctaj intrbare gasita 4 "+ punctajFinal1[0]);
                                                }
                                            }
                                        }
                                        // System.out.println("Pentru intrebarea "+intrbarePus+ " s-a pus "+punctajProvizoriu);
                                        if(punctajProvizoriu - punctajProvizoriu1<0){
                                            double punctaj12 =punctajProvizoriu - punctajProvizoriu1;
                                            punctajFinal[0] += 0;
                                            System.out.println("Pentru intrebarea1 "+intrbarePus+ " s-a pus "+punctaj12);
                                        }else {
                                            double punctaj12 =punctajProvizoriu - punctajProvizoriu1;
                                            punctajFinal[0] += punctaj12;
                                            System.out.println("Pentru intrebarea 2"+intrbarePus+ " s-a pus "+punctaj12);
                                        };
                                    }
                                   // System.out.println("Intrebare 123: " + fieldValue);
                                }

                                //punctajFinal[0] += punctajIntrebare;
                            }
                            //System.out.println("Intrebare "+intrebare);

                            //String variante = intrebare.getString("variante");
                            // System.out.println("Index 1" +j);
                            //jsonObject.put("intrebare", intrbarePus);
                            // jsonObject.put("tip", tip);
                            // jsonObject.put("punctaj", punctaj);
                            //jsonObject.put("variante", intrebare.get("variante", List.class));
                            // jsonObject1.put(intrebareIndex, jsonObject);

                        }
                        //System.out.println("Rezultat final " + jsonObject1);
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });


            List<Document> pipeline1 = Arrays.asList(
                    new Document("$project", new Document(rutaCaptol2, 1))
            );

            List<Document> results1 = new ArrayList<>();
            collection.aggregate(pipeline1).into(results1);
            Thread asyncTask = new Thread(() -> {
            for (Document document : results1) {
                Document an = (Document) document.get(an1);
                //System.out.println("an " + an);
                if (an != null) {
                    Document materia = (Document) an.get(finalMaterie);
                    Document capitol = (Document) materia.get(capitol2);
                    int lungimeLectie = capitol.size();
                    for (int i = 0; i < lungimeLectie - 1; i++) {
                        int j = i + 1;
                        String bank1 = "bank" + j;
                        //System.out.println("Bank " + bank1);
                        Random rand = new Random();
                        Document bank2 = (Document) capitol.get(bank1);
                        int bankSize = bank2.size();
                        //int randomPoz = rand.nextInt(bankSize + 1);

                        //Document intrebare = ((ArrayList<Document>) bank2.get("intrebari")).get(randomPoz);
                        int lungimeIntrebari = ((ArrayList<Document>) bank2.get("intrebari")).size();
                        //System.out.println("Punctaj intrbare gasita 3 "+ punctajFinal[0]);
                        for(int l = 0;l<lungimeIntrebari ;l++) {
                            Document intrebare = ((ArrayList<Document>) bank2.get("intrebari")).get(l);

                            String intrbarePus = intrebare.getString("intrebare");
                            //System.out.println("Intrebare1234: " + intrbarePus);

                            double punctajIntrebare = 0 ;
                            //System.out.println("Intrebare "+intrbarePus+" "+punctaj[0]);
                            Iterator<Map.Entry<String, JsonNode>> fields = intrebari.fields();
                            while(fields.hasNext()) {
                                Map.Entry<String, JsonNode> field = fields.next();
                                String   fieldName  = field.getKey();
                                ArrayNode fieldValue = (ArrayNode) field.getValue();

                                //System.out.println("Raspunsuri mongo "+raspuns);
                                //System.out.println("Intrebare123: " + fieldName);
                                //System.out.println("Intrebare: " + intrbarePus);
                                double punctajProvizoriu =0;
                                double punctajProvizoriu1 =0;
                                if(intrbarePus.equals(fieldName)){
                                    Object punctajValue = intrebare.get("punctaj");
                                    String tip = intrebare.getString("tip");
                                    //System.out.println("Intrebare "+intrbarePus);
                                    //System.out.println("Tip intrebare "+tip);
                                    ArrayList<String> raspuns1 = (ArrayList<String>) intrebare.get("raspuns", List.class);
                                    //System.out.println("Raspunsuri mongo "+raspuns1);
                                    if (punctajValue instanceof Integer) {
                                        punctaj[0] = ((Integer) punctajValue).doubleValue();
                                    } else if (punctajValue instanceof Double) {
                                        punctaj[0] = (double) punctajValue;
                                    }
                                    System.out.println("Punctaj "+punctaj[0]);
                                    //System.out.println("Am intrat in verificare "+tip);
                                    for(int k=0;k<fieldValue.size(); k++){
                                        ArrayList<String> raspuns = (ArrayList<String>) intrebare.get("raspuns", List.class);
                                       // System.out.println("Raspunsuri mongo "+raspuns);
                                        //System.out.println("Raspuns primit "+fieldValue.get(k));
                                        String varianta = fieldValue.get(k).toString();
                                        String raspsunsString = raspuns.toString();
                                        //System.out.println(raspsunsString.contains(varianta));
                                        //System.out.println("Punctaj provizoriu "+punctajProvizoriu);
                                        System.out.println("Intrebare de pus "+intrbarePus);
                                        if(tip.equals("multiplu") || punctaj[0]==0.33){
                                            if (raspsunsString.contains(varianta) || punctaj[0]==0.33) {
                                                punctajProvizoriu += punctaj[0];
                                                System.out.println("Punctaj adaugat varaiana multipla "+intrbarePus+" "+ punctajProvizoriu);
                                            } else{
                                                punctajProvizoriu1 +=  punctaj[0];
                                                System.out.println("Punctaj scadat varaiana multipla1 "+intrbarePus+" "+ punctajProvizoriu1);
                                            }

                                            if(punctajProvizoriu < 0){
                                                punctajProvizoriu =0;
                                            }
                                        }
                                        else {
                                            if (raspsunsString.contains(varianta)) {
                                                punctajProvizoriu += punctaj[0];
                                                //System.out.println("Punctaj intrbare gasita 4 "+ punctajFinal1[0]);
                                            }
                                        }
                                    }
                                   // System.out.println("Pentru intrebarea "+intrbarePus+ " s-a pus "+punctajProvizoriu);
                                    if(punctajProvizoriu - punctajProvizoriu1<0){
                                        double punctaj12 =punctajProvizoriu - punctajProvizoriu1;
                                        System.out.println("Pentru intrebarea1 "+intrbarePus+ " s-a pus "+punctaj12+" "+punctajFinal[0]);
                                    }else {
                                        double punctaj12 =punctajProvizoriu - punctajProvizoriu1;
                                        punctajFinal[0] += punctaj12;
                                         System.out.println("Pentru intrebarea 2"+intrbarePus+ " s-a pus "+punctaj12+" "+punctajFinal[0]);

                                    }
                                }
                            }
                        }


                    }

                }
                try {
                    Thread.sleep(1100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            });

            //System.out.println("da da da da"+results);

            Thread asyncTask2 = new Thread(() -> {
                double finalP =punctajFinal[0]+ punctajFinal1[0];
                System.out.println("Punctaj test "+ finalP);

                try {
                    Thread.sleep(1200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });

            asyncTask.start();
            asyncTask1.start();
            asyncTask2.start();
            try {
                asyncTask.join();
                asyncTask1.join();
                asyncTask2.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            double finalP =punctajFinal[0]+ punctajFinal1[0]+1;
            if(finalP>10){
                finalP = 10.0;
            }
            DecimalFormat df = new DecimalFormat("0.00");
            return df.format(finalP);
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to insert document: " + e.getMessage();
        }
    }

    public void salvezNota(JsonNode date,String nota){
        CountDownLatch latch = new CountDownLatch(1);
        System.out.println("Nota "+nota);
        System.out.println("An "+date.get("an").asText());
        String an1 = date.get("an").asText();
        System.out.println("Materie "+date.get("materie").asText());
        String materie = date.get("materie").asText();
        System.out.println("Materie "+date.get("capitole").asText());
        String capitole = date.get("capitole").asText();
        System.out.println("Mail "+(date.get("mailElev").asText()).split("_")[1]);
        String mail = (date.get("mailElev").asText()).split("_")[1];
        String materie1 = "", tip="";

        if(capitole.equals("1_2")){
            tip ="Nota1";
        }
        else if(capitole.equals("3_4")) {
            tip = "Nota2";
        }


        if (materie.equals("Fizica")) {
            materie1 = "Fizică";
        }
        if (materie.equals("Matematica")) {
            materie1 =  "Matematică";
        }
        if (materie.equals("Limbasiliteraturaromana")) {
            materie1 =  "Limba si literatura romana";
        }
        if (materie.equals("Limbaengleza")) {
            materie1 =  "Limba engleza";
        }

        String finalTip = tip;
        String finalMaterie = materie1;
        materii.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot studentSnapshot : dataSnapshot.getChildren()) {
                    DataSnapshot dataSnapshot1 = studentSnapshot;
                    String elevEmail = dataSnapshot1.child("mailElev").getValue(String.class);
                    String elevNume = dataSnapshot1.child("nume").getValue(String.class);
                    String elevPrenume= dataSnapshot1.child("prenume").getValue(String.class);
                    String grupaElev = dataSnapshot1.child("grupa").getValue(String.class);
                    String anElev = dataSnapshot1.child("an").getValue(String.class);
                    elevNume = elevNume+" "+elevPrenume;
                    System.out.println(grupaElev);
                    if (elevEmail.equals(mail))   {
                        System.out.println("Pe aici nu se trece ");
                        Map<String, Object> data = new HashMap<String, Object>();
                        double notaDouble = Double.parseDouble(nota);
                        long notaRotunjita = Math.round(notaDouble);
                        data.put(finalTip, notaRotunjita);
                        studentSnapshot.child(finalMaterie).getRef().updateChildren(data, null);
                        break;
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

    public String prreiauGrafic(JsonNode date) throws InterruptedException {
        System.out.println("An " + date.get("an").asText());
        String an1 = date.get("an").asText();
        System.out.println("Materie " + date.get("materie").asText());
        String materie = date.get("materie").asText();
        System.out.println("Materie " + date.get("capitole").asText());
        String capitole = date.get("capitole").asText();
        System.out.println("Mail " + (date.get("mailElev").asText()).split("_")[1]);
        String mail = (date.get("mailElev").asText()).split("_")[1];
        String materie1 = "", tip = "";

        if (capitole.equals("1_2")) {
            tip = "Nota1";
        } else if (capitole.equals("3_4")) {
            tip = "Nota2";
        }

        if (materie.equals("Fizica")) {
            materie1 = "Fizică";
        }
        if (materie.equals("Matematica")) {
            materie1 = "Matematică";
        }
        if (materie.equals("Limbasiliteraturaromana")) {
            materie1 = "Limba si literatura romana";
        }
        if (materie.equals("Limbaengleza")) {
            materie1 = "Limba engleza";
        }

        String finalMaterie = materie1;
        String finalTip = tip;

        CountDownLatch latch = new CountDownLatch(1);
        CountDownLatch latch1 = new CountDownLatch(1);
        final int[] nr = {1};
        List<Map<String, Object>> resultList = new ArrayList<>();
        Map<String, Object> data1 = new HashMap<>();
            materii.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot studentSnapshot : dataSnapshot.getChildren()) {
                        String mailProfesor = studentSnapshot.child(finalMaterie).child("mailProfesor").getValue(String.class);
                        System.out.println("Mai " + mailProfesor);
                        //int nota = dataSnapshot.child(finalMaterie).child("Nota1").getValue(Integer.class);
                        //System.out.println("Nota " + nota);
                       // String mailElev = studentSnapshot.child("mailElev").getValue(String.class);
                        //System.out.println("Mail elev " + mailElev);
                        /*String nume = studentSnapshot.child("nume").getValue(String.class);
                        System.out.println("Nume " + nume);
                        String prenume = studentSnapshot.child("prenume").getValue(String.class);
                        System.out.println("Prenume " + prenume);
                        String grupa = studentSnapshot.child("grupa").getValue(String.class);
                        System.out.println("Grupa " + grupa);*/

                                if (mailProfesor.equals(mail)) {
                                    System.out.println("Pe aici nu se trece " + finalTip+ " "+finalMaterie);
                                    DataSnapshot materieSnapshot = studentSnapshot.child(finalMaterie);
                                    Integer nota = -1;
                                    if(materieSnapshot.exists()) {
                                        nota = materieSnapshot.child(finalTip).getValue(Integer.class);
                                    }

                                    String mailElev = studentSnapshot.child("mailElev").getValue(String.class);
                                    String nume = studentSnapshot.child("nume").getValue(String.class);
                                    String prenume = studentSnapshot.child("prenume").getValue(String.class);
                                    String grupa = studentSnapshot.child("grupa").getValue(String.class);

                                    Map<String, Object> data = new HashMap<>();
                                    data.put("nume", nume);
                                    data.put("prenume", prenume);
                                    data.put("grupa", grupa);
                                    data.put("mail", mailElev);
                                    data.put("nota", nota);


                                    data1.put("elev"+ nr[0], data);
                                    nr[0]++;
                                    System.out.println("Nota " + nota);
                                    System.out.println("Mail elev " + mailElev);
                                    System.out.println("Nume " + nume);
                                    System.out.println("Prenume " + prenume);
                                    System.out.println("Grupa " + grupa);
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
        String result = data1.toString();
        System.out.println("Raspuns pentru profesor 123" + result);

        return data1.toString();

    }
}
