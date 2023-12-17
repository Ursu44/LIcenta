package example.micronaut;

import java.util.Optional;

public interface IRefreshTokenRepository {

        void save(String username, Boolean revoked, String refreshToken);
        Optional<RefreshTokenEntity> findByRefreshToken(String refreshToken);

}
