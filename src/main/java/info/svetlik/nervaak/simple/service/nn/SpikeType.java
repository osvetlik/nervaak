package info.svetlik.nervaak.simple.service.nn;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum SpikeType {
	INHIBITORY(-NervaakConstants.SPIKE_VOLUME),
	EXCITATORY(NervaakConstants.SPIKE_VOLUME);

	@Getter
	private final double volume;
}
