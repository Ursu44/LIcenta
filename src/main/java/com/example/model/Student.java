package com.example.model;


import com.example.EncryptDecrypt;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.SerdeImport;
import io.micronaut.serde.annotation.Serdeable;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

@Introspected
@SerdeImport(Student.class)
@JsonPropertyOrder({"nume","prenume","mail","parola", "rol"})
@Serdeable.Serializable
public class Student {

    private EncryptDecrypt encryptor = new EncryptDecrypt();
    @JsonProperty("nume")
    private String nume;

    @JsonProperty("prenume")
    private String prenume;

    @JsonProperty("mail")
    private String mail;

    @JsonProperty("parola")
    private String parola;
    @JsonProperty("rol")
    private String rol;

    @JsonProperty("an")
    private String an;

    @JsonProperty("grupa")
    private String grupa;

    @JsonCreator
    public Student(
            @JsonProperty("nume") String nume,
            @JsonProperty("prenume") String prenume,
            @JsonProperty("mail") String mail,
            @JsonProperty("parola") String parola,
            @JsonProperty("an") String an,
            @JsonProperty("grupa") String grupa
    ) throws NoSuchAlgorithmException {
        this.nume = nume;
        this.prenume = prenume;
        this.mail = mail;
        this.parola = encryptor.toHexString(encryptor.getSHA(parola));
        this.an =an;
        this.grupa =grupa;
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

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getMail(){
        return mail;
    }

    public void setGrupa(String grupa){
        this.grupa = grupa;
    }
    public String getGrupa() {
        return grupa;
    }

    public void setAn(String an){
        this.an = an;
    }
    public String getAn() {
        return an;
    }


    public void setParola(String parola) throws NoSuchAlgorithmException {
        this.parola = this.parola = encryptor.toHexString(encryptor.getSHA(parola));
    }

    public String getParola(){
        return parola;
    }

    public static Student fromJson(String json) {
        try {
            Student student = new ObjectMapper().readValue(json, Student.class);
            return new ObjectMapper().readValue(json, Student.class);
        } catch (IOException e) {
            return null;
        }
    }




}
