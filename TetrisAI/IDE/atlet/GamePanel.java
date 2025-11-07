package atlet;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class GamePanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private static final int TILE = 20;
    private static final int COLS = 12, ROWS = 20;

    private Arena arena;
    private Player player;
    private AI ai;
    private Timer timer;
    private boolean gameOver = false;
    private ArrayList<Integer> leaderboard = new ArrayList<>();

    public GamePanel() {
        setPreferredSize(new Dimension(COLS * TILE + 200, ROWS * TILE));
        setBackground(Color.BLACK);
        arena = new Arena(COLS, ROWS);
        player = new Player(arena);
        ai = new AI();
    }

    public void startGame() {
        timer = new Timer(100, e -> updateGame());
        timer.start();
    }

    private void updateGame() {
        if (gameOver) return;

        ai.makeMove(player, arena);
        player.drop();

        if (player.isGameOver()) {
            gameOver = true;
            saveScore(player.getScore());
        }

        repaint();
    }

    private void saveScore(int score) {
        leaderboard.add(score);
        leaderboard.sort((a, b) -> b - a);
        if (leaderboard.size() > 10) leaderboard = new ArrayList<>(leaderboard.subList(0, 10));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        arena.draw(g, TILE);
        player.draw(g, TILE);

        // Wynik
        g.setColor(Color.WHITE);
        g.drawString("Punkty: " + player.getScore(), COLS * TILE + 20, 20);

        // Ranking
        g.drawString("Ranking TOP 10", COLS * TILE + 20, 60);
        int y = 80;
        for (int i = 0; i < leaderboard.size(); i++) {
            g.drawString((i + 1) + ". AI - " + leaderboard.get(i) + " pkt",
                         COLS * TILE + 20, y);
            y += 20;
        }

        // Game Over
        if (gameOver) {
            g.setColor(Color.RED);
            g.setFont(g.getFont().deriveFont(24f));
            g.drawString("GAME OVER", COLS * TILE / 2 - 50, ROWS * TILE / 2);
        }
    }
}
