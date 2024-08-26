package muse_kopis.muse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class MuseApplication {

	public static void main(String[] args) {
		SpringApplication.run(MuseApplication.class, args);
	}
}
