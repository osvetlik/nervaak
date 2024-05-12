package info.svetlik.nervaak.simple.service.nn.util;

import java.util.concurrent.locks.StampedLock;
import java.util.function.BiConsumer;
import java.util.function.LongConsumer;
import java.util.function.LongFunction;

import org.springframework.stereotype.Service;

import info.svetlik.nervaak.simple.service.nn.StatisticsCollector;
import info.svetlik.nervaak.simple.service.time.ClockService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class LockingServiceImpl implements LockingService {

	private final ClockService clockService;

	@Override
	public long doUnderLock(StampedLock lock, LongConsumer consumer) {
		final var start = clockService.clock();
		final var stamp = lock.writeLock();
		try {
			final var timestamp = clockService.clock();
			StatisticsCollector.registerLockWait(timestamp - start);
			consumer.accept(timestamp);
			return timestamp;
		} finally {
			lock.unlockWrite(stamp);
		}
	}

	@Override
	public <T> long doUnderLock(StampedLock lock, BiConsumer<Long, T> consumer, T value) {
		final var start = clockService.clock();
		final var stamp = lock.writeLock();
		try {
			final var timestamp = clockService.clock();
			StatisticsCollector.registerLockWait(timestamp - start);
			consumer.accept(timestamp, value);
			return timestamp;
		} finally {
			lock.unlockWrite(stamp);
		}
	}

	@Override
	public <T> T doUnderLock(StampedLock lock, LongFunction<T> function) {
		final var start = clockService.clock();
		final var stamp = lock.writeLock();
		try {
			final var timestamp = clockService.clock();
			StatisticsCollector.registerLockWait(timestamp - start);
			return function.apply(timestamp);
		} finally {
			lock.unlockWrite(stamp);
		}
	}

	@Override
	public long readUnderLock(StampedLock lock, LongConsumer consumer) {
		final var start = clockService.clock();
		final var stamp = lock.readLock();
		try {
			final var timestamp = clockService.clock();
			StatisticsCollector.registerLockWait(timestamp - start);
			consumer.accept(timestamp);
			return timestamp;
		} finally {
			lock.unlockRead(stamp);
		}
	}

	@Override
	public <T> long readUnderLock(StampedLock lock, BiConsumer<Long, T> consumer, T value) {
		final var start = clockService.clock();
		final var stamp = lock.readLock();
		try {
			final var timestamp = clockService.clock();
			StatisticsCollector.registerLockWait(timestamp - start);
			consumer.accept(timestamp, value);
			return timestamp;
		} finally {
			lock.unlockRead(stamp);
		}
	}

	@Override
	public <T> T readUnderLock(StampedLock lock, LongFunction<T> function) {
		final var start = clockService.clock();
		final var stamp = lock.readLock();
		try {
			final var timestamp = clockService.clock();
			StatisticsCollector.registerLockWait(timestamp - start);
			return function.apply(timestamp);
		} finally {
			lock.unlockRead(stamp);
		}
	}

}
