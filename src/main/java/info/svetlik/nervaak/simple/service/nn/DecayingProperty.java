package info.svetlik.nervaak.simple.service.nn;

import org.springframework.util.Assert;

import info.svetlik.nervaak.simple.service.nn.util.CalcUtil;
import info.svetlik.nervaak.simple.service.nn.util.LockingService;
import info.svetlik.nervaak.simple.service.time.ClockService;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Decaying property is such property which has a resting state, but can be pushed out of balance
 * by an input. It then returns to the resting state following an exponential function.
 */
@EqualsAndHashCode(callSuper = true)
public class DecayingProperty extends NetworkComponent {

	private long lastUpdateTimestamp;
	private double lastUpdateValue;
	private double referenceValue;
	private double decaySpeed;
	private double maxValue;

	/**
	 * @param referenceValue The resting state of the property.
	 * @param decaySpeed Modifies how fast the property drops back to the resting state. Must be a positive number.
	 * @param maxValue The upper boundary the value can reach.
	 */
	public DecayingProperty(double referenceValue, double decaySpeed, double maxValue, ClockService clockService, LockingService lockingService) {
		super(clockService, lockingService);
		Assert.isTrue(maxValue > 0.0, "Max value must be bigger than min value");
		Assert.isTrue(referenceValue > 0.0, "Reference value must be bigger than min value");
		Assert.isTrue(referenceValue < maxValue, "Reference value must be smaller than max value");
		Assert.isTrue(0.0 < decaySpeed, "Decay speed must be a positive number");
		this.referenceValue = referenceValue;
		this.lastUpdateValue = referenceValue;
		this.decaySpeed = decaySpeed;
		this.maxValue = maxValue;
		this.lastUpdateTimestamp = clockService.clock();
	}

	@Data
	private static class StateSnapshot {
		double referenceValue;
		double lastUpdateValue;
		long lastUpdateTimestamp;
	}

	/**
	 * This method doesn't actually modify the internal state, there is no need to. It just calculates how
	 * the value from the last update would have decayed since.
	 *
	 * @return The current value of the parameter, calculated from the value and timestamp of last update
	 * and the exponential decay since then.
	 */
	public double getValue() {
		final var stateSnapshot = new StateSnapshot();
		final var timestamp = readUnderLock(ts -> {
			fillSnapshotUnderLock(stateSnapshot);
			return ts;
		});
		final var timeDiff = timestamp - stateSnapshot.lastUpdateTimestamp;
		return stateSnapshot.referenceValue + (stateSnapshot.lastUpdateValue - stateSnapshot.referenceValue) * Math.exp(-decaySpeed * timeDiff * 1E-9);
	}

	private void fillSnapshotUnderLock(StateSnapshot stateSnapshot) {
		stateSnapshot.lastUpdateTimestamp = this.lastUpdateTimestamp;
		stateSnapshot.lastUpdateValue = this.lastUpdateValue;
		stateSnapshot.referenceValue = this.referenceValue;
	}

	/**
	 * Takes the timestamp and value after last update and applies the decay function to it. Doesn't modify
	 * the internal state, but should be run after lock to get consistent results.
	 *
	 * @param timestamp Timestamp of the value request.
	 *
	 * @return The current value of the parameter, calculated from the value and timestamp of last update
	 * and the exponential decay since then.
	 */
	private double getValueAfterDecay(long timestamp) {
		final var timeDiff = timestamp - lastUpdateTimestamp;
		return this.referenceValue + (lastUpdateValue - referenceValue) * Math.exp(-decaySpeed * timeDiff * 1E-9);
	}

	public double add(double value) {
		return doUnderLock(this::addUnderLock, value);
	}

	private double addUnderLock(long timestamp, double addition) {
		final var currentValue = getValueAfterDecay(timestamp);
		this.lastUpdateValue = CalcUtil.addToBoundedProperty(currentValue, addition, this.referenceValue, this.maxValue);
		this.lastUpdateTimestamp = timestamp;
		return this.lastUpdateValue;
	}

	/**
	 * Strengthens the current imbalance in the property by a given percentage. This
	 * locks in some portion of the imbalance "permanently" (until the next call) in an updated
	 * reference value.
	 *
	 * @param percentage How much of the difference between the current value and the reference
	 * value should be locked in.
	 */
	public void strengthenPropertyChange(double percentage) {
		doUnderLock(this::strengthenPropertyChangeUnderLock, percentage);
	}

	public void strengthenPropertyChangeUnderLock(long timestamp, double percentage) {
		final var currentValue = getValueAfterDecay(timestamp);
		final var diff = currentValue - this.referenceValue;
		this.referenceValue += diff * percentage;
		this.lastUpdateTimestamp = timestamp;
	}

}
