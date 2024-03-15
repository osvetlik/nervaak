package info.svetlik.nervaak.simple.service.nn;

import java.util.Arrays;
import java.util.List;

import lombok.Data;

public record Individual(World world, List<Neuron> neurons, int connectionsLimit, State state) {

	public Individual(World world, List<Neuron> neurons, int connectionsLimit, State state) {
		this.world = world;
		this.neurons = neurons;
		this.connectionsLimit = connectionsLimit;
		this.state = state;
	}

	public Individual(World world, int initialNeuronCount, int connectionsLimit) {
		this(world, Arrays.asList(new Neuron[initialNeuronCount]), connectionsLimit, new State());
	}

	@Data
	public static class State {

		private float happines;

	}

}
