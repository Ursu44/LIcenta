package com.example.model;


import com.example.EncryptDecrypt;
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
import java.util.concurrent.CountDownLatch;

@Introspected
@SerdeImport(Student.class)
@JsonPropertyOrder({"nume","prenume","mail","parola"})
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

    @JsonCreator
    public Student(
            @JsonProperty("nume") String nume,
            @JsonProperty("prenume") String prenume,
            @JsonProperty("mail") String mail,
            @JsonProperty("parola") String parola
    ) throws NoSuchAlgorithmException {

        this.nume = nume;
        this.prenume = prenume;
        this.mail = mail;
        this.parola = encryptor.toHexString(encryptor.getSHA(parola));
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
        this.parola = this.parola = encryptor.toHexString(encryptor.getSHA(parola));
    }

    public String getParola(){
        return parola;
    }

    public static Student fromJson(String json) {
        try {
            Student student = new ObjectMapper().readValue(json, Student.class);
            if (!isUnique(student.getMail())) {
                throw new IllegalArgumentException("Mail-ul exista deja in baza de date " + student.getMail());
            }
            return new ObjectMapper().readValue(json, Student.class);
        } catch (IOException e) {
            return null;
        }
    }
    public static boolean isUnique(String mailCheck) {
        final boolean[] ok = {true};
        DatabaseReference student = FirebaseDatabase.getInstance().getReference("Studenti");

        CountDownLatch latch = new CountDownLatch(1);
        student.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String email = snapshot.child("mail").getValue(String.class);
                    if (email != null && email.equals(mailCheck)) {
                        ok[0] = false;
                        break;
                    }
                }
                latch.countDown();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                ok[0] = false;
                latch.countDown();
            }
        });

        try {
            latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        return ok[0];
    }





}
