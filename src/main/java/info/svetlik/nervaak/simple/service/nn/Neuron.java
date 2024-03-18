package info.svetlik.nervaak.simple.service.nn;

import java.util.Arrays;
import java.util.List;

import lombok.Data;

public record Neuron(List<Source> inputs, Sink output, State state) {

	public Neuron(int connectionsLimit) {
		this(Arrays.asList(new Source[connectionsLimit]), new Axon(connectionsLimit), new State());
	}

	public Neuron(int connectionsLimit, Source input) {
		this(Arrays.asList(input), new Axon(connectionsLimit), new State());
	}

	@Data
	public static class State {
		private double bias;
	}

}
