package info.svetlik.nervaak.simple.service.nn;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import info.svetlik.nervaak.simple.service.configuration.NervaakConfigurationProperties;
import info.svetlik.nervaak.simple.service.impl.Pair;
import lombok.Data;

public record Individual(World world, State state, List<Source> inputs, List<Source> outputs) {

	public Individual(World world, NervaakConfigurationProperties.Individual configuration) {
		this(world, new State(), new ArrayList<>(), new ArrayList<>());
		populate(new Context(configuration.initialLayersCount(), configuration.initialConnectionLimit(), configuration.initialInputs(), configuration.initialOutputs()));
	}

	private static record Context(int numberOfLayers, int connectionsLimit, int numberOfInputs, int numberOfOutputs) {}

	@Data
	public static class State {

		private double happines = 0.0;

	}

	private void populate(Context context) {
		populateInputs(context.numberOfInputs());
		populateFeedback(context.numberOfOutputs());
		populateHiddenLayers(context);
	}

	private Map<Integer, List<Neuron>> populateHiddenLayers(Context context) {
		return IntStream.range(0, context.numberOfLayers())
				.mapToObj(id -> populateHiddenLayer(id, context))
				.collect(Collectors.toMap(Pair::first, Pair::second));
	}

	private Pair<Integer, List<Neuron>> populateHiddenLayer(int layerId, Context context) {
		final var neurons = IntStream.range(0, context.connectionsLimit() - 1)
				.mapToObj(i -> createNeuron(context))
				.toList();
		return Pair.of(layerId, neurons);
	}

	private List<Sensor> populateInputs(int numberOfInputs) {
		return IntStream.range(0, numberOfInputs + 1)
				.mapToObj(Sensor::new)
				.toList();
	}

	private List<FeedbackRegister> populateFeedback(int numberOfOutputs) {
		return IntStream.range(0, numberOfOutputs)
				.mapToObj(FeedbackRegister::new)
				.toList();
	}

	private Neuron createNeuron(Context context) {
		return new Neuron(context.connectionsLimit());
	}

}
