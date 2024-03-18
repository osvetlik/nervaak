package info.svetlik.nervaak.simple.service.nn;

import lombok.Data;

@Data
public class Sensor implements Source {

	private final int id;

	@Override
	public double read() throws InterruptedException {
		// TODO Auto-generated method stub
		return 0;
	}

}
