package fmalc.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@ComponentScan({"fmalc.api"})
@EntityScan({"fmalc.api"})
@EnableJpaRepositories({"fmalc.api.repository"})
@EnableSwagger2
public class FmalcApplication {

	public static void main(String[] args) {
		SpringApplication.run(FmalcApplication.class, args);
	}

}
