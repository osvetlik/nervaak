package info.svetlik.nervaak.simple.service.impl;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import info.svetlik.nervaak.simple.service.NervaakService;
import info.svetlik.nervaak.simple.service.configuration.NervaakConfigurationProperties;
import info.svetlik.nervaak.simple.service.nn.Network;
import info.svetlik.nervaak.simple.service.nn.SpikeType;
import info.svetlik.nervaak.simple.service.nn.StatisticsCollector;
import info.svetlik.nervaak.simple.service.nn.World;
import info.svetlik.nervaak.simple.service.nn.util.LockingService;
import info.svetlik.nervaak.simple.service.time.ClockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class NervaakServiceImpl implements NervaakService, ApplicationContextAware {

	private final NervaakConfigurationProperties configurationProperties;
	private final StopWatch sw;
	private final ClockService clockService;
	private final LockingService lockingService;

	@Override
	public void runSimulation() throws InterruptedException {
		sw.start("World population");
		final var world = populateWorld();
		sw.stop();

		log.info("Start simulation: {} neurons, {} connections",
				world.individuals().get(0).getAllNeurons().size(),
				StatisticsCollector.getConnections());
		sw.start("Simulation");
		simulation(world);
		Thread.currentThread().join();
		sw.stop();

		log.info("Statistics:\n{}", sw.prettyPrint());
	}

	private void simulation(final World world) {
		final var neuron = world.individuals().get(0).getAllNeurons().get(1);
		neuron.spike(SpikeType.EXCITATORY);
		neuron.spike(SpikeType.EXCITATORY);
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

	private Network createIndividual(World world) {
		return new Network(world, configurationProperties.individual(), clockService, lockingService);
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		final var context = (ConfigurableApplicationContext) applicationContext;
		context.registerShutdownHook();
	}

}
