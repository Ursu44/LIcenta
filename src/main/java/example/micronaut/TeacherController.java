package example.micronaut;

import io.micronaut.http.annotation.*;
import jakarta.inject.Inject;

@Controller("/add")
public class TeacherController {

    private  Repository profesorRepository = null;

    @Inject
    public TeacherController(Repository profesorRepository) {
        this.profesorRepository = profesorRepository;
    }

    @Post("/profesor")
    public void createEntity(@Body String json)  {
        Profesor profesor = Profesor.fromJson(json);
        profesorRepository.create(profesor);
    }
}
