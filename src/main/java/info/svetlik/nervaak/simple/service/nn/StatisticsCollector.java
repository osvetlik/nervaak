package info.svetlik.nervaak.simple.service.nn;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicLongArray;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

/**
 * Statistics collector to measure miscelaneous performance statistics.
 */
@Slf4j
@UtilityClass
public final class StatisticsCollector {

	/**
	 * We are not interested in values less then 16 ns, these are probably not waits anyway.
	 */
	private static final int IGNORED_LOCK_WAIT_BITS = 6;
	private static final long LOCK_WAIT_THRESHOLD = 1 << IGNORED_LOCK_WAIT_BITS;

	/**
	 * A total sum of waiting times in nanoseconds.
	 */
	private static final AtomicLong lockWaitTime = new AtomicLong(0);

	/**
	 * A total count of wait events.
	 */
	private static final AtomicLong lockWaitEvents = new AtomicLong(0);

	/**
	 * Logarithmic (base 2) distribution of the wait times.
	 */
	private static final AtomicLongArray lockWaitDistribution = new AtomicLongArray(Long.SIZE - IGNORED_LOCK_WAIT_BITS - 1);

	private static final AtomicLong neuronFires = new AtomicLong();

	private static final AtomicLong connections = new AtomicLong();

	/**
	 * Register a lock waiting time.
	 *
	 * @param nanos How many nanoseconds did you wait for the lock.
	 */
	public static void registerLockWait(long nanos) {
		if (nanos >= LOCK_WAIT_THRESHOLD) {
			lockWaitTime.addAndGet(nanos);
			lockWaitEvents.incrementAndGet();
			final var magnitude = Long.SIZE - IGNORED_LOCK_WAIT_BITS - Long.numberOfLeadingZeros(nanos) - 1;
			lockWaitDistribution.incrementAndGet(magnitude);
		}
	}

	public static void registerNeuronFire() {
		final var current = neuronFires.incrementAndGet();
		if (current % 100000 == 0) {
			final var events = lockWaitEvents.get();
			final var time = lockWaitTime.get();
			log.info("{} fires, {} lock waits, {} ns spent waiting, {} ns average",
					current, events, time, time/events);
		}
		if (current > 1E6) {
			log.info("Quitting after {} spikes", current);
			System.exit(0);
		}
	}

	public void registerNewConnection() {
		connections.incrementAndGet();
	}

	public long getConnections() {
		return connections.get();
	}

	/**
	 * Get the total waiting time.
	 *
	 * @return A long representation of the waiting time preventing accidental change of the internal value.
	 */
	public static long getLockWaitTime() {
		return lockWaitTime.get();
	}

	/**
	 * Get the total number of events.
	 *
	 * @return A long representation of the waiting events preventing accidental change of the internal value.
	 */
	public static long getLockWaitEvents() {
		return lockWaitEvents.get();
	}

	/**
	 * Get the whole distribution of waiting times.
	 *
	 * @return A copy of the internal array to prevent accidental change.
	 */
	public static long[] getLockWaitDistribution() {
		final var result = new long[lockWaitDistribution.length()];
		for (int i = 0; i < result.length; i++) {
			result[i] = lockWaitDistribution.get(i);
		}

		return result;
	}

	/**
	 * Fill the given array with lock wait time distribution, only copies the requested (result.length)
	 * number of entries.
	 *
	 * @param result Array of longs to store the distribution in.
	 *
	 * @throws IllegalArgumentException Don't give me longer arrays, I don't like it.
	 */
	public static void getLockWaitDistribution(long[] result) {
		if (result.length > lockWaitDistribution.length()) {
			throw new IllegalArgumentException("The output array is larger than the statistics");
		}
		for (int i = 0; i < result.length; i++) {
			result[i] = lockWaitDistribution.get(i);
		}
	}

}
