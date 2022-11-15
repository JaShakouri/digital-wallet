package ir.jashakouri.data.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import ir.jashakouri.data.dto.response.GeneratorToken;
import ir.jashakouri.data.enums.TokenType;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author jashakouri on 27.08.22
 * @project Digital Wallet
 * @email JaShakouri@gmail.com
 */
@Slf4j(topic = "LOG_JWTGenerator")
public class JWTGenerator {

    public static GeneratorToken generate(HttpServletRequest request,
                                          PasswordEncoder passwordEncoder,
                                          Object user,
                                          Algorithm algorithm,
                                          TokenType type) {

        String username = null;
        var listPermission = new ArrayList<String>();

        if (user instanceof org.springframework.security.core.userdetails.User) {

            username = ((User) user).getUsername();
            listPermission.addAll(((User) user).getAuthorities()
                    .stream().map(GrantedAuthority::getAuthority).toList());

        } else if (user instanceof ir.jashakouri.data.entities.User) {

            username = ((ir.jashakouri.data.entities.User) user).getUsername();
            listPermission.addAll(((ir.jashakouri.data.entities.User) user)
                    .getUserType().getGrantedAuthorities().stream().map(SimpleGrantedAuthority::getAuthority).toList());

        }

        var expireDate = new Date(System.currentTimeMillis() +
                (type == TokenType.ACCESS ?
                        TimeUnit.MINUTES.toMillis(120) :
                        TimeUnit.DAYS.toMillis(10)));

        String token = JWT.create()
                .withSubject(username)
                .withExpiresAt(expireDate)
                .withIssuer(request.getRequestURL().toString())
                .withClaim("roles", listPermission)
                .withClaim("sessionId", passwordEncoder.encode(NetworkUtils.getIp(request)))
                .sign(algorithm);

        return new GeneratorToken(
                token, expireDate
        );
    }
}

