package spharoom.unjeong.global.security.token;

import io.fusionauth.jwt.Verifier;
import io.fusionauth.jwt.domain.JWT;
import io.fusionauth.jwt.hmac.HMACSigner;
import io.fusionauth.jwt.hmac.HMACVerifier;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import spharoom.unjeong.appointment.domain.entity.Admin;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.UUID;

@Slf4j
public class UnjeongToken extends AbstractAuthenticationToken {
    private final Object principal;
    private final JWT jwt;

    // login
    public UnjeongToken(Admin admin) {
        super(null);
        this.principal = admin;
        this.jwt = null;
        setAuthenticated(true);
    }

    // request
    public UnjeongToken(String encodedJwt) {
        super(null);
        JWT jwt = decodeJwt(encodedJwt);
        this.principal = jwt.audience;
        this.jwt = jwt;
        super.setAuthenticated(true);
    }

    // login
    public String generateJwt() {
        Admin admin = (Admin) principal;
        JWT jwt = new JWT().setSubject("unjeongToken")
                .setIssuer("unjeong")
                .setIssuedAt(ZonedDateTime.now(ZoneId.of("Asia/Seoul")))
                .setAudience(admin.getId())
                .setUniqueId(UUID.randomUUID().toString())
                .setExpiration(ZonedDateTime.of(LocalDate.now().plusDays(1), LocalTime.MIN.plusHours(9), ZoneId.of("Asia/Seoul")));
        return String.format("Bearer %s", JWT.getEncoder().encode(jwt, HMACSigner.newSHA512Signer(System.getenv("UNJEONG_SIGNER"))));
    }

    // request
    private JWT decodeJwt(String encodedJwtString) {
        Verifier verifier = HMACVerifier.newVerifier(System.getenv("UNJEONG_SIGNER"));
        String[] splitJwt = encodedJwtString.split("Bearer ");
        return JWT.getDecoder().decode(splitJwt[0], verifier);
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }

    @Override
    public Object getCredentials() {
        return null;
    }
}