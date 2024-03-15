package info.svetlik.nervaak.simple.service.nn;

import lombok.Data;

public record Dendrite(Individual individual, State state) implements Neuron.Input {

	public Dendrite(Individual individual) {
		this(individual, new State());
	}

	@Data
	public static class State {
		private float weight;
		private float value;
	}

}
