package example.micronaut;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.SerdeImport;
import io.micronaut.serde.annotation.Serdeable;

import java.security.NoSuchAlgorithmException;

@Introspected
@SerdeImport(RefreshTokenEntity.class)
@JsonPropertyOrder({"username","revoked","refreshToken"})
@Serdeable.Serializable
public class RefreshTokenEntity {

    @JsonProperty("username")
    private String username;

    @JsonProperty("revoked")
    private Boolean revoked;

    @JsonProperty("refreshToken")
    private String refreshToken;


    @JsonCreator
    public RefreshTokenEntity(
            @JsonProperty("username") String username,
            @JsonProperty("revoked") Boolean revoked,
            @JsonProperty("refreshToken") String refreshToken
    ) throws NoSuchAlgorithmException {
        this.username = username;
        this.revoked = revoked;
        this.refreshToken = refreshToken;
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
}
