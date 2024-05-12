package info.svetlik.nervaak.simple.service.nn;

import info.svetlik.nervaak.simple.service.nn.util.LockingService;
import info.svetlik.nervaak.simple.service.time.ClockService;

public class PassingSpikeReceiver extends NetworkComponent implements SpikeReceiver {

	private final SpikeTransmitter spikeTransmitter;

	public PassingSpikeReceiver(ClockService clockService, LockingService lockingService, long delay) {
		super(clockService, lockingService);
		this.spikeTransmitter = new SpikeTransmitter(delay);
	}

	public PassingSpikeReceiver(ClockService clockService, LockingService lockingService, long delay, SpikeReceiver target) {
		super(clockService, lockingService);
		this.spikeTransmitter = new SpikeTransmitter(delay, target);
	}

	@Override
	public void spike(SpikeType spikeType, double weight) {
		spikeTransmitter.transmit(spikeType);
	}

	public void addSpikeReceiver(SpikeReceiver spikeReceiver) {
		this.spikeTransmitter.addSpikeReceiver(spikeReceiver);
	}

}
