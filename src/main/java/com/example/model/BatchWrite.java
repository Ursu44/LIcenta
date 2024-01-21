package com.example.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.SerdeImport;
import io.micronaut.serde.annotation.Serdeable;

import java.security.NoSuchAlgorithmException;

@Introspected
@SerdeImport(Student.class)
@JsonPropertyOrder({"id","email"})
@Serdeable.Serializable
public class BatchWrite {
    @JsonProperty("id")
    private String id;

    @JsonProperty("email")
    private String email;

    @JsonCreator
    public BatchWrite(
            @JsonProperty("id") String id,
            @JsonProperty("email") String email
    ) throws NoSuchAlgorithmException {

        this.id = id;
        this.email = email;
    }

    public void setId(String id){
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}
