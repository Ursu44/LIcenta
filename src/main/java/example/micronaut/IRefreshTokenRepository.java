package example.micronaut;

import java.security.NoSuchAlgorithmException;
import java.util.Optional;

public interface IRefreshTokenRepository {

        void save(String username, Boolean revoked, String refreshToken) throws NoSuchAlgorithmException;
        Optional<RefreshTokenEntity> findByRefreshToken(String refreshToken);
}
