package example.micronaut;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.database.*;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.SerdeImport;
import io.micronaut.serde.annotation.Serdeable;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.concurrent.CountDownLatch;

@Introspected
@SerdeImport(Profesor.class)
@JsonPropertyOrder({"nume","prenume","mail","parola","materie", "rol"})
@Serdeable.Serializable
public class Profesor {

    private Hashing hasher = new Hashing();
    @JsonProperty("nume")
    private String nume;
    @JsonProperty("prenume")
    private String prenume;
    @JsonProperty("mail")
    private String mail;
    @JsonProperty("parola")
    private String parola;
    @JsonProperty("materie")
    private String materie;
    @JsonProperty("rol")
    private String rol;
    @JsonProperty("grupe")
    private String grupe;

    @JsonProperty("materii")
    private HashMap<String, Object> materii;

    @JsonCreator
    public Profesor(
            @JsonProperty("nume") String nume,
            @JsonProperty("prenume") String prenume,
            @JsonProperty("mail") String mail,
            @JsonProperty("parola") String parola,
            @JsonProperty("materie") String materie,
            @JsonProperty("rol")  String rol,
            @JsonProperty("grupe") String grupe,
            @JsonProperty("materii") HashMap<String, Object> materii
    ) throws NoSuchAlgorithmException {
        this.nume = nume;
        this.prenume = prenume;
        this.mail = mail;
        this.parola = hasher.toHexString(hasher.getSHA(parola));
        this.materie = materie;
        this.rol =rol;
        this.grupe = grupe;
        this.materii = materii;
    }

    public void setNume(String nume){
        this.nume = nume;
    }

    public String getNume(){
        return nume;
    }

    public void setPrenume(String prenume){
        this.prenume = prenume;
    }

    public String getPrenume(){
        return prenume;
    }

    public void setMail(String mail){
        this.mail = mail;
    }

    public String getMail(){
        return mail;
    }

    public void setParola(String parola) throws NoSuchAlgorithmException {
        this.parola = hasher.toHexString(hasher.getSHA(parola));;
    }

    public String getParola(){
        return parola;
    }

    public void setMaterie(String materie){
        this.materie = materie;
    }

    public String getMaterie(){
        return materie;
    }

    public String getRol() {return rol;}

    public void setRol(String rol) {
        this.rol = rol;
    }

    public void setGrupe(String grupe) {
        this.grupe = grupe;
    }

    public String getGrupe(){
        return grupe;
    }

    public void setMaterii(HashMap<String, Object> materii) {
        this.materii = materii;
    }

    public HashMap<String, Object>  getMaterii(){
        return materii;
    }

    public static Profesor fromJson(String json) {
        try {
            Profesor student = new ObjectMapper().readValue(json, Profesor.class);
            return new ObjectMapper().readValue(json, Profesor.class);
        } catch (IOException e) {
            return null;
        }
    }
}
