package info.svetlik.nervaak.simple.service.nn;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import info.svetlik.nervaak.simple.exception.NervaakRuntimeException;
import lombok.Data;

public record Dendrite(State state) implements Sink, Source {

	@Data
	public static class State {
		private float weight;
		private float value;
		private final BlockingQueue<Double> queue;
		public State() {
			this.queue = new ArrayBlockingQueue<>(1);
		}
	}

	@Override
	public void sink(double value) {
		try {
			state.queue.put(value);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new NervaakRuntimeException(e);
		}
	}

	@Override
	public double read() throws InterruptedException {
		return state.queue.take();
	}

}
