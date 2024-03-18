package info.svetlik.nervaak.simple.service.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @param worldSize Number of individuals in the world.
 */
@ConfigurationProperties(prefix = "nervaak")
public record NervaakConfigurationProperties(int worldSize, Individual individual) {

	/**
	 * @param initialConnectionLimit Maximum number of connections, this applies separately to input and output connections, but the limit is the same.
	 * @param initialLayersCount The count of layers in a newly generated individual.
	 * @param initialInputs The number of input neurons in a newly generated individual not counting the feedback.
	 * @param initialOutputs The number of output neurons in a newly generated individual.
	 */
	public record Individual(int initialConnectionLimit, int initialLayersCount, int initialInputs, int initialOutputs) {}

}
