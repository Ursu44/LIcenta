package example.micronaut;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Singleton;

@Singleton
public class Conectare {

    private MongoClientSettings settings;

    @PostConstruct
    public void conectez() {
        String connectionString = "mongodb+srv://cosminirimiaaursulesei:t9pam*JtJYk5M@cluster0.zfa9zls.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0";
        ServerApi serverApi = ServerApi.builder()
                .version(ServerApiVersion.V1)
                .build();
        settings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(connectionString))
                .serverApi(serverApi)
                .build();
    }

    public MongoClientSettings getSettings(){
        return settings;
    }

}