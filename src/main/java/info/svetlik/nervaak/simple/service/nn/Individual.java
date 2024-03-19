package info.svetlik.nervaak.simple.service.nn;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import info.svetlik.nervaak.simple.service.configuration.NervaakConfigurationProperties;
import info.svetlik.nervaak.simple.service.impl.Pair;
import lombok.Data;

@Data
public class Individual {
	private final World world;
	private final State state = new State();
	private final List<ThroughputPipe> inputs = new ArrayList<>();
	private final List<ThroughputPipe> realInputs = new ArrayList<>();
	private final List<Neuron> outputs = new ArrayList<>();
	private final List<FeedbackRegister> feedbacks = new ArrayList<>();
	private final SortedMap<Integer, List<Neuron>> layers = new TreeMap<>();
	private final List<Neuron> allNeurons = new ArrayList<>();

	public Individual(World world, NervaakConfigurationProperties.Individual configuration) {
		this.world = world;
		populate(new Context(configuration.initialLayersCount(), configuration.initialConnectionLimit(), configuration.initialInputs(), configuration.initialOutputs()));
	}

	private static record Context(int numberOfLayers, int connectionsLimit, int numberOfInputs, int numberOfOutputs) {}

	@Data
	public static class State {

		private double happines = 0.0;

	}

	private void populate(Context context) {
		inputs.addAll(populateInputs(context.numberOfInputs()));
		realInputs.addAll(inputs);
		outputs.addAll(populateOutputs(context));
		feedbacks.addAll(populateFeedback(outputs));
		inputs.addAll(feedbacks);
		layers.putAll(populateHiddenLayers(context));

		Stream.concat(inputs.stream(), feedbacks.stream())
				.forEach(source -> source.addSinks(addNeuronLayerSinks(layers.get(0))));

		final var lastLayer = layers.values().stream().reduce(this::connectLayers).orElseThrow();

		connectLayers(lastLayer, outputs);
	}

	private List<Neuron> connectLayers(List<Neuron> layer1, List<Neuron> layer2) {
		layer1.stream().forEach(neuron -> neuron.getOutput().addSinks(addNeuronLayerSinks(layer2)));
		return layer2;
	}

	private Collection<Sink> addNeuronLayerSinks(Collection<Neuron> neurons) {
		return neurons.stream().map(Neuron::addInputSink).toList();
	}

	private Map<Integer, List<Neuron>> populateHiddenLayers(Context context) {
		return IntStream.range(0, context.numberOfLayers())
				.mapToObj(id -> populateHiddenLayer(id, context))
				.collect(Collectors.toMap(Pair::first, Pair::second, (a1, a2) -> a1, TreeMap::new));
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

	private List<Neuron> populateOutputs(Context context) {
		return IntStream.range(0, context.numberOfOutputs())
				.mapToObj(i -> createNeuron(context))
				.toList();
	}

	private List<FeedbackRegister> populateFeedback(List<Neuron> outputs) {
		return outputs.stream()
				.map(FeedbackRegister::new)
				.toList();
	}

	private Neuron createNeuron(Context context) {
		final var neuron = new Neuron();
		allNeurons.add(neuron);

		return neuron;
	}

}
