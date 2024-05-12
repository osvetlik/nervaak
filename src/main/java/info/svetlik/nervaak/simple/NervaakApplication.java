package info.svetlik.nervaak.simple;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import info.svetlik.nervaak.simple.service.NervaakService;
import lombok.RequiredArgsConstructor;

@SpringBootApplication
@RequiredArgsConstructor
public class NervaakApplication implements CommandLineRunner {

	private final NervaakService nervakService;

	@Override
	public void run(String... args) throws InterruptedException {
		nervakService.runSimulation();
	}

	public static void main(String[] args) {
		final var app = new SpringApplicationBuilder(NervaakApplication.class).build(args);
		app.run(args);
	}

}
