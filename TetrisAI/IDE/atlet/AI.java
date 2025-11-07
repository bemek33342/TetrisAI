package atlet;

public class AI {
    public void makeMove(Player player, Arena arena) {
        if (player.getPiece() == null) return;
        int[][] current = player.getPiece().getMatrix();
        Piece nextPiece = player.getNext();

        double bestScore = Double.NEGATIVE_INFINITY;
        int bestX = player.getX(), bestY = player.getY();
        int[][] bestMatrix = current;

        for (int r = 0; r < 4; r++) {
            int[][] rotated = rotateCopy(current, r);
            for (int x = -2; x <= arena.getCols() - rotated[0].length + 2; x++) {
                int y = 0;
                while (!player.collides(x, y, rotated)) y++;
                y--;
                if (y < 0) continue;

                int[][] arenaCopy = copyArena(arena.getGrid());
                mergeAt(arenaCopy, rotated, x, y);

                // lookahead dla następnego klocka
                double nextBest = Double.NEGATIVE_INFINITY;
                for (int nr = 0; nr < 4; nr++) {
                    int[][] nextRot = rotateCopy(nextPiece.getMatrix(), nr);
                    for (int nx = 0; nx <= arenaCopy[0].length - nextRot[0].length; nx++) {
                        int ny = 0;
                        while (!collideAt(nextRot, nx, ny, arenaCopy)) ny++;
                        ny--;
                        if (ny < 0) continue;
                        int[][] tempArena = copyArena(arenaCopy);
                        mergeAt(tempArena, nextRot, nx, ny);
                        double s = evaluate(tempArena);
                        if (s > nextBest) nextBest = s;
                    }
                }

                double score = (nextBest != Double.NEGATIVE_INFINITY) ? nextBest : evaluate(arenaCopy);
                if (score > bestScore) {
                    bestScore = score;
                    bestX = x;
                    bestY = y;
                    bestMatrix = rotated;
                }
            }
        }
        player.setPieceMatrix(bestMatrix);
        player.setPos(bestX, bestY);
    }

    private static int[][] rotateCopy(int[][] m, int times) {
        int[][] copy = new int[m.length][m[0].length];
        for (int y = 0; y < m.length; y++)
            for (int x = 0; x < m[y].length; x++)
                copy[y][x] = m[y][x];
        for (int i = 0; i < times; i++)
            copy = Piece.rotate(copy, 1);
        return copy;
    }

    private static boolean collideAt(int[][] m, int nx, int ny, int[][] arena) {
        for (int y = 0; y < m.length; y++) {
            for (int x = 0; x < m[y].length; x++) {
                if (m[y][x] != 0) {
                    int gx = nx + x, gy = ny + y;
                    if (gy < 0 || gy >= arena.length || gx < 0 || gx >= arena[0].length) return true;
                    if (arena[gy][gx] != 0) return true;
                }
            }
        }
        return false;
    }

    private static void mergeAt(int[][] arena, int[][] m, int px, int py) {
        for (int y = 0; y < m.length; y++) {
            for (int x = 0; x < m[y].length; x++) {
                if (m[y][x] != 0) {
                    arena[py + y][px + x] = m[y][x];
                }
            }
        }
    }

    private static int[][] copyArena(int[][] arena) {
        int[][] copy = new int[arena.length][arena[0].length];
        for (int y = 0; y < arena.length; y++)
            copy[y] = arena[y].clone();
        return copy;
    }

    // heurystyka
    private static double evaluate(int[][] arena) {
        int rows = arena.length, cols = arena[0].length;
        int aggHeight = 0, holes = 0, bump = 0, lines = 0, wells = 0;
        int[] colHeights = new int[cols];

        for (int x = 0; x < cols; x++) {
            boolean blockFound = false;
            for (int y = 0; y < rows; y++) {
                if (arena[y][x] != 0) {
                    if (!blockFound) {
                        colHeights[x] = rows - y;
                        blockFound = true;
                    }
                } else if (blockFound) {
                    holes++;
                }
            }
            aggHeight += colHeights[x];
        }

        for (int i = 0; i < cols - 1; i++)
            bump += Math.abs(colHeights[i] - colHeights[i + 1]);

        for (int y = 0; y < rows; y++) {
            boolean full = true;
            for (int x = 0; x < cols; x++)
                if (arena[y][x] == 0) { full = false; break; }
            if (full) lines++;
        }

        for (int x = 0; x < cols; x++) {
            int left = (x > 0) ? colHeights[x - 1] : 0;
            int right = (x < cols - 1) ? colHeights[x + 1] : 0;
            if (left > colHeights[x] && right > colHeights[x]) {
                wells += Math.min(left, right) - colHeights[x];
            }
        }

        return -0.51066 * aggHeight
             - 0.35663 * holes
             - 0.184483 * bump
             + 0.760666 * lines
             + 0.2 * wells;
    }
}
