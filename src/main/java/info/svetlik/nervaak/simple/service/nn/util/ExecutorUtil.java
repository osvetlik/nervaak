package info.svetlik.nervaak.simple.service.nn.util;

import java.util.Collection;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ExecutorUtil {

	private final ThreadFactory threadFactory = new VirtualThreadFactory();
	private final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(128, threadFactory);

	private static class VirtualThreadFactory implements ThreadFactory {

		@Override
		public Thread newThread(Runnable r) {
			return Thread.ofVirtual().unstarted(r);
		}

	}

	public void schedule(Runnable runnable, long delay) {
		scheduledExecutorService.schedule(runnable, delay, TimeUnit.NANOSECONDS);
	}

	public void runInParallel(Collection<Runnable> runnables) {
		try (final var virtualThreadPool = Executors.newFixedThreadPool(runnables.size(), threadFactory)) {
			ForkJoinPool.commonPool().submit(() -> runnables.parallelStream().forEach(virtualThreadPool::execute)).join();
		}
	}

}
