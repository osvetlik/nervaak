package info.svetlik.nervaak.simple.service.nn.util;

import java.util.concurrent.locks.StampedLock;
import java.util.function.BiConsumer;
import java.util.function.LongConsumer;
import java.util.function.LongFunction;

public interface LockingService {

	public long readUnderLock(StampedLock lock, LongConsumer consumer);

	public <T> long readUnderLock(StampedLock lock, BiConsumer<Long, T> consumer, T value);

	public <T> T readUnderLock(StampedLock lock, LongFunction<T> function);

	public long doUnderLock(StampedLock lock, LongConsumer consumer);

	public <T> long doUnderLock(StampedLock lock, BiConsumer<Long, T> consumer, T value);

	public <T> T doUnderLock(StampedLock lock, LongFunction<T> function);

}
