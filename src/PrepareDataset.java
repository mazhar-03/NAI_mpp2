import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PrepareDataset {
    public static Map<String, Object> trainTestSplit(double[][] dataset, int[] labels, double trainRatio) {
        Map<String, Object> splitData = new HashMap<>();
        List<double[]> setosa = new ArrayList<>();
        List<double[]> versicolor = new ArrayList<>();
        List<Integer> setosaLabels = new ArrayList<>();
        List<Integer> versicolorLabels = new ArrayList<>();

        for (int i = 0; i < labels.length; i++) {
            if (labels[i] == 1) {
                setosa.add(dataset[i]);
                setosaLabels.add(labels[i]);
            } else {
                versicolor.add(dataset[i]);
                versicolorLabels.add(labels[i]);
            }
        }

        int trainsetosaSize = (int) (setosa.size() * trainRatio);
        int trainversicolorSize = (int) (versicolor.size() * trainRatio);

        double[][] trainData = new double[trainsetosaSize + trainversicolorSize][];
        double[][] testData = new double[setosa.size() - trainsetosaSize + versicolor.size() - trainversicolorSize][];
        int[] trainLabels = new int[trainsetosaSize + trainversicolorSize];
        int[] testLabels = new int[setosa.size() - trainsetosaSize + versicolor.size() - trainversicolorSize];

        for (int i = 0; i < trainsetosaSize; i++) {
            trainData[i] = setosa.get(i);
            trainLabels[i] = setosaLabels.get(i);
        }
        for (int i = trainsetosaSize; i < setosa.size(); i++) {
            testData[i - trainsetosaSize] = setosa.get(i);
            testLabels[i - trainsetosaSize] = setosaLabels.get(i);
        }
        for (int i = 0; i < trainversicolorSize; i++) {
            trainData[trainsetosaSize + i] = versicolor.get(i);
            trainLabels[trainsetosaSize + i] = versicolorLabels.get(i);
        }
        for (int i = trainversicolorSize; i < versicolor.size(); i++) {
            testData[setosa.size() - trainsetosaSize + i - trainversicolorSize] = versicolor.get(i);
            testLabels[setosa.size() - trainsetosaSize + i - trainversicolorSize] = versicolorLabels.get(i);
        }

        splitData.put("trainData", trainData);
        splitData.put("trainLabels", trainLabels);
        splitData.put("testData", testData);
        splitData.put("testLabels", testLabels);

        return splitData;
    }
    public static Map<String, Object> loadDataset(String filePath) {
        List<double[]> data = new ArrayList<>();
        List<Integer> labels = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            int rowIndex = 0;

            // Skip the header row
            br.readLine();

            while ((line = br.readLine()) != null && rowIndex <= 101) { // only the first 100 rows, not virginica
                rowIndex++;
                String[] values = line.split(",");
                double feature1 = Double.parseDouble(values[0]); // Example: Sepal Length
                double feature2 = Double.parseDouble(values[1]); // Example: Sepal Width
                String label = values[4];  // Class label

                // Convert labels (setosa = 1, versicolor = 0)
                int numericLabel = label.equals("Iris-setosa") ? 1 : 0;

                data.add(new double[]{feature1, feature2});
                labels.add(numericLabel);
            }
        } catch (IOException e) {
            System.err.println("Error reading CSV file: " + e.getMessage());
            return null;
        }

        Map<String, Object> datasetMap = new HashMap<>();
        datasetMap.put("data", data.toArray(new double[0][]));
        datasetMap.put("labels", labels.stream().mapToInt(i -> i).toArray());

        return datasetMap;
    }

}