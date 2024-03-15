package info.svetlik.nervaak.simple.service.nn;

import java.util.Arrays;
import java.util.List;

public record Neuron(Individual individual, List<Input> inputs, Output output) {

	public Neuron(Individual individual) {
		this(individual, Arrays.asList(new Input[individual.connectionsLimit()]), new Axon(individual.connectionsLimit()));
	}

	public static interface Input {

	}

	public static interface Output {

	}

}
