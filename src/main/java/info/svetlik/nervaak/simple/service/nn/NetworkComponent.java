package info.svetlik.nervaak.simple.service.nn;

import java.util.concurrent.locks.StampedLock;
import java.util.function.BiConsumer;
import java.util.function.LongConsumer;
import java.util.function.LongFunction;

import info.svetlik.nervaak.simple.service.nn.util.LockingService;
import info.svetlik.nervaak.simple.service.time.ClockService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class NetworkComponent {

	protected final ClockService clockService;
	protected final LockingService lockingService;
	private final StampedLock lock = new StampedLock();

	protected long doUnderLock(LongConsumer consumer) {
		return this.lockingService.doUnderLock(lock, consumer);
	}

	protected <T> long doUnderLock(BiConsumer<Long, T> consumer, T value) {
		return this.lockingService.doUnderLock(lock, consumer, value);
	}

	public <T> T doUnderLock(LongFunction<T> function) {
		return this.lockingService.doUnderLock(lock, function);
	}

	protected long readUnderLock(LongConsumer consumer) {
		return this.lockingService.readUnderLock(lock, consumer);
	}

	protected <T> long readUnderLock(BiConsumer<Long, T> consumer, T value) {
		return this.lockingService.readUnderLock(lock, consumer, value);
	}

	public <T> T readUnderLock(LongFunction<T> function) {
		return this.lockingService.readUnderLock(lock, function);
	}

}
