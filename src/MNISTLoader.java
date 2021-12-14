import java.io.File;
import java.util.Scanner;

class MNISTLoader {

    /**
     * Loads the first *num* images from the MNIST dataset folder.
     */
    public static MNISTImage load(int num, String directory, int compressScale){
        return new MNISTImage(directory + "/testimg-" + num + "-input.txt", compressScale);
    }

    /**
     * Loads the solutions to each training image from disk.
     * @param filename The path to the trainlabels.txt file.
     * @param n The number of training images.
     * @return An array of solutions data, such that each row consists of all zeroes except one column which has a 1 indicating it is the correct solution
     */
    public static int[] getTrainingSolutions(String filename, int n){
        try {
            File file = new File(filename);
            Scanner scanner = new Scanner(file);
            int[] solutions = new int[n];
            for(int i = 0; i < n; i++){
                solutions[i] = Integer.parseInt(scanner.nextLine());
            }
            return solutions;
        } catch(Exception e) { e.printStackTrace(); }
        return new int[0];
    }
}

class MNISTImage {
    // Each image is 28x28, so that means we have 28^2=784 points of data in each image.
    // Instead, let's sample every *n*th pixel to reduce the data size. (new size: 14x14=196)
    // *n* is the parameter compressScale and can be adjusted
    public double[] data;
    public int compressScale;

    public MNISTImage(String filename, int compressScale){
        this.compressScale = compressScale;
        try {
            data = new double[(28 / compressScale) * (28 / compressScale)];
            File file = new File(filename);
            Scanner scanner = new Scanner(file);
            int dataIndex = 0;
            for(int i = 0; i < 28; i++){
                String line = scanner.nextLine();
                for(int j = 0; j < 28; j++){
                    int tab = line.indexOf('\t');
                    if(i % compressScale == 0 && j % compressScale == 0) {
                        data[dataIndex++] = Integer.parseInt(line.substring(0, tab)) / 255.0 * 10;
                    }
                    line = line.substring(tab + 1);
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void print(){
        for(int i = 0; i < 28 / compressScale; i++){
            for(int j = 0; j < 28 / compressScale; j++){
                System.out.print(data[i * (28 / compressScale) + j] > 0 ? "█" : ".");
            }
            System.out.println();
        }
    }
}
