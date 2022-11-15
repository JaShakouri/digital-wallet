package ir.jashakouri.domain.config.security.openapi;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

/**
 * @author jash
 * @created 14/11/2022 - 17:14
 * @project digital-wallet-backend
 */
@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Digital wallet platform",
                description = "Digital wallet platform for improve myself and written by Javad Shakouri (Software Engineer)",
                contact = @Contact(
                        name = "Javad Shakouri",
                        url = "https://jashakouri.ir",
                        email = "jashakouri@gmail.com")),
        servers = {
                @Server(url = "http://localhost:8080", description = "Develop version")
        })
@SecurityScheme(
        name = "API",
        scheme = "BASIC",
        type = SecuritySchemeType.HTTP,
        in = SecuritySchemeIn.HEADER)
class OpenAPIConfiguration {
}