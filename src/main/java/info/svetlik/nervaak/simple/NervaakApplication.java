package info.svetlik.nervaak.simple;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import info.svetlik.nervaak.simple.service.NervaakService;
import lombok.RequiredArgsConstructor;

@SpringBootApplication
@RequiredArgsConstructor
public class NervaakApplication implements CommandLineRunner {

	private final NervaakService nervakService;

	@Override
	public void run(String... args) {
		nervakService.runSimulation();
	}

	public static void main(String[] args) {
		SpringApplication.run(NervaakApplication.class, args);
	}
}
