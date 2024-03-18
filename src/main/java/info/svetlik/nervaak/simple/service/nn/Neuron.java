package info.svetlik.nervaak.simple.service.nn;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class Neuron {
	private final List<Source> inputs = new ArrayList<>();
	private final ThroughputPipe output = new Axon();
	State state;

	@Data
	public static class State {
		private double bias;
	}

	public Sink addInputSink() {
		final var sink = new Dendrite();
		this.inputs.add(sink);
		return sink;
	}

}
