package info.svetlik.nervaak.simple.service.time;

import java.util.function.LongSupplier;

@FunctionalInterface
public interface ClockService extends LongSupplier {

	default long clock() {
		return this.getAsLong();
	}

}
