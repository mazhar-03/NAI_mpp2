import java.util.List;

class EvaluationMetrics {
    public static double measureAccuracy(int[] realClasses, int[] predictedClasses) {
        int correct = 0;
        for (int i = 0; i < realClasses.length; i++) {
            if (realClasses[i] == predictedClasses[i]) {
                correct++;
            }
        }
        return (double) correct / realClasses.length;
    }
}