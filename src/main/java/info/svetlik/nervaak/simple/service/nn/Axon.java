package info.svetlik.nervaak.simple.service.nn;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public record Axon(List<Sink> sinks) implements Sink {

	public Axon(int connectionsLimit) {
		this(Arrays.asList(new Sink[connectionsLimit]));
	}

	@Override
	public void sink(double value) {
		sinks.stream().filter(Objects::nonNull).forEach(sink -> sink.sink(value));
	}

}