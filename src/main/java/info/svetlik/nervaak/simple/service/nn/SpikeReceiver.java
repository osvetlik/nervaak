package info.svetlik.nervaak.simple.service.nn;

@FunctionalInterface
public interface SpikeReceiver {

	default void spike(SpikeType spikeType) {
		spike(spikeType, 1.0);
	}

	void spike(SpikeType spikeType, double weight);

}
