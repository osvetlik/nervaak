package info.svetlik.nervaak.simple.configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.boot.autoconfigure.task.TaskExecutionAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.core.task.support.TaskExecutorAdapter;
import org.springframework.util.StopWatch;

@Configuration
public class CommonBeansConfiguration {

	public static final String COMMON_STOPWATCH_ID = "common-stop-watch";

	@Bean
	public StopWatch stopWatch() {
		return new StopWatch(COMMON_STOPWATCH_ID);
	}

	@Bean
	public ExecutorService executorService() {
		return Executors.newVirtualThreadPerTaskExecutor();
	}

	@Bean(TaskExecutionAutoConfiguration.APPLICATION_TASK_EXECUTOR_BEAN_NAME)
	public AsyncTaskExecutor asyncTaskExecutor(ExecutorService executorService) {
	  return new TaskExecutorAdapter(executorService);
	}

}
