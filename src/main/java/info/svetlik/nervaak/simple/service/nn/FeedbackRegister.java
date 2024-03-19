package info.svetlik.nervaak.simple.service.nn;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class FeedbackRegister extends ThroughputPipeImpl {

	public FeedbackRegister(Neuron neuron) {
		neuron.getOutput().addSink(this);
	}

}
