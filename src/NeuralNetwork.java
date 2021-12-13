import java.util.Random;

class NeuralNetwork {
    public Layer[] layers;

    // temp var, used my Main for convenience
    public double error;

    /**
     * The activation function of our Neural Network. This is basically a transformation of our choice that gets applied
     * to the data after each layer before it is passed on.
     *
     * For now, we won't use an activation function, but some common ones are ReLU, Sigmoid, etc.
     */
    public static double activationFunction(double input){
        return input;
    }

    /**
     * Constructs a new Neural Network.
     * @param neuronCounts An array representing how many neurons each layer should have. The length of this array should be how many layers the NN should have.
     * @param randMin The minimum bound for the random weights & biases assigned to each neuron.
     * @param randMax The maximum bound for the random weights & biases assigned to each neuron.
     */
    public NeuralNetwork(int[] neuronCounts, double randMin, double randMax){
        layers = new Layer[neuronCounts.length];
        // Let's create the input layer first!
        layers[0] = new Layer(neuronCounts[0], randMin, randMax, null);
        // Create the hidden & output layers
        for(int i = 1; i < neuronCounts.length; i++){
            layers[i] = new Layer(neuronCounts[i], randMin, randMax, layers[i - 1]);
        }
    }

    public double[] runCalculation(double[] inputs){
        // First, manually set the values of the input layer to what we're given.
        Layer inputLayer = layers[0];
        for(int i = 0; i < inputLayer.neurons.length; i++){
            inputLayer.neurons[i].val = inputs[i];
        }

        // Then, just call runCalculation on each hidden layer and then the output layer.
        for(int i = 1; i < layers.length; i++){
            layers[i].runCalculation();
        }

        // Extract the values from the output layer and return
        Layer outputLayer = layers[layers.length - 1];
        double[] output = new double[outputLayer.neurons.length];
        for(int i = 0; i < output.length; i++){
            output[i] = outputLayer.neurons[i].val;
        }
        return output;
    }

    // A helper function to create a clone of this NN.
    public NeuralNetwork deepCopy(){
        int[] neuronCounts = new int[layers.length];
        for(int i = 0; i < layers.length; i++) neuronCounts[i] = layers[i].neurons.length;
        NeuralNetwork copy = new NeuralNetwork(neuronCounts, 0, 0);
        for(int i = 0; i < layers.length; i++){
            for(int j = 0; j < layers[i].neurons.length; j++){
                copy.layers[i].neurons[j].bias = layers[i].neurons[j].bias;
                for(int k = 0; k < layers[i].neurons[j].weights.length; k++){
                    copy.layers[i].neurons[j].weights[k] = layers[i].neurons[j].weights[k];
                }
            }
        }
        return copy;
    }

    public void mutate(double randomAmount){
        for(int i = 1; i < layers.length; i++){
            Layer layer = layers[i];
            for(int j = 0; j < layer.neurons.length; j++){
                Neuron neuron = layer.neurons[j];
                for(int k = 0; k < neuron.weights.length; k++){
                    neuron.weights[k] += (Math.random() * randomAmount * 2) - randomAmount;
                }
                neuron.bias += (Math.random() * randomAmount * 2) - randomAmount;
            }
        }
    }
}
