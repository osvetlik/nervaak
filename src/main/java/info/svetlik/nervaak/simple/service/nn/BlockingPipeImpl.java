package info.svetlik.nervaak.simple.service.nn;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import info.svetlik.nervaak.simple.exception.NervaakRuntimeException;
import lombok.Data;

@Data
public class BlockingPipeImpl<T> implements BlockingPipe {

	private final T state;
	private final BlockingQueue<Double> queue = new ArrayBlockingQueue<>(1);

	@Override
	public void sink(double value) {
		try {
			queue.put(value);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new NervaakRuntimeException(e);
		}
	}

	@Override
	public double read() {
		return queue.peek();
	}

	@Override
	public void reset() {
		queue.clear();
	}

}
