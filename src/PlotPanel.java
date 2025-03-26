import javax.swing.*;
import java.awt.*;

public class PlotPanel extends JPanel {
    private final double[][] testData;
    private final int[] testLabels;
    private final Perceptron perceptron;
    private final double[] customPoint;
    private final int feature1Index;
    private final int feature2Index;

    public PlotPanel(double[][] testData, int[] testLabels, Perceptron perceptron, double[] customPoint, int feature1Index, int feature2Index) {
        this.testData = testData;
        this.testLabels = testLabels;
        this.perceptron = perceptron;
        this.customPoint = customPoint;
        this.feature1Index = feature1Index;
        this.feature2Index = feature2Index;
        setPreferredSize(new Dimension(500, 500));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        int width = getWidth();
        int height = getHeight();

        double minX = Double.MAX_VALUE, maxX = Double.MIN_VALUE;
        double minY = Double.MAX_VALUE, maxY = Double.MIN_VALUE;

        for (double[] point : testData) {
            minX = Math.min(minX, point[0]);
            maxX = Math.max(maxX, point[0]);
            minY = Math.min(minY, point[1]);
            maxY = Math.max(maxY, point[1]);
        }
        if (customPoint != null) {
            minX = Math.min(minX, customPoint[0]);
            maxX = Math.max(maxX, customPoint[0]);
            minY = Math.min(minY, customPoint[1]);
            maxY = Math.max(maxY, customPoint[1]);
        }

        double padding = 0.5;
        minX -= padding; maxX += padding;
        minY -= padding; maxY += padding;

        double scaleX = width / (maxX - minX);
        double scaleY = height / (maxY - minY);

        for (int i = 0; i < testData.length; i++) {
            double x = testData[i][0];
            double y = testData[i][1];
            int px = (int) ((x - minX) * scaleX);
            int py = height - (int) ((y - minY) * scaleY);

            g2.setColor(testLabels[i] == 1 ? Color.GREEN : Color.RED);
            g2.fillOval(px - 4, py - 4, 8, 8);
        }

        // w1*x + w2*y = threshold --> y = (-w1*x + threshold)/w2
        double[] weights = perceptron.getWeights();
        double threshold = perceptron.getThreshold();
        double w1 = weights[feature1Index];
        double w2 = weights[feature2Index];

        if (Math.abs(w2) > 1e-6) {
            double x1 = minX;
            double x2 = maxX;
            double y1 = (-w1 * x1 + threshold) / w2;
            double y2 = (-w1 * x2 + threshold) / w2;

            int px1 = (int) ((x1 - minX) * scaleX);
            int py1 = height - (int) ((y1 - minY) * scaleY);
            int px2 = (int) ((x2 - minX) * scaleX);
            int py2 = height - (int) ((y2 - minY) * scaleY);

            g2.setColor(Color.BLUE);
            g2.setStroke(new BasicStroke(2));
            g2.drawLine(px1, py1, px2, py2);
            g2.setColor(Color.BLACK);
        }

        if (customPoint != null) {
            double x = customPoint[0];
            double y = customPoint[1];
            int px = (int) ((x - minX) * scaleX);
            int py = height - (int) ((y - minY) * scaleY);

            g2.setColor(Color.MAGENTA);
            g2.fillOval(px - 6, py - 6, 12, 12);
            g2.setColor(Color.BLACK);
            g2.drawString("Your Input", px + 8, py);
        }
    }
}