class Layer {
    public Neuron[] neurons;

    /**
     * Constructs a new layer.
     * @param neuronCount How many neurons this layer should have.
     * @param randMin The minimum bound for the random weights & biases assigned to each neuron.
     * @param randMax The maximum bound for the random weights & biases assigned to each neuron.
     * @param prevLayer The previous layer in the NN; set to null if this layer is the input layer.
     */
    public Layer(int neuronCount, double randMin, double randMax, Layer prevLayer){
        neurons = new Neuron[neuronCount];
        // Let's initialize each neuron in this layer with random values
        for(int i = 0; i < neuronCount; i++){
            // Generate random weights and a random bias for this neuron
            double[] weights;
            if(prevLayer == null){
                // Special case for input layer
                weights = new double[0];
            } else {
                weights = new double[prevLayer.neurons.length];
            }
            for(int j = 0; j < weights.length; j++){
                weights[j] = (Math.random() * randMax) + randMin;
            }
            double bias = (Math.random() * randMax) + randMin;

            neurons[i] = new Neuron(weights, bias, prevLayer);
        }
    }

    public void runCalculation(){
        for(int i = 0; i < neurons.length; i++){
            neurons[i].runCalculation();
        }
    }
}
