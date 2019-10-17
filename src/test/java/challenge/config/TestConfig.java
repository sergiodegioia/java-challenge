package challenge.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@ComponentScan("challenge.*")
@EnableJpaRepositories(basePackages = {"challenge.data.repository"})
@EntityScan("challenge.data.model")
@EnableAutoConfiguration
public class TestConfig {}

