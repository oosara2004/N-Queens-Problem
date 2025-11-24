
import java.util.*;

public class MAC {

    public static long constraintChecks = 0;

    public static boolean solve(int[] board) {
        constraintChecks = 0;
        int n = board.length;

        List<Set<Integer>> domains = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            Set<Integer> d = new HashSet<>();
            for (int r = 0; r < n; r++) d.add(r);
            domains.add(d);
        }

        boolean result = backtrack(board, 0, domains);
        System.out.println("ConstraintCheckCounter(MAC) = " + constraintChecks);
        return result;
    }

    private static boolean backtrack(int[] board, int col, List<Set<Integer>> domains) {
        int n = board.length;
        if (col == n) return true;

        for (int value : new HashSet<>(domains.get(col))) {
            int oldValue = board[col];
            board[col] = value;

            List<Set<Integer>> newDomains = deepCopy(domains);
            if (AC3(board, newDomains)) {
                if (backtrack(board, col + 1, newDomains))
                    return true;
            }
            board[col] = oldValue;
        }
        return false;
    }

    private static boolean AC3(int[] board, List<Set<Integer>> domains) {
        Queue<int[]> queue = new LinkedList<>();
        int n = domains.size();
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                if (i != j)
                    queue.add(new int[]{i, j});

        while (!queue.isEmpty()) {
            int[] arc = queue.poll();
            int Xi = arc[0], Xj = arc[1];
            if (revise(domains, Xi, Xj)) {
                if (domains.get(Xi).isEmpty()) return false;
                for (int Xk = 0; Xk < n; Xk++)
                    if (Xk != Xi && Xk != Xj)
                        queue.add(new int[]{Xk, Xi});
            }
        }
        return true;
    }

    private static boolean revise(List<Set<Integer>> domains, int Xi, int Xj) {
        boolean revised = false;
        Set<Integer> toRemove = new HashSet<>();
        for (int vi : domains.get(Xi)) {
            boolean supported = false;
            for (int vj : domains.get(Xj)) {
                constraintChecks++;
                if (!conflict(Xi, vi, Xj, vj)) {
                    supported = true;
                    break;
                }
            }
            if (!supported) toRemove.add(vi);
        }
        domains.get(Xi).removeAll(toRemove);
        return !toRemove.isEmpty();
    }

    private static boolean conflict(int c1, int r1, int c2, int r2) {
        if (r1 == r2) return true;
        return Math.abs(r1 - r2) == Math.abs(c1 - c2);
    }

    private static List<Set<Integer>> deepCopy(List<Set<Integer>> domains) {
        List<Set<Integer>> copy = new ArrayList<>();
        for (Set<Integer> s : domains) copy.add(new HashSet<>(s));
        return copy;
    }
}

