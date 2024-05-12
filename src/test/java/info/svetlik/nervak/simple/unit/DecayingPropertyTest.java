package info.svetlik.nervak.simple.unit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import info.svetlik.nervaak.simple.service.nn.DecayingProperty;
import info.svetlik.nervaak.simple.service.nn.util.LockingService;
import info.svetlik.nervaak.simple.service.nn.util.LockingServiceImpl;
import info.svetlik.nervaak.simple.service.time.ClockService;
import lombok.extern.slf4j.Slf4j;

@Import({
	LockingServiceImpl.class
})
@Slf4j
class DecayingPropertyTest extends UnitTestParent {

	@MockBean
	private ClockService clockService;

	@Autowired
	private LockingService lockingService;

	@Nested
	class ReadValue {

		@ParameterizedTest
		@CsvSource({
			"3.0, 1.0, 10.0",
			"3.0, 0.5, 10.0",
			"3.0, 0.1, 10.0",
			"0.1, 1.0, 10.0",
			"0.1, 0.5, 10.0",
			"0.1, 0.1, 10.0",
			"9.9, 1.0, 10.0",
			"9.9, 0.5, 10.0",
			"9.9, 0.1, 10.0",
		})
		void referenceValueRemains(double referenceValue, double decaySpeed, double maxValue) {
			when(clockService.clock()).thenCallRealMethod();
			when(clockService.getAsLong()).thenReturn(0L);
			final var property = new DecayingProperty(referenceValue, decaySpeed, maxValue, clockService, lockingService);
			when(clockService.getAsLong()).thenReturn(1000000L);
			final var decayed = property.getValue();
			log.info("{} -> {}", referenceValue, decayed);
			assertEquals(referenceValue, decayed);
		}

		@ParameterizedTest
		@CsvSource({
			"3.0, 1.0, 10.0",
			"3.0, 5.0, 10.0",
			"3.0, 0.1, 10.0",
			"0.1, 1.0, 10.0",
			"0.1, 5.0, 10.0",
			"0.1, 0.1, 10.0",
			"9.9, 1.0, 10.0",
			"9.9, 5.0, 10.0",
			"9.9, 0.1, 10.0",
		})
		void decaysGradually(double referenceValue, double decaySpeed, double maxValue) {
			when(clockService.clock()).thenCallRealMethod();
			when(clockService.getAsLong()).thenReturn(0L);
			final var property = new DecayingProperty(referenceValue, decaySpeed, maxValue, clockService, lockingService);
			property.add(5.0);
			final var startValue = property.getValue();
			when(clockService.getAsLong()).thenReturn(1000000L);
			final var decayed1 = property.getValue();
			assertThat(decayed1)
				.isLessThanOrEqualTo(startValue)
				.isGreaterThan(referenceValue);
			when(clockService.getAsLong()).thenReturn(2000000L);
			final var decayed2 = property.getValue();
			assertThat(decayed2)
				.isLessThan(decayed1)
				.isGreaterThan(referenceValue);
			when(clockService.getAsLong()).thenReturn(1000000000L);
			final var decayed3 = property.getValue();
			assertThat(decayed3)
				.isLessThan(decayed2)
				.isGreaterThan(referenceValue);
			log.info("{} -> {} -> {} -> {}", startValue, decayed1, decayed2, decayed3);
		}

		@ParameterizedTest
		@CsvSource({
			"3.0, 1.0, 10.0",
			"3.0, 5.0, 10.0",
			"3.0, 0.1, 10.0",
			"0.1, 1.0, 10.0",
			"0.1, 5.0, 10.0",
			"0.1, 0.1, 10.0",
			"9.9, 1.0, 10.0",
			"9.9, 5.0, 10.0",
			"9.9, 0.1, 10.0",
		})
		void decaysGraduallyFromBelow(double referenceValue, double decaySpeed, double maxValue) {
			when(clockService.clock()).thenCallRealMethod();
			when(clockService.getAsLong()).thenReturn(0L);
			final var property = new DecayingProperty(referenceValue, decaySpeed, maxValue, clockService, lockingService);
			property.add(-5.0);
			final var startValue = property.getValue();
			when(clockService.getAsLong()).thenReturn(1000000L);
			final var decayed1 = property.getValue();
			assertThat(decayed1)
				.isGreaterThanOrEqualTo(startValue)
				.isLessThan(referenceValue);
			when(clockService.getAsLong()).thenReturn(2000000L);
			final var decayed2 = property.getValue();
			assertThat(decayed2)
				.isGreaterThanOrEqualTo(decayed1)
				.isLessThan(referenceValue);
			when(clockService.getAsLong()).thenReturn(1000000000L);
			final var decayed3 = property.getValue();
			assertThat(decayed3)
				.isGreaterThanOrEqualTo(decayed2)
				.isLessThan(referenceValue);
			log.info("{} -> {} -> {} -> {}", startValue, decayed1, decayed2, decayed3);
		}

	}

	@Nested
	class Add {

	}

	@Nested
	class Strengthen {

	}

}
