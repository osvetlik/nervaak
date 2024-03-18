package info.svetlik.nervaak.simple.service.nn;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class FeedbackRegister extends ThroughputPipeImpl {

	private final int id;

	public FeedbackRegister(int id) {
		this.id = id;
	}

}
