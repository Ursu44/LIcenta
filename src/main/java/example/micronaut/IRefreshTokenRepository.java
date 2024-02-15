package example.micronaut;

import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.Optional;

public interface IRefreshTokenRepository {

        void save(String username, Boolean revoked, String refreshToken, String role) throws NoSuchAlgorithmException;
        Optional<RefreshTokenEntity> findByRefreshToken(String refreshToken);

        long updateByUsername(String username, boolean revoked);
}
