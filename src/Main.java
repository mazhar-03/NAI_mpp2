import java.util.Map;

public class Main {
    public static void main(String[] args) {
        String filePath = "resources/iris.csv";
        Map<String, Object> dataset = PrepareDataset.loadDataset(filePath);

        if (dataset == null) {
            System.out.println("Error loading dataset.");
            return;
        }

        double[][] datasetArray = (double[][]) dataset.get("data");
        int[] labels = (int[]) dataset.get("labels");

        Map<String, Object> splitData = PrepareDataset.trainTestSplit(datasetArray, labels, 0.7);

        double[][] trainData = (double[][]) splitData.get("trainData");
        int[] trainLabels = (int[]) splitData.get("trainLabels");
        double[][] testData = (double[][]) splitData.get("testData");
        int[] testLabels = (int[]) splitData.get("testLabels");

        int dimension = trainData[0].length;
        Perceptron perceptron = new Perceptron(dimension, 0.5, 0.3);

        int epochs = 100;
        perceptron.train(trainData, trainLabels, epochs);

        int[] predictions = new int[testData.length];
        for (int i = 0; i < testData.length; i++) {
            predictions[i] = perceptron.predict(testData[i]);
        }

        double accuracy = EvaluationMetrics.measureAccuracy(testLabels, predictions);
        System.out.println("Test Accuracy: " + (int)((accuracy * 100)) + "%");

        PerceptronGUI gui = new PerceptronGUI(perceptron, testData, testLabels);
    }
}
