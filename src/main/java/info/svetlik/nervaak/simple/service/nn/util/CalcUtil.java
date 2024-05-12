package info.svetlik.nervaak.simple.service.nn.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class CalcUtil {

	/**
	 * Mimicing the way of relativistic speed addition, this method adds two numbers with regards to a given
	 * limit. The sum may reach the limit, but never exceed it. It adds non-linearity to the sum as opposed
	 * to just adding the numbers and cropping the outcome.
	 *
	 * If addition is negative, negative limits are not applied, the caller is responsible to handle the implications.
	 *
	 * @param x Original value if bigger than the limit, set to limit.
	 * @param addition Number to add, may be negative, negative limits not applied. If bigger than the limit, se to limit.
	 * @param limit Maximum sum.
	 * @return "Relativistic" sum of the two numbers.
	 */
	public double relativisticAdd(double x, double addition, double limit) {
		if (addition < 0.0) {
			final var result = x + addition;
			return result <= limit ? result : limit;
		}

		final var x2 = x < limit ? x : limit;
		final var addition2 = addition < limit ? addition : limit;
		return (x2 + addition2) / (1 + x2 * addition2 / (limit * limit));
	}

	/**
	 * Adds given value to the current one with zero and max as limits and restingValue
	 * as a breaking point. Towards the resting point the addition is linear, away from it
	 * it non-linearly approaches the respective limit.
	 *
	 * @param value Value to add to.
	 * @param addition To add (or subtract if negative)
	 * @param restingValue Breaking point.
	 * @param max The upper boundary.
	 * @return A number anywhere between zero and max.
	 */
	public double addToBoundedProperty(double value, double addition, double restingValue, double max) {
		if (addition > 0) {
			return normalizeAndAdd(value, addition, restingValue, max);
		}

		final var value2 = max - value;
		final var addition2 = -addition;
		final var restingValue2 = max - restingValue;

		return max - normalizeAndAdd(value2, addition2, restingValue2, max);
	}

	/**
	 * Normalizes the input so that only the interval between restingValue and max is considered.
	 *
	 * @see #addToBoundedProperty(double, double, double, double)
	 */
	private double normalizeAndAdd(double value, double addition, double restingValue, double max) {
		final var max2 = max - restingValue;
		final var value2 = value >= restingValue ? value - restingValue : 0;
		final var addition2 = value >= restingValue ? addition : addition - (restingValue - value);
		return restingValue + relativisticAdd(value2, addition2, max2);
	}

}
