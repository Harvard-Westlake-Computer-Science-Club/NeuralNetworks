import java.util.Arrays;
import java.util.Comparator;

class Main {
    /*
    public static void main(String[] args){
        // Let's train a NN that can add two numbers!
        // You can make up your own simple algorithm and have the computer guess - just add a few data points below and
        // then train, and then add test cases to see if it found the algorithm!
        double[][] inputs = {
                {1, 2},
                {2, 4},
                {5, 2},
                {10, 2},
                {5, 5},
                {100}, {-50}
        };
        double[][] solutions = {
                {3}, {6}, {7}, {12}, {10}, {50}
        };
        NeuralNetwork nn = trainByEvolution(new int[]{inputs[0].length, 10, 10, solutions[0].length}, -2, 2, 0.001, 0.02, inputs, solutions);
//        NeuralNetwork nn = trainByHillClimber(new int[]{inputs[0].length, 10, 10, solutions[0].length}, -2, 2, 0.001, 0.02, inputs, solutions);

        // Let's run a calculation that *wasn't* included in our training set to make sure the NN actually found our
        // solution, and wasn't just overfitting to our training dataset.
        System.out.println(nn.runCalculation(new double[]{9, 10})[0]); // --> should output 19
        System.out.println(nn.runCalculation(new double[]{4, 5})[0]); // --> should output 9
        System.out.println(nn.runCalculation(new double[]{10, -5})[0]); // --> should output 5
    }
    */

    public static void main(String[] args){

        int numToLoad = 400;
        String dir = "/Users/yooniverse/Desktop/mnist-digits";
        double[][] inputs = new double[numToLoad][784];
        for(int i = 0; i < numToLoad; i++){
            inputs[i] = MNISTLoader.load(i, dir, 2).data;
        }
        int[] solutions = MNISTLoader.getTrainingSolutions("/Users/yooniverse/Desktop/mnist-digits/trainlabels.txt", 59999);
        MNISTLoader.load(0, dir, 1).print();
        MNISTLoader.load(0, dir, 2).print();
        NeuralNetwork nn = trainByEvolutionCategorical(new int[]{inputs[0].length, 20, 20, 20, 20, 1}, -0.1, 0.1, 0.01, 3700, inputs, solutions);
        int output = (int)nn.runCalculation(MNISTLoader.load(101, dir, 2).data)[0];
        System.out.println("Predicted: " + output + "\t\tActual: " + solutions[101]);

        Drawing drawing = new Drawing(2, nn);
    }

    public static double calculateError(NeuralNetwork nn, double[][] inputs, double[][] solutions){
        double error = 0;
        for(int i = 0; i < inputs.length; i++) {
            double[] prediction = nn.runCalculation(inputs[i]);
            for(int j = 0; j < prediction.length; j++){
                error += Math.abs(solutions[i][j] - prediction[j]);
            }
        }
        return error;
    }

    public static int calculateErrorCategorical(NeuralNetwork nn, double[][] inputs, int[] solutions){
        int error = 0;
        for(int i = 0; i < inputs.length; i++) {
            int prediction = (int)nn.runCalculation(inputs[i])[0];
            if(prediction < 0){
                error -= prediction;
            } else if(prediction > 10) {
                error += prediction - 10;
            }
            if(prediction != solutions[i]) error++;
        }
        return error;
    }


    public static NeuralNetwork trainByHillClimber(int[] neuronCounts, double initialRandMin, double initialRandMax, double mutationAmt, double successThreshold, double[][] inputs, double[][] solutions){
        NeuralNetwork nn = new NeuralNetwork(neuronCounts, initialRandMin, initialRandMax);

        // Hill climber: mutate randomly a little bit, then check if our mutated model is better than our
        // old one. If so, use that model from now on. Otherwise, revert our mutations and try again.

        double error = calculateError(nn, inputs, solutions);
        while(error > successThreshold) {
            NeuralNetwork copy = nn.deepCopy();
            // Mutate a certain amount depending on how far from solution we are
            copy.mutate(mutationAmt * error);
            double newError = calculateError(copy, inputs, solutions);
            if(newError < error){
                error = newError;
                nn = copy;
            }
            System.out.println(error);
        }
        return nn;
    }

    public static NeuralNetwork trainByEvolution(int[] neuronCounts, double initialRandMin, double initialRandMax, double mutationAmt, double successThreshold, double[][] inputs, double[][] solutions){

        // Evolution (without breeding): create a population of 100 models. The bottom 20% die; the middle 60% survive (and get mutated);
        // and the top 20% get cloned (and the clones are mutated).

        NeuralNetwork[] nets = new NeuralNetwork[100];
        for(int i = 0; i < nets.length; i++){
            nets[i] = new NeuralNetwork(neuronCounts, initialRandMin, initialRandMax);
        }
        while(true){
            // Calculate error of all the models and store it as an instance variable
            for(NeuralNetwork nn : nets){
                nn.error = calculateError(nn, inputs, solutions);
            }

            // The code below sorts the models by their error.
            Arrays.sort(nets, new Comparator<NeuralNetwork>() {
                @Override
                public int compare(NeuralNetwork o1, NeuralNetwork o2) {
                    if(o1.error < o2.error) return 1;
                    if(o2.error < o1.error) return -1;
                    return 0;
                }
            });

            // return if best net is better than threshold
            System.out.println("Best: " + nets[99].error + "\t\tMedian: " + nets[50].error + "\t\tWorst: " + nets[0].error);
            if(nets[99].error < successThreshold) return nets[99];

            // replace bottom 20% with clones of top 20%
            for(int i = 0; i < 20; i++){
                nets[i] = nets[80 + i].deepCopy();
            }

            // Mutate bottom 80%
            for(int i = 0; i < 80; i++){
                // Mutate a certain amount depending on how far from solution we are
                nets[i].mutate(mutationAmt * nets[i].error);
            }
        }
    }

    public static NeuralNetwork trainByEvolutionCategorical(int[] neuronCounts, double initialRandMin, double initialRandMax, double mutationAmt, double successThreshold, double[][] inputs, int[] solutions){
        NeuralNetwork[] nets = new NeuralNetwork[100];
        for(int i = 0; i < nets.length; i++){
            nets[i] = new NeuralNetwork(neuronCounts, initialRandMin, initialRandMax);
        }
        while(true){
            for(NeuralNetwork nn : nets){
                nn.error = calculateErrorCategorical(nn, inputs, solutions);
            }
            Arrays.sort(nets, new Comparator<NeuralNetwork>() {
                @Override
                public int compare(NeuralNetwork o1, NeuralNetwork o2) {
                    if(o1.error < o2.error) return 1;
                    if(o2.error < o1.error) return -1;
                    return 0;
                }
            });

//            for(Layer l : nets[50].layers)
//                for(Neuron n : l.neurons)
//                    for(double w : n.weights)
//                        System.out.print(w + "\t");
            System.out.println(nets[50].runCalculation(inputs[0])[0]);

            System.out.println("Best: " + nets[99].error + "\t\tMedian: " + nets[50].error + "\t\tWorst: " + nets[0].error);
            if(nets[99].error < successThreshold) return nets[99];
            for(int i = 0; i < 20; i++){
                nets[i] = nets[80 + i].deepCopy();
            }
            for(int i = 0; i < 80; i++){
                nets[i].mutate(mutationAmt);
            }
        }

    }
}
