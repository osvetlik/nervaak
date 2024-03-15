package info.svetlik.nervaak.simple.service.nn;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections4.list.FixedSizeList;

public record World(int size, List<Individual> individuals, State state) {

	public World(int size, List<Individual> individuals, State state) {
		if (size != individuals.size()) {
			throw new IllegalStateException();
		}
		this.size = size;
		this.individuals = FixedSizeList.fixedSizeList(individuals);
		this.state = state;
	}

	public World(int worldSize) {
		this(worldSize, Arrays.asList(new Individual[worldSize]), new State());
	}

	public static class State {

	}

}
