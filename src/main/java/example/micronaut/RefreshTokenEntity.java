package example.micronaut;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.SerdeImport;
import io.micronaut.serde.annotation.Serdeable;

import java.security.NoSuchAlgorithmException;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Introspected
@SerdeImport(RefreshTokenEntity.class)
@JsonPropertyOrder({"username","revoked","refreshToken", "dateCreated", "role"})
@Serdeable.Serializable
public class RefreshTokenEntity {

    @JsonProperty("username")
    private String username;

    @JsonProperty("revoked")
    private Boolean revoked;

    @JsonProperty("refreshToken")
    private String refreshToken;

    @JsonProperty("dateCreated")
    private String dateCreated;

    @JsonProperty("role")
    private String role;

    @JsonCreator
    public RefreshTokenEntity(
            @JsonProperty("username") String username,
            @JsonProperty("revoked") Boolean revoked,
            @JsonProperty("refreshToken") String refreshToken,
            @JsonProperty("role") String role)
            throws NoSuchAlgorithmException {
        this.username = username;
        this.revoked = revoked;
        this.refreshToken = refreshToken;
        this.role = role;
        String formattedDate = DateTimeFormatter.ISO_INSTANT.format((new Date()).toInstant());
        this.dateCreated = formattedDate;

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Boolean getRevoked() {
        return revoked;
    }

    public void setRevoked(Boolean revoked) {
        this.revoked = revoked;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("username", username);
        map.put("revoked", revoked);
        map.put("refreshToken", refreshToken);
        map.put("role", role);
        map.put("date created", dateCreated);
        return map;
    }
}
