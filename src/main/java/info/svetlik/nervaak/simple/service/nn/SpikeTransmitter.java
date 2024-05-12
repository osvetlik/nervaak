package info.svetlik.nervaak.simple.service.nn;

import java.util.ArrayList;
import java.util.List;

import info.svetlik.nervaak.simple.service.nn.util.ExecutorUtil;

public record SpikeTransmitter(long delay, List<SpikeReceiver> spikeReceivers) {

	public SpikeTransmitter(long delay) {
		this(delay, new ArrayList<>(8));
	}

	public SpikeTransmitter(long delay, SpikeReceiver target) {
		this(delay, List.of(target));
	}

	public void addSpikeReceiver(SpikeReceiver spikeReceiver) {
		this.spikeReceivers.add(spikeReceiver);
	}

	public void transmit(SpikeType spikeType) {
		ExecutorUtil.schedule(transmitInternal(spikeType), delay);
	}

	private Runnable transmitInternal(SpikeType spikeType) {
		return () -> spikeReceivers.forEach(sr -> sr.spike(spikeType));
	}

}
