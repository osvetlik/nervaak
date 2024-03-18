package info.svetlik.nervaak.simple.service.impl;

public record Pair<U, V> (U first, V second) {

	public static <U, V> Pair<U, V> of(U first, V second) {
		return new Pair<>(first, second);
	}

}
