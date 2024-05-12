package info.svetlik.nervak.simple.unit;

import static org.mockito.Mockito.after;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.RetryingTest;

import info.svetlik.nervaak.simple.service.nn.util.ExecutorUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
class ExecutorUtilTest extends UnitTestParent {

	@Nested
	class Schedule {

		@RetryingTest(maxAttempts = 3)
		void scheduleAlreadyRunTest() {
			final var runnable = mock(Runnable.class);
			doAnswer(i -> {
				log.info("Here!");
				return null;
			}).when(runnable).run();
			ExecutorUtil.schedule(runnable, 100000000);
			verify(runnable, after(210).only()).run();
		}

		@Test
		void scheduleNotRunTest() {
			final var runnable = mock(Runnable.class);
			ExecutorUtil.schedule(runnable, 1000000000L);
			verify(runnable, after(11).never()).run();
		}

		@RetryingTest(maxAttempts = 3)
		void scheduleMultipleRunTest() {
			final var runnable1 = mock(Runnable.class);
			final var runnable2 = mock(Runnable.class);
			final var runnable3 = mock(Runnable.class);
			final var runnable4 = mock(Runnable.class);
			doAnswer(i -> {
				log.info("1 here!");
				return null;
			}).when(runnable1).run();
			doAnswer(i -> {
				log.info("2 here!");
				return null;
			}).when(runnable2).run();
			doAnswer(i -> {
				log.info("3 here!");
				return null;
			}).when(runnable3).run();
			doAnswer(i -> {
				log.info("4 here!");
				return null;
			}).when(runnable4).run();
			ExecutorUtil.schedule(runnable1, 100000000);
			ExecutorUtil.schedule(runnable2, 200000000);
			ExecutorUtil.schedule(runnable3, 300000000);
			ExecutorUtil.schedule(runnable4, 400000000);
			verify(runnable1, after(210).only()).run();
			verify(runnable2, after(310).only()).run();
			verify(runnable3, after(410).only()).run();
			verify(runnable4, after(510).only()).run();
		}

		@Test
		void bulkTest() throws InterruptedException {
			log.info("Scheduling threads");
			final var runCounter = new AtomicLong();
			final var runnables = ForkJoinPool.commonPool().submit(
					() -> IntStream.range(0, 1000)
							.parallel()
							.mapToObj(i -> mock(Runnable.class))
							.peek(r -> doAnswer(i -> runCounter.incrementAndGet()).when(r).run())
							.peek(r -> ExecutorUtil.schedule(r, 100000000))
							.map(this::runnableRunOnceChecker)
							.toList()).join();
			log.info("Scheduled, waiting");
			Thread.sleep(1000);
			log.info("Checking");
			ExecutorUtil.runInParallel(runnables);
			log.info("Checked {}", runCounter.get());
		}

		private Runnable runnableRunOnceChecker(Runnable r) {
			return () -> verify(r, after(1010).only()).run();
		}

	}

}
