package info.svetlik.nervak.simple.unit;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import info.svetlik.nervaak.simple.service.nn.StatisticsCollector;

class StatisticsCollectorTest extends UnitTestParent {

	@Test
	void distributionTest() {
		StatisticsCollector.registerLockWait(Long.rotateLeft(1, 4));
		StatisticsCollector.registerLockWait(Long.rotateLeft(1, 4));
		StatisticsCollector.registerLockWait(Long.rotateLeft(1, 5));
		StatisticsCollector.registerLockWait(Long.rotateLeft(1, 62));
		final var result = StatisticsCollector.getLockWaitDistribution();
		assertThat(result[0]).isEqualTo(2);
		assertThat(result[1]).isEqualTo(1);
		assertThat(result[58]).isEqualTo(1);

		final var overallTime = StatisticsCollector.getLockWaitTime();
		assertThat(overallTime).isEqualTo(2L * Long.rotateLeft(1, 4) + Long.rotateLeft(1, 5) +  Long.rotateLeft(1, 62));

		final var events = StatisticsCollector.getLockWaitEvents();
		assertThat(events).isEqualTo(4);
	}

}
