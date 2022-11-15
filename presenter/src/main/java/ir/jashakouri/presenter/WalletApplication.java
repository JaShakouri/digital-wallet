package ir.jashakouri.presenter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = {"ir.jashakouri.data"})
@EntityScan(basePackages = {"ir.jashakouri.data"})
@ComponentScan(basePackages = {"ir.jashakouri", "ir.jashakouri.data", "ir.jashakouri.domain", "ir.jashakouri.presenter"})
public class WalletApplication {
    public static void main(String[] args) {
        SpringApplication.run(WalletApplication.class, args);
    }
}
