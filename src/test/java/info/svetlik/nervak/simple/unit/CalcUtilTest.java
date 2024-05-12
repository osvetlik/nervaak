package info.svetlik.nervak.simple.unit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.offset;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import info.svetlik.nervaak.simple.service.nn.util.CalcUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
class CalcUtilTest extends UnitTestParent {

	@Nested
	class RelativisticAdd {

		@ParameterizedTest
		@CsvSource({
			"0.0, 0.0, 10.0",
			"10.0, 10.0, 10.0",
			"2.0, 2.0, 10.0",
			"20.0, 0.0, 10.0",
			"20.0, 20.0, 10.0",
			"10.0, 10.0, 10.0",
			"0.0, 5.0, 10.0",
			"0.0, 10.0, 10.0",
			"0.0, 15.0, 10.0",
			"5.0, 0.0, 10.0",
			"10.0, 0.0, 10.0",
			"15.0, 0.0, 10.0",
			"0.1, 0.1, 10.0",
			"9.9, 9.9, 10.0"
		})
		void adding(double a, double b, double limit) {
			final var result = CalcUtil.relativisticAdd(a, b, limit);
			log.info("relativisticAdd: {} + {} = {} <= {}", a, b, result, limit);
			assertThat(result)
				.isLessThanOrEqualTo(limit)
				.isGreaterThanOrEqualTo(a < limit ? a : limit)
				.isGreaterThanOrEqualTo(b < limit ? b : limit);
		}

		@ParameterizedTest
		@CsvSource({
			"20.0, -10.0, 10.0",
			"20.0, -1.0, 10.0",
			"10.0, -10.0, 0.0",
			"2.0, -2.0, 0.0",
			"10.0, -2.0, 8.0",
		})
		void subtracting(double a, double b, double expected) {
			final var result = CalcUtil.relativisticAdd(a, b, 10.0);
			log.info("relativisticAdd: {} + {} = {} <= {}", a, b, result, 10.0);
			assertThat(result)
				.isEqualTo(expected);
		}
	}

	@Nested
	class AddToBoundedProperty {

		@ParameterizedTest
		@CsvSource({
			"2.0, 3.0, 5.0, 10.0",
			"2.0, 7.0, 7.0, 10.0",
			"5.0, 7.0, 10.0, 10.0",
			"5.0, 7.0, 100.0, 10.0",
			"5.0, 7.0, 0.1, 10.0",
			"5.0, 5.0, 100.0, 10.0",
			"5.0, 5.0, 0.1, 10.0",
			"5.0, 9.9, 0.1, 10.0",
			"5.0, 9.9, 9.9, 10.0",
		})
		void addingAboveRestingValue(double restingValue, double value, double addition, double max) {
			final var result = CalcUtil.addToBoundedProperty(value, addition, restingValue, max);
			log.info("addToBoundedProperty: {} <= {} + {} = {} <= {}", restingValue, value, addition, result, max);
			assertThat(result)
				.isGreaterThanOrEqualTo(value)
				.isLessThanOrEqualTo(max);
		}

		@ParameterizedTest
		@CsvSource({
			"4.0, 3.0, 5.0, 10.0, 8.0",
			"4.0, 2.0, 7.0, 10.0, 9.0",
			"5.0, 3.0, 10.0, 10.0, 10.0",
			"5.0, 3.0, 1.0, 10.0, 4.0",
			"5.0, 3.0, 0.1, 10.0, 3.1",
			"5.0, 3.0, 9.9, 10.0, 10.0",
		})
		void addingBelowRestingValue(double restingValue, double value, double addition, double max, double expected) {
			final var result = CalcUtil.addToBoundedProperty(value, addition, restingValue, max);
			log.info("addToBoundedProperty: {} > {} + {} = {} <= {}", restingValue, value, addition, result, max);
			assertThat(result)
				.isEqualTo(expected);
		}

		@ParameterizedTest
		@CsvSource({
			"4.0, 7.0, -2.0, 5.0",
			"4.0, 5.0, -2.0, 3.0",
			"4.0, 6.0, -7.0, 0.0",
			"4.0, 6.0, -0.1, 5.9",
			"4.0, 4.1, -0.2, 3.9",
			"4.0, 4.1, -0.1, 4.0",
	})
		void subtractingAboveRestingValue(double restingValue, double value, double subtraction, double expected) {
			final var result = CalcUtil.addToBoundedProperty(value, subtraction, restingValue, 10.0);
			log.info("addToBoundedProperty: {} < {} - {} = {} >= 0", restingValue, value, -subtraction, result);
			assertThat(result)
				.isEqualTo(expected, offset(0.0001));
		}

		@ParameterizedTest
		@CsvSource({
			"7.0, 4.0, -2.0",
			"5.0, 4.0, -2.0",
			"6.0, 3.0, -7.0",
			"6.0, 6.0, -7.0",
			"6.0, 6.0, -0.1",
			"6.0, 6.0, -5.9",
			"6.0, 0.1, -5.9",
			"6.0, 0.1, -0.1",
		})
		void subtractingBelowRestingValue(double restingValue, double value, double subtraction) {
			final var result = CalcUtil.addToBoundedProperty(value, subtraction, restingValue, 10.0);
			log.info("addToBoundedProperty: {} > {} - {} = {} >= 0", restingValue, value, -subtraction, result);
			assertThat(result)
				.isGreaterThanOrEqualTo(0.0)
				.isLessThan(restingValue);
		}

		@ParameterizedTest
		@CsvSource({
			"9.9, 9.9, 1.0, 10.0",
			"9.8, 9.8, 0.1, 10.0",
		})
		void addingWithRestingValueNearMax(double restingValue, double value, double addition, double max) {
			final var result = CalcUtil.addToBoundedProperty(value, addition, restingValue, max);
			log.info("addToBoundedProperty: {} <= {} + {} = {} <= {}", restingValue, value, addition, result, max);
			assertThat(result)
				.isGreaterThanOrEqualTo(value)
				.isLessThanOrEqualTo(max);
		}

		@ParameterizedTest
		@CsvSource({
			"0.1, 0.1, -1.0",
			"0.1, 0.1, -0.01",
			"0.2, 0.1, -0.01",
			"0.2, 0.1, -100.0",
		})
		void subtractingWithRestingValueNearZero(double restingValue, double value, double subtraction) {
			final var result = CalcUtil.addToBoundedProperty(value, subtraction, restingValue, 10.0);
			log.info("addToBoundedProperty: {} >= {} - {} = {} >= 0", restingValue, value, -subtraction, result);
			assertThat(result)
				.isGreaterThanOrEqualTo(0.0)
				.isLessThan(restingValue);
		}

	}

}
