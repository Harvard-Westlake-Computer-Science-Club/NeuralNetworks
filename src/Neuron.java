class Neuron {
    public double[] weights;
    public double bias;
    public Layer prevLayer;

    // This value is the neuron's output from the most recent run.
    public double val;

    // We take the whole layer as an argument since every neuron from the previous layer is linked to every node of the next layer!
    public Neuron(double[] weights, double bias, Layer prevLayer){
        this.weights = weights;
        this.bias = bias;
        this.prevLayer = prevLayer;
    }

    public void runCalculation(){
        // the bias is the "constant" of our linear equation, so let's add it from the beginning
        val = bias;
       // we then take the sum of each input, multiplied by its corresponding weight
        for(int i = 0; i < prevLayer.neurons.length; i++){
            val += weights[i] * prevLayer.neurons[i].val;
        }
        // finally, let's run our value through our activation function
        val = NeuralNetwork.activationFunction(val);
    }
}
