package info.svetlik.nervaak.simple.service.nn;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

import info.svetlik.nervaak.simple.service.configuration.NervaakConfigurationProperties;
import info.svetlik.nervaak.simple.service.nn.util.LockingService;
import info.svetlik.nervaak.simple.service.time.ClockService;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

@Data
@EqualsAndHashCode(callSuper = true)
@Slf4j
public class Network extends NetworkComponent {
	private final World world;
	private final State state = new State();
	private final List<Neuron> allNeurons = new ArrayList<>(8);
	private final Random random = new Random(1);

	public Network(World world, NervaakConfigurationProperties.Individual configuration, ClockService clockService, LockingService lockingService) {
		super(clockService, lockingService);
		this.world = world;
		populate();
	}

	@Data
	public static class State {

		private double happines = 0.0;

	}

	// FIXME just to test it now
	private void populate() {
		log.info("Creating neurons");
		this.allNeurons.addAll(IntStream.range(0, 80000)
				.mapToObj(this::createNeuron)
				.toList());
		log.info("Created {} neurons, connecting", this.allNeurons.size());
		this.allNeurons.forEach(n -> n.connect(allNeurons, random));
		log.info("Connected", this.allNeurons.size());
	}

	private Neuron createNeuron(int i) {
		if (i % 2 == 0) {
			return Neuron.inhibitory(clockService, lockingService);
		}

		return Neuron.excitatory(clockService, lockingService);
	}

}
