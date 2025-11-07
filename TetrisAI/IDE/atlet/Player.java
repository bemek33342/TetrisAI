package atlet;

import java.awt.*;

public class Player {
    private int x, y;
    private Piece piece, next;
    private Arena arena;
    private int score = 0;
    private boolean gameOver = false;

    public Player(Arena arena) {
        this.arena = arena;
        next = Piece.randomPiece();
        reset();
    }

    public void reset() {
        piece = next;
        next = Piece.randomPiece();
        x = arena.getCols() / 2 - piece.getMatrix()[0].length / 2;
        y = 0;
        if (collides(x, y, piece.getMatrix())) {
            gameOver = true;
        }
    }

    public void drop() {
        if (!collides(x, y + 1, piece.getMatrix())) {
            y++;
        } else {
            arena.merge(piece, x, y);
            arena.sweep(this);
            reset();
        }
    }

    public boolean collides(int nx, int ny, int[][] m) {
        int[][] grid = arena.getGrid();
        for (int yy = 0; yy < m.length; yy++) {
            for (int xx = 0; xx < m[yy].length; xx++) {
                if (m[yy][xx] != 0) {
                    int gx = nx + xx, gy = ny + yy;
                    if (gy < 0 || gy >= arena.getRows() || gx < 0 || gx >= arena.getCols()) return true;
                    if (grid[gy][gx] != 0) return true;
                }
            }
        }
        return false;
    }

    public void draw(Graphics g, int tile) {
        int[][] m = piece.getMatrix();
        for (int yy = 0; yy < m.length; yy++) {
            for (int xx = 0; xx < m[yy].length; xx++) {
                if (m[yy][xx] != 0) {
                    g.setColor(Piece.COLORS[m[yy][xx]]);
                    g.fillRect((x + xx) * tile, (y + yy) * tile, tile, tile);
                    g.setColor(Color.BLACK);
                    g.drawRect((x + xx) * tile, (y + yy) * tile, tile, tile);
                }
            }
        }
    }

    public int getScore() { return score; }
    public void addScore(int s) { score += s; }
    public boolean isGameOver() { return gameOver; }
    public Piece getPiece() { return piece; }
    public Piece getNext() { return next; }
    public int getX() { return x; }
    public int getY() { return y; }
    public void setPos(int nx, int ny) { x = nx; y = ny; }
    public void setPieceMatrix(int[][] m) { piece = new Piece(m); }
}
