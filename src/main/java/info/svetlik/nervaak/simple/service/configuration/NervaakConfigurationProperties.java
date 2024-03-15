package info.svetlik.nervaak.simple.service.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "nervaak")
public record NervaakConfigurationProperties(int worldSize, int initialConnectionLimit, int initialLayersCount, int initialNeuronsPerLayerCount) {

}
