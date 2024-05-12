package info.svetlik.nervaak.simple.service.nn;

import info.svetlik.nervaak.simple.service.nn.util.LockingService;
import info.svetlik.nervaak.simple.service.time.ClockService;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class Dendrite extends NetworkComponent implements SpikeReceiver {

	private final DecayingProperty weight;
	private final SpikeReceiver neuron;

	public Dendrite(ClockService clockService, LockingService lockingService, double weight, SpikeReceiver neuron) {
		super(clockService, lockingService);
		this.weight = new DecayingProperty(weight, 5.0, 10.0, clockService, lockingService);
		this.neuron = neuron;
	}

	@Override
	public void spike(SpikeType spikeType, double weight) {
		neuron.spike(spikeType, this.weight.getValue());
	}

}
