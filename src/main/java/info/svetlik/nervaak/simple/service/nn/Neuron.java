package info.svetlik.nervaak.simple.service.nn;

import java.util.Collection;
import java.util.Random;
import java.util.function.Predicate;

import info.svetlik.nervaak.simple.service.nn.util.LockingService;
import info.svetlik.nervaak.simple.service.time.ClockService;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class Neuron extends NetworkComponent implements SpikeReceiver {
	private final State state = new State(100.0, 50.0, 80.0);
	private final SpikeType spikeType;
	private final PassingSpikeReceiver axon;

	public Neuron(SpikeType type, ClockService clockService, LockingService lockingService) {
		super(clockService, lockingService);
		this.spikeType = type;
		this.axon = new PassingSpikeReceiver(clockService, lockingService, 500000);
	}

	@Data
	public class State {
		/**
		 * When current potential reaches this value (possibly shifted) this neuron
		 * sends an activation spike downstream.
		 */
		private DecayingProperty activationThreshold;
		/**
		 * Current potential as replenished by activation spikes from upstream, used for sending
		 * an activation spike or slowly decayed.
		 */
		private DecayingProperty currentPotential;

		public State(double maxPotential, double restingPotential, double activationThreshold) {
			this.currentPotential = new DecayingProperty(restingPotential, 5.0, maxPotential, clockService, lockingService);
			this.activationThreshold = new DecayingProperty(activationThreshold, 5.0, maxPotential, clockService, lockingService);
		}
	}

	public static Neuron excitatory(ClockService clockService, LockingService lockingService) {
		return new Neuron(SpikeType.EXCITATORY, clockService, lockingService);
	}

	public static Neuron inhibitory(ClockService clockService, LockingService lockingService) {
		return new Neuron(SpikeType.INHIBITORY, clockService, lockingService);
	}

	@Override
	public void spike(SpikeType spikeType, double weight) {
		final var potential = this.state.currentPotential.add(weight * spikeType.getVolume());
		if (potential > this.state.activationThreshold.getValue()) {
			this.state.currentPotential.add(-NervaakConstants.SPIKE_VOLUME);
			this.axon.spike(this.spikeType);
			StatisticsCollector.registerNeuronFire();
		}
	}

	public void connect(Collection<Neuron> targets, Random random) {
		targets.stream()
				.filter(Predicate.not(this::equals))
				.filter(n -> random.nextDouble() < 0.001)
				.forEach(n -> connect(n, random.nextDouble(0.5) + 0.5));
	}

	public void connect(Neuron target, double weight) {
		if (target == this) {
			return;
		}
		StatisticsCollector.registerNewConnection();
		final var dendrite = new Dendrite(clockService, lockingService, weight, target);
		final var synapse = new PassingSpikeReceiver(clockService, lockingService, 500000, dendrite);
		this.axon.addSpikeReceiver(synapse);
	}

}
