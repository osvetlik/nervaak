package info.svetlik.nervaak.simple.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StopWatch;

@Configuration
public class CommonBeansConfiguration {

	public static final String COMMON_STOPWATCH_ID = "common-stop-watch";

	@Bean
	public StopWatch stopWatch() {
		return new StopWatch(COMMON_STOPWATCH_ID);
	}

}
