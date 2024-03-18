package info.svetlik.nervaak.simple.service.nn;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ActivationFunctions implements ActivationFunction {

	TANH(ActivationFunctions::tanh),
	SIGMOID(ActivationFunctions::sigmoid),
	RELU(ActivationFunctions::relu),
	LEAKY_RELU_001(leakyRelu(ActivationFunctions.ALPHA_001)),
	LEAKY_RELU_002(leakyRelu(ActivationFunctions.ALPHA_002)),
	LEAKY_ELU_001(elu(ActivationFunctions.ALPHA_001)),
	LEAKY_ELU_002(elu(ActivationFunctions.ALPHA_002)),
	SWISH(ActivationFunctions::swish);

	private static final double ALPHA_001 = 0.01;
	private static final double ALPHA_002 = 0.02;

	private final ActivationFunction activationFunction;

	private static final double tanh(double input) {
		return Math.tanh(input);
	}

	private static final double sigmoid(double input) {
		return 1.0 / (1.0 + Math.exp(-input));
	}

	private static double relu(double x) {
        return Math.max(0, x);
    }

	private static ActivationFunction leakyRelu(double alpha) {
    	return x -> x > 0 ? x : alpha * x;
    }

	private static double swish(double x) {
		return x * sigmoid(x);
	}

	private static ActivationFunction elu(double alpha) {
		return x -> (x > 0) ? x : alpha * (Math.exp(x) - 1);
	}

	@Override
	public double activation(double input) {
		return this.activationFunction.activation(input);
	}

}
