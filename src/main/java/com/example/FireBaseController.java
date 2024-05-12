package com.example;

import com.example.firebase.FirebaseInitializer;
import com.example.model.Profesor;
import com.example.model.Student;
import com.example.repository.AbstractFirebasRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micronaut.http.annotation.*;
import jakarta.inject.Inject;

import java.nio.charset.StandardCharsets;

@Controller("/firebase")
public class FireBaseController {

    private ObjectMapper objectMapper;
    private AbstractFirebasRepository studentRepository = null;

    @Inject
    private FirebaseInitializer firebaseInitializer;
    @Inject
    public FireBaseController(AbstractFirebasRepository studentRepository ) {
            this.studentRepository = studentRepository;
    }

    /*@Get("/get/{type1}")
    public void getCollection(@PathVariable String type1){
        if("profesor".equals(type1)) {
            profesorRepository.read();
        }
        else if("student".equals(type1)){
            studentRepository.read();
        }

    }*/
    @Post("/add/{type}")
    public void createEntity(@PathVariable String type, @Body String json)  {
        if ("profesor".equals(type) ) {
            Profesor profesor = Profesor.fromJson(json);
        } else if ("student".equals(type) ) {
            Student student = Student.fromJson(json);
            studentRepository.create(student);
        }
    }

    @Put("/update/{identifier}")
    public void updateEntity(@PathVariable String identifier, @Body String json){
        Student student = Student.fromJson(json);
    }


    @Get("/activationLink/activate/{token}/")
    public void updateForConfirmation(@PathVariable String token){
        try {
            String decodedToken = java.net.URLDecoder.decode(token, StandardCharsets.UTF_8);
            studentRepository.updateConfirmation(decodedToken);
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }


}