package info.svetlik.nervaak.simple.service.time.impl;

import org.springframework.stereotype.Service;

import info.svetlik.nervaak.simple.service.time.ClockService;
import info.svetlik.nervaak.simple.service.time.FrequencyService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FrequencyServiceImpl implements FrequencyService {

	private final ClockService clockService;
	private long waveLength;
	private long base;

	@Override
	public void setFrequency(double frequency) {
		setWaveLength((long) (1E9 / frequency));
	}

	@Override
	public void setWaveLength(long waveLength) {
		this.base = clockService.clock();
		this.waveLength = waveLength;
	}

	@Override
	public long nextPeak() {
		final var now = clockService.clock();
		final var diff = now - this.base;
		final var aligned = (1 + diff / this.waveLength) * waveLength;
		return this.base + aligned;
	}

}
