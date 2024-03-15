package info.svetlik.nervaak.simple.service.nn;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.list.FixedSizeList;

public record Axon(List<Dendrite> dendrites) implements Neuron.Output {

	public Axon(List<Dendrite> dendrites) {
		this.dendrites = FixedSizeList.fixedSizeList(dendrites);
	}

	public Axon(int connectionsLimit) {
		this(new ArrayList<>(connectionsLimit));
	}

}