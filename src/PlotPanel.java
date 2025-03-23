import javax.swing.*;
import java.awt.*;


public class PlotPanel extends JPanel {
    private double[][] testData;
    private int[] testLabels;
    private Perceptron perceptron;
    private double[] customPoint;

    public PlotPanel(double[][] testData, int[] testLabels, Perceptron perceptron, double[] customPoint) {
        this.testData = testData;
        this.testLabels = testLabels;
        this.perceptron = perceptron;
        this.customPoint = customPoint;
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        int width = getWidth();
        int height = getHeight();

        // Find min and max values for scaling
        double minX = Double.MAX_VALUE, maxX = Double.MIN_VALUE;
        double minY = Double.MAX_VALUE, maxY = Double.MIN_VALUE;

        for (double[] point : testData) {
            minX = Math.min(minX, point[0]);
            maxX = Math.max(maxX, point[0]);
            minY = Math.min(minY, point[1]);
            maxY = Math.max(maxY, point[1]);
        }

        double padding = 0.5;
        minX -= padding;
        maxX += padding;
        minY -= padding;
        maxY += padding;

        // Coordinate transform
        double scaleX = width / (maxX - minX);
        double scaleY = height / (maxY - minY);

        // Draw test data points
        for (int i = 0; i < testData.length; i++) {
            double x = testData[i][0];
            double y = testData[i][1];
            int px = (int) ((x - minX) * scaleX);
            int py = height - (int) ((y - minY) * scaleY);

            if (testLabels[i] == 1) {
                g2.setColor(Color.GREEN); // Setosa
            } else {
                g2.setColor(Color.RED); // Versicolor
            }
            g2.fillOval(px - 4, py - 4, 8, 8);
        }

        // Draw decision boundary: w1*x + w2*y = threshold
        double[] weights = perceptron.getWeights();
        double threshold = perceptron.getThreshold();

        if (weights[1] != 0) {
            // solve for y: y = (-w1/w2)*x + (threshold / w2)
            double x1 = minX;
            double x2 = maxX;
            double y1 = ((weights[0] * x1 - threshold) / weights[1]) * -1;
            double y2 = ((weights[0] * x2 - threshold) / weights[1]) * -1;

            int px1 = (int) ((x1 - minX) * scaleX);
            int py1 = height - (int) ((y1 - minY) * scaleY);
            int px2 = (int) ((x2 - minX) * scaleX);
            int py2 = height - (int) ((y2 - minY) * scaleY);

            g2.setColor(Color.BLUE);
            g2.setStroke(new BasicStroke(2));
            g2.drawLine(px1, py1, px2, py2);
        }
        if (customPoint != null) {
            double x = customPoint[0];
            double y = customPoint[1];
            int px = (int) ((x - minX) * scaleX);
            int py = height - (int) ((y - minY) * scaleY);

            g2.setColor(Color.MAGENTA); // Highlighted custom point
            g2.fillOval(px - 6, py - 6, 12, 12);
            g2.drawString("Your Input", px + 8, py);
        }
    }
}
