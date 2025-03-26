import javax.swing.*;
import java.awt.*;

public class PerceptronGUI extends JFrame {
    private final JTextField feature1Field, feature2Field, feature3Field, feature4Field;
    private final JLabel resultLabel;
    private final Perceptron perceptron;
    private final double[][] testData;
    private final int[] testLabels;
    private final JComboBox<String> feature1ComboBox, feature2ComboBox;

    public PerceptronGUI(Perceptron perceptron, double[][] testData, int[] testLabels) {
        this.perceptron = perceptron;
        this.testData = testData;
        this.testLabels = testLabels;

        setTitle("Iris Classification");
        setSize(500, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Feature inputs
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Sepal Length): "), gbc);
        feature1Field = new JTextField(); gbc.gridx = 1;
        panel.add(feature1Field, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Sepal Width: "), gbc);
        feature2Field = new JTextField(); gbc.gridx = 1;
        panel.add(feature2Field, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Petal Length: "), gbc);
        feature3Field = new JTextField(); gbc.gridx = 1;
        panel.add(feature3Field, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Petal Width: "), gbc);
        feature4Field = new JTextField(); gbc.gridx = 1;
        panel.add(feature4Field, gbc);

        // Feature selection for plot
        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(new JLabel("Select Feature for X-axis:"), gbc);
        feature1ComboBox = new JComboBox<>(new String[]{"Sepal Length", "Sepal Width", "Petal Length", "Petal Width"});
        gbc.gridx = 1;
        panel.add(feature1ComboBox, gbc);

        gbc.gridx = 0; gbc.gridy = 5;
        panel.add(new JLabel("Select Feature for Y-axis:"), gbc);
        feature2ComboBox = new JComboBox<>(new String[]{"Sepal Length", "Sepal Width", "Petal Length", "Petal Width"});
        gbc.gridx = 1;
        panel.add(feature2ComboBox, gbc);

        // Predict button
        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2;
        JButton predictButton = new JButton("Predict");
        panel.add(predictButton, gbc);

        // Prediction result
        gbc.gridy = 7;
        resultLabel = new JLabel("Result: ");
        resultLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        panel.add(resultLabel, gbc);

        // Show plot button
        gbc.gridy = 8;
        JButton showPlotButton = new JButton("Show Plot");
        panel.add(showPlotButton, gbc);

        add(panel);

        predictButton.addActionListener(e -> predictClass());
        showPlotButton.addActionListener(e -> showPlot());

        setVisible(true);
    }

    private void predictClass() {
        try {
            double f1 = Double.parseDouble(feature1Field.getText());
            double f2 = Double.parseDouble(feature2Field.getText());
            double f3 = Double.parseDouble(feature3Field.getText());
            double f4 = Double.parseDouble(feature4Field.getText());

            double[] input = {f1, f2, f3, f4};
            int prediction = perceptron.predict(input);
            String predictedClass = (prediction == 1) ? "Setosa (1)" : "Versicolor (0)";
            resultLabel.setText("Result: " + predictedClass);
        } catch (NumberFormatException ex) {
            resultLabel.setText("Invalid input! Enter numeric values.");
        }
    }

    private void showPlot() {
        try {
            int feature1Index = feature1ComboBox.getSelectedIndex();
            int feature2Index = feature2ComboBox.getSelectedIndex();

            double[][] projectedTestData = new double[testData.length][2];
            for (int i = 0; i < testData.length; i++) {
                projectedTestData[i][0] = testData[i][feature1Index];
                projectedTestData[i][1] = testData[i][feature2Index];
            }

            double[][] projectedTrainData = new double[testData.length][2];
            int[] trainLabels = new int[testLabels.length];
            for (int i = 0; i < testData.length; i++) {
                projectedTrainData[i][0] = testData[i][feature1Index];
                projectedTrainData[i][1] = testData[i][feature2Index];
                trainLabels[i] = testLabels[i];
            }

            Perceptron perceptron2D = new Perceptron(2, 0.5, 0.3);
            perceptron2D.train(projectedTrainData, trainLabels, 100);

            double[] fullInput = new double[]{
                    Double.parseDouble(feature1Field.getText()),
                    Double.parseDouble(feature2Field.getText()),
                    Double.parseDouble(feature3Field.getText()),
                    Double.parseDouble(feature4Field.getText())
            };

            double[] input2D = new double[]{
                    fullInput[feature1Index],
                    fullInput[feature2Index]
            };

            JFrame plotFrame = new JFrame("Decision Boundary Plot");
            plotFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            plotFrame.add(new PlotPanel(projectedTestData, testLabels, perceptron2D, input2D, 0, 1));
            plotFrame.pack();
            plotFrame.setLocationRelativeTo(null);
            plotFrame.setVisible(true);

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Enter numeric values for selected plot features.", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
