package info.svetlik.nervaak.simple.service.nn;

import lombok.Data;

@Data
public class FeedbackRegister implements Source, Sink {

	private final int id;

	@Override
	public void sink(double value) {
		// TODO Auto-generated method stub

	}

	@Override
	public double read() {
		// TODO Auto-generated method stub
		return 0;
	}

}
