package info.svetlik.nervaak.simple.service.nn;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class ThroughputPipeImpl implements ThroughputPipe {

	private final List<Sink> sinks = new ArrayList<>();

	@Override
	public void sink(double value) {
		sinks.stream().filter(Objects::nonNull).forEach(sink -> sink.sink(value));
	}

	@Override
	public void addSink(Sink sink) {
		sinks.add(sink);
	}

	@Override
	public void addSinks(Collection<Sink> sinks) {
		this.sinks.addAll(sinks);
	}

}
