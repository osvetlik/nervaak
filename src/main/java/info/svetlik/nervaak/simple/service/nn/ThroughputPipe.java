package info.svetlik.nervaak.simple.service.nn;

import java.util.Collection;

public interface ThroughputPipe extends Sink {

	void addSink(Sink sink);
	void addSinks(Collection<Sink> sinks);

}
