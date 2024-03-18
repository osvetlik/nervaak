package info.svetlik.nervaak.simple.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import info.svetlik.nervaak.simple.service.NervaakService;
import info.svetlik.nervaak.simple.service.configuration.NervaakConfigurationProperties;
import info.svetlik.nervaak.simple.service.nn.Individual;
import info.svetlik.nervaak.simple.service.nn.World;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class NervaakServiceImpl implements NervaakService {

	private final NervaakConfigurationProperties configurationProperties;
	private final StopWatch sw;

	@Override
	public void runSimulation() {
		sw.start("World population");
		final var world = populateWorld();
		sw.stop();
		log.info("Statistics:\n{}", sw.prettyPrint());
	}

	private World populateWorld() {
		final var world = new World(configurationProperties.worldSize());
		final var iterator = world.individuals().listIterator();
		while (iterator.hasNext()) {
			iterator.next();
			iterator.set(createIndividual(world));
		}
		return world;
	}

	private Individual createIndividual(World world) {
		return new Individual(world, configurationProperties.individual());
	}

}
