package annotare.annotare;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class AnnotareApplication {

	public static void main(String[] args) {
		SpringApplication.run(AnnotareApplication.class, args);
	}

}
