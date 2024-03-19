package info.svetlik.nervaak.simple.service.impl;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.function.Function;

import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import info.svetlik.nervaak.simple.exception.NervaakRuntimeException;
import info.svetlik.nervaak.simple.service.NervaakService;
import info.svetlik.nervaak.simple.service.configuration.NervaakConfigurationProperties;
import info.svetlik.nervaak.simple.service.nn.Individual;
import info.svetlik.nervaak.simple.service.nn.ThroughputPipe;
import info.svetlik.nervaak.simple.service.nn.World;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class NervaakServiceImpl implements NervaakService {

	private final NervaakConfigurationProperties configurationProperties;
	private final StopWatch sw;
	private final ExecutorService executorService;

	@Override
	public void runSimulation() {
		sw.start("World population");
		final var world = populateWorld();
		sw.stop();

		sw.start("Iteration");
		iteration(world, this::allIndividualInputs);
		sw.stop();

		sw.start("More iterations");
		for (int i = 0; i < 1000; i++) {
			iteration(world, this::realIndividualInputs);
		}
		sw.stop();

		log.info("Statistics:\n{}", sw.prettyPrint());
	}

	private List<ThroughputPipe> allIndividualInputs(Individual individual) {
		return individual.getInputs();
	}

	private List<ThroughputPipe> realIndividualInputs(Individual individual) {
		return individual.getRealInputs();
	}

	private void iteration(final World world, Function<Individual, List<ThroughputPipe>> inputSupplier) {
		final var futures = world.individuals().stream().flatMap(i -> i.getAllNeurons().stream()).map(n -> executorService.submit(n::process)).toList();
		log.info("Started {} processes", futures.size());
		executorService.submit(() -> world.individuals().stream().flatMap(i -> inputSupplier.apply(i).stream()).forEach(i -> i.sink(0.0)));
		futures.forEach(this::join);
		log.info("Futures joined");
	}

	private void join(Future<?> future) {
		try {
			future.get();
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new NervaakRuntimeException(e);
		} catch (ExecutionException e) {
			throw new NervaakRuntimeException(e);
		}
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
