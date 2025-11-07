package atlet;

import java.io.*;
import java.util.Arrays;

public class HighScoreManager {
    private static final String SCORE_FILE = "tetris_scores.txt";
    private int[] scores;

    public HighScoreManager() {
        scores = loadHighScores();
    }

    public void saveHighScore(int score) {
        scores = Arrays.copyOf(scores, scores.length + 1);
        scores[scores.length - 1] = score;
        Arrays.sort(scores);
        reverse(scores);
        if (scores.length > 10) {
            scores = Arrays.copyOfRange(scores, 0, 10);
        }
        try (PrintWriter writer = new PrintWriter(new FileWriter(SCORE_FILE))) {
            for (int s : scores) {
                writer.println(s);
            }
        } catch (IOException e) {
            // Silent fail to keep console clean
        }
    }

    public int[] getHighScores() {
        int[] result = new int[10];
        System.arraycopy(scores, 0, result, 0, Math.min(scores.length, 10));
        return result;
    }

    private int[] loadHighScores() {
        File file = new File(SCORE_FILE);
        if (!file.exists()) {
            return new int[]{};
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            return reader.lines().mapToInt(Integer::parseInt).toArray();
        } catch (IOException e) {
            return new int[]{};
        }
    }

    private void reverse(int[] array) {
        for (int i = 0; i < array.length / 2; i++) {
            int temp = array[i];
            array[i] = array[array.length - 1 - i];
            array[array.length - 1 - i] = temp;
        }
    }
}