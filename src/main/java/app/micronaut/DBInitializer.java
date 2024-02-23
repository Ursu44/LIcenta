package app.micronaut;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import io.micronaut.context.annotation.Context;
import jakarta.inject.Singleton;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

@Context
@Singleton
public class DBInitializer {

    @PostConstruct
    private void initializeFirebase() {
        FileInputStream refreshToken = null;
        try {
            refreshToken = new FileInputStream("/home/student/Licenta/DataBase/firebase.json");

            GoogleCredentials credentials = GoogleCredentials.fromStream(refreshToken);
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(credentials)
                    .setDatabaseUrl("https://learnhub-f5ef3-default-rtdb.firebaseio.com")
                    .build();
            FirebaseApp.initializeApp(options);
        }
        catch (FileNotFoundException e) {

            throw new RuntimeException(e);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
