import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PerceptronGUI extends JFrame {
    private JTextField feature1Field, feature2Field;
    private JLabel resultLabel;
    private Perceptron perceptron;
    private double[][] testData;
    private int[] testLabels;

    public PerceptronGUI(Perceptron perceptron, double[][] testData, int[] testLabels) {
        this.perceptron = perceptron;
        this.testData = testData;
        this.testLabels = testLabels;

        setTitle("Iris Classification");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main content panel
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Row 1: Feature 1
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Feature 1:"), gbc);

        gbc.gridx = 1;
        feature1Field = new JTextField();
        panel.add(feature1Field, gbc);

        // Row 2: Feature 2
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Feature 2:"), gbc);

        gbc.gridx = 1;
        feature2Field = new JTextField();
        panel.add(feature2Field, gbc);

        // Row 3: Predict button
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        JButton predictButton = new JButton("Predict");
        panel.add(predictButton, gbc);

        // Row 4: Result label
        gbc.gridy = 3;
        resultLabel = new JLabel("Result: ");
        resultLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        panel.add(resultLabel, gbc);

        // Row 5: Show Plot button
        gbc.gridy = 4;
        JButton showPlotButton = new JButton("Show Plot");
        panel.add(showPlotButton, gbc);

        add(panel);

        // Button Actions
        predictButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                predictClass();
            }
        });

        showPlotButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showPlot();
            }
        });

        setVisible(true);
    }

    private void predictClass() {
        try {
            double feature1 = Double.parseDouble(feature1Field.getText());
            double feature2 = Double.parseDouble(feature2Field.getText());

            double[] input = {feature1, feature2};
            int prediction = perceptron.predict(input);

            String predictedClass = (prediction == 1) ? "Setosa (1)" : "Versicolor (0)";
            resultLabel.setText("Result: " + predictedClass);
        } catch (NumberFormatException ex) {
            resultLabel.setText("Invalid input! Enter numeric values.");
        }
    }

    private void showPlot() {
        double[] input = null;
        try {
            input = new double[]{
                    Double.parseDouble(feature1Field.getText()),
                    Double.parseDouble(feature2Field.getText())
            };
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        JFrame plotFrame = new JFrame("Decision Boundary Plot");
        plotFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        plotFrame.add(new PlotPanel(testData, testLabels, perceptron, input));
        plotFrame.pack();
        plotFrame.setLocationRelativeTo(null);
        plotFrame.setVisible(true);
    }

}
