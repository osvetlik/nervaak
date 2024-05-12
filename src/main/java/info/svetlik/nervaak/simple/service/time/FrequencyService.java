package info.svetlik.nervaak.simple.service.time;

/**
 * Simulates brain frequencies.
 */
public interface FrequencyService {

	/**
	 * Set the brain frequency in Hz.
	 * @param frequency New brain frequency in Hz.
	 */
	void setFrequency(double frequency);

	/**
	 * Set the brain wavelength in ns (1E9/frequency).
	 * @param waveLength New brain wavelength in ns.
	 */
	void setWaveLength(long waveLength);

	/**
	 * @return Time of the next frequency peak in nanoseconds.
	 */
	long nextPeak();

}
