package muse_kopis.muse.auth.jwt;

import static java.util.concurrent.TimeUnit.DAYS;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import java.util.Date;
import muse_kopis.muse.common.UnAuthorizationException;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

    private final long accessTokenExpirationDayToMills;
    private final Algorithm algorithm;

    public JwtService(JwtProperty jwtProperty) {
        this.accessTokenExpirationDayToMills = MILLISECONDS.convert(jwtProperty.accessTokenExpirationDay(), DAYS);
        this.algorithm = Algorithm.HMAC256(jwtProperty.secretKey());
    }

    public String createToken(String memberId) {
        return JWT.create()
                .withExpiresAt(new Date(
                            System.currentTimeMillis() + accessTokenExpirationDayToMills
                        ))
                .withIssuedAt(new Date())
                .withClaim("memberId", memberId)
                .sign(algorithm);
    }

    public Object extractMemberId(String token) {
        try{
            return JWT.require(algorithm)
                    .build()
                    .verify(token)
                    .getClaim("memberId")
                    .asString();
        } catch (JWTVerificationException e) {
            throw new UnAuthorizationException("유효하지 않은 토큰입니다.");
        }
    }
}
