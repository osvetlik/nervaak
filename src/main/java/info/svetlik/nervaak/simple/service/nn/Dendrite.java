package info.svetlik.nervaak.simple.service.nn;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class Dendrite extends BlockingPipeImpl<Dendrite.State> {

	public Dendrite() {
		super(new State());
	}

	@Data
	public static class State {
		private float weight;
	}

	@Override
	public double read() {
		return getState().weight * super.read();
	}

}
