package com.example;

import com.example.firebase.FirebaseInitializer;
import com.example.model.Profesor;
import com.example.model.Student;
import com.example.repository.FireBaseRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micronaut.http.annotation.*;
import jakarta.inject.Inject;

@Controller("/firebase")
public class FireBaseController {

    private ObjectMapper objectMapper;
    private  FireBaseRepository<Profesor> profesorRepository = null;
    private  FireBaseRepository<Student> studentRepository = null;

    @Inject
    private FirebaseInitializer firebaseInitializer;


    @Inject
    public FireBaseController(FireBaseRepository<Profesor> profesorRepository, FireBaseRepository<Student> studentRepository ) {
            this.profesorRepository = profesorRepository;
            this.studentRepository = studentRepository;
    }

    @Get("/get/{type1}")
    public void getCollection(@PathVariable String type1){
        if("profesor".equals(type1)) {
            profesorRepository.read();
        }
        else if("student".equals(type1)){
            studentRepository.read();
        }

    }

    @Post("/add/{type}")
    public void createEntity(@PathVariable String type, @Body String json)  {
        if ("profesor".equals(type) ) {
            Profesor profesor = Profesor.fromJson(json);
            profesorRepository.create(profesor);
        } else if ("student".equals(type) ) {
            Student student = Student.fromJson(json);
            studentRepository.create(student);
        }
    }

    @Put("/update/{identifier}")
    public void updateEntity(@PathVariable String identifier, @Body String json){
        Student student = Student.fromJson(json);
        studentRepository.update(student, identifier);
    }


}