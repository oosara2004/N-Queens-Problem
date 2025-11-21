import java.util.*;

public class ForwardChecking {

    public static long constraintChecks = 0;

    public static boolean intilize(int[] board) {
        constraintChecks = 0;
        int n = board.length;

        boolean[][] Placement = new boolean[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                Placement[i][j] = true;

            }
            board[i] = -1;
        }
        return FC(board, 0, Placement);

    }

    private static boolean FC(int[] board, int coloumn, boolean[][] currentPlacement) {

        int n = board.length;

        if (coloumn == n) {
            return true;
        }

         List<Integer> rows = new ArrayList<>();
        for (int r = 0; r < n; r++) {
            if (currentPlacement[coloumn][r] == true) {
                rows.add(r);
            }
        }
        Collections.shuffle(rows);

        for (int row : rows) {
             board[coloumn] = row;

             boolean[][] newPlacment = copyPlacment(currentPlacement);

             boolean possible = forwardcheck(newPlacment, coloumn, row);

            if (possible) {
                if (FC(board, coloumn + 1, newPlacment)) {
                    return true;
                }
            }
             board[coloumn] = -1;
        }
        return false;
    }

     private static boolean forwardcheck(boolean[][] placement, int coloumn, int row) {
        int n = placement.length;

        for (int nextColoumn = coloumn + 1; nextColoumn < n; nextColoumn++) {

            boolean safeplacment = false;

            for (int r = 0; r < n; r++) {

                if (placement[nextColoumn][r] == false) {
                    continue;
                }

                constraintChecks++;

                 boolean sameRow = (r == row);
                boolean sameDiagonal = (Math.abs(nextColoumn - coloumn) == Math.abs(r - row));

                if (sameRow || sameDiagonal) {
                    placement[nextColoumn][r] = false;  
                } else {
                    safeplacment = true;
                }
            }
            if (!safeplacment) {
                return false;
            }

        }
        return true;
    }

    private static boolean[][] copyPlacment(boolean[][] original) {
        int n = original.length;
        boolean[][] copy = new boolean[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                copy[i][j] = original[i][j];
            }
        }
        return copy;
    }

}