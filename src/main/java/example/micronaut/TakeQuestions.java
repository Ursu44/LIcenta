package example.micronaut;

import com.fasterxml.jackson.databind.JsonNode;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import io.micronaut.http.annotation.Get;
import jakarta.inject.Inject;
import org.bson.Document;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class TakeQuestions {

    @Inject
    Connection connection;

    public String take(JsonNode json){

        String capitole = json.get("capitole").textValue();
        System.out.println("Capitole  "+capitole);
        String an1 = json.get("an").textValue();
        System.out.println("An "+an1);
        String materie = json.get("materie").textValue();
        System.out.println("Materie "+materie);

        String capitol1 = "Capitol"+capitole.split("_")[0];
        String capitol2 = "Capitol"+capitole.split("_")[1];
        System.out.println("Captol 1 "+capitol1);
        System.out.println("Captol 2 "+capitol2);

        String rutaCaptol1 = an1+"."+materie+"."+capitol1;
        String rutaCaptol2 = an1+"."+materie+"."+capitol2;

        int index = 0;

        MongoClientSettings settings = connection.getSettings();
        try (MongoClient mongoClient = MongoClients.create(settings)) {
            MongoDatabase database = mongoClient.getDatabase("test");
            MongoCollection<Document> collection = database.getCollection("IntrebariTest");


            JSONObject jsonObject1 = new JSONObject();
            List<Document> pipeline = Arrays.asList(
                    new Document("$project", new Document(rutaCaptol1, 1))
            );

            List<Document> results = new ArrayList<>();
            collection.aggregate(pipeline).into(results);

            for (Document document : results) {
                Document an = (Document) document.get(an1);
                System.out.println("an " + an);
                if (an != null) {
                    Document materia = (Document) an.get(materie);
                    Document capitol = (Document) materia.get(capitol1);
                    int lungimeLectie = capitol.size();
                    for (int i = 0; i < lungimeLectie - 1; i++) {
                        int j = i + 1;
                        String bank1 = "bank" + j;
                        System.out.println("Bank " + bank1);
                        Random rand = new Random();
                        Document bank2 = (Document) capitol.get(bank1);
                        int bankSize = bank2.size();
                        int randomPoz = rand.nextInt(bankSize + 1);

                        Document intrebare = ((ArrayList<Document>) bank2.get("intrebari")).get(randomPoz);
                        Object punctajValue =intrebare.get("punctaj");
                        double punctaj = 0;
                        JSONObject jsonObject = new JSONObject();
                        if(punctajValue instanceof Integer){
                            punctaj = ((Integer) punctajValue).doubleValue();
                            jsonObject.put("punctaj", punctaj);
                        }else if(punctajValue instanceof Double){
                            punctaj = (double) punctajValue;
                        }
                        System.out.println("Intrebare "+intrebare);
                        String intrbarePus = intrebare.getString("intrebare");
                        String tip = intrebare.getString("tip");
                        //String variante = intrebare.getString("variante");
                        index++;
                        String intrebareIndex = "Intrebare" + index;
                        System.out.println("Index 1" +j);
                        jsonObject.put("intrebare", intrbarePus);
                        jsonObject.put("tip", tip);
                        jsonObject.put("punctaj", punctaj);
                        jsonObject.put("variante", intrebare.get("variante", List.class));
                        jsonObject1.put(intrebareIndex, jsonObject);

                    }
                    System.out.println("Rezultat final " + jsonObject1);
                }
            }


            List<Document> pipeline1 = Arrays.asList(
                    new Document("$project", new Document(rutaCaptol2, 1))
            );

            List<Document> results1 = new ArrayList<>();
            collection.aggregate(pipeline1).into(results1);

            for (Document document : results1) {
                Document an = (Document) document.get(an1);
                System.out.println("an " + an);
                if (an != null) {
                    Document materia = (Document) an.get(materie);
                    Document capitol = (Document) materia.get(capitol2);
                    int lungimeLectie = capitol.size();
                    for (int i = 0; i < lungimeLectie - 1; i++) {
                        int j = i + 1;
                        String bank1 = "bank" + j;
                        System.out.println("Bank " + bank1);
                        Random rand = new Random();
                        Document bank2 = (Document) capitol.get(bank1);
                        int bankSize = bank2.size();
                        int randomPoz = rand.nextInt(bankSize + 1);

                        Document intrebare = ((ArrayList<Document>) bank2.get("intrebari")).get(randomPoz);
                        String intrbarePus = intrebare.getString("intrebare");
                        String tip = intrebare.getString("tip");
                        Object punctajValue =intrebare.get("punctaj");
                        double punctaj = 0;
                        JSONObject jsonObject = new JSONObject();
                        if(punctajValue instanceof Integer){
                            punctaj = ((Integer) punctajValue).doubleValue();
                            jsonObject.put("punctaj", punctaj);
                        }else if(punctajValue instanceof Double){
                            punctaj = (double) punctajValue;
                        }
                        //String variante = intrebare.getString("variante");
                        index++ ;
                        System.out.println("Index 2" +index);
                        String intrebareIndex = "Intrebare" + index;
                        jsonObject.put("intrebare", intrbarePus);
                        jsonObject.put("tip", tip);
                        jsonObject.put("punctaj", punctaj);
                        jsonObject.put("variante", intrebare.get("variante", List.class));
                        jsonObject1.put(intrebareIndex, jsonObject);

                    }
                    System.out.println("Rezultat final " + jsonObject1);
                }
            }

            //System.out.println("da da da da"+results);
            return jsonObject1.toJSONString();
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to insert document: " + e.getMessage();
        }
    }
}
