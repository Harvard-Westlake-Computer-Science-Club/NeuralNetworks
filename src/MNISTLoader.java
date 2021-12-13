import java.io.File;
import java.util.Scanner;

class MNISTLoader {


//        MNISTImage[] images = MNISTLoader.load(100, "/Users/yooniverse/Desktop/mnist-digits");
//        double[][] solutions = MNISTLoader.getTrainingSolutions("/Users/yooniverse/Desktop/mnist-digits/trainlabels.txt", 100);

    /**
     * Loads the first *num* images from the MNIST dataset folder.
     */
    public static MNISTImage[] load(int num, String directory){
        MNISTImage[] dataset = new MNISTImage[num];
        for(int i = 0; i < num; i++){
            dataset[i] = new MNISTImage(directory + "/testimg-" + i + "-input.txt");
        }
        return dataset;
    }

    /**
     * Loads the solutions to each training image from disk.
     * @param filename The path to the trainlabels.txt file.
     * @param n The number of training images.
     * @return An array of solutions data, such that each row consists of all zeroes except one column which has a 1 indicating it is the correct solution
     */
    public static double[][] getTrainingSolutions(String filename, int n){
        try {
            File file = new File(filename);
            Scanner scanner = new Scanner(file);
            double[][] solutions = new double[n][10];
            for(int i = 0; i < n; i++){
                int correct = Integer.parseInt(scanner.nextLine());
                solutions[i][correct] = 1;
            }
            return solutions;
        } catch(Exception e) { e.printStackTrace(); }
        return new double[0][0];
    }
}

class MNISTImage {
    // Each image is 28x28, so that means we have 28^2=784 points of data in each image.
    public double[] data;

    public MNISTImage(String filename){
        try {
            data = new double[784];
            File file = new File(filename);
            Scanner scanner = new Scanner(file);
            int dataIndex = 0;
            for(int i = 0; i < 28; i++){
                String line = scanner.nextLine();
                for(int j = 0; j < 28; j++){
                    int tab = line.indexOf('\t');
                    data[dataIndex++] = Integer.parseInt(line.substring(0, tab)) / 255.0;
//                    System.out.print(data[dataIndex] + "\t");
                    line = line.substring(tab + 1);
                }
//                System.out.println();
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void print(){
        for(int i = 0; i < 28; i++){
            for(int j = 0; j < 28; j++){
                System.out.print(data[i * 28 + j] > 0 ? "█" : " ");
            }
            System.out.println();
        }
    }
}
