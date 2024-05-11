package example.micronaut;

import io.micronaut.http.annotation.*;
import jakarta.inject.Inject;

import java.nio.charset.StandardCharsets;

@Controller("/add")
public class TeacherController {

    private  Repository profesorRepository = null;

    @Inject
    public TeacherController(Repository profesorRepository) {
        this.profesorRepository = profesorRepository;
    }

    @Post("/profesor")
    public void createEntity(@Body String json)  {
        System.out.println("Rezultat "+json);
        Profesor profesor = Profesor.fromJson(json);
        profesorRepository.create(profesor);
    }
    @Get("/activationLink/activate/{token}/")
    public void updateForConfirmation(@PathVariable String token){
        try {
            String decodedToken = java.net.URLDecoder.decode(token, StandardCharsets.UTF_8);
            profesorRepository.updateConfirmation(decodedToken);
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

}
