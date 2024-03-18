package info.svetlik.nervaak.simple.service.nn;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class Sensor extends ThroughputPipeImpl {

	private final int id;

	public Sensor(int id) {
		this.id = id;
	}

}
