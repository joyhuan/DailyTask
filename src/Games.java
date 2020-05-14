import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

public class Games {
    /**
     * Each solution contains a distinct board configuration of the n-queens' placement, where 'Q' and '.' both indicate
     * a queen and an empty space respectively.
     * @param n
     * @return all distinct solutions to the n-queens puzzle.
     */
    private List<List<String>> solveNQueens(int n) {
        char[][] board = new char[n][n];
        for(int i = 0; i<n; i++)
            for(int j = 0; j<n; j++)
                board[i][j] = '.';
        List<List<String>> res = new ArrayList<>();
        dfs(board, 0, res);
        return res;
    }

    private void dfs(char[][] board, int colIndex, List<List<String>> res){
        if(colIndex == board.length){
            res.add(construct(board));
            return;
        }
        for(int i = 0; i< board.length; i++){
            if(validate(board, i, colIndex)){
                board[i][colIndex] = 'Q';
                dfs(board, colIndex + 1, res);
                board[i][colIndex] = '.';
            }
        }
    }

    private boolean validate(char[][] board, int x, int y){
        for(int i =0; i<board.length; i++){
            for(int j = 0; j< y; j++){
                if(board[i][j] == 'Q' && (x + j == y+i || x + y == i+j || x == i))
                    return false;
            }
        }
        return true;
    }

    private List<String> construct(char[][] board){
        List<String> res = new LinkedList<>();
        for(int i = 0; i<board.length; i++){
            String s = new String(board[i]);
            res.add(s);
        }
        return res;
    }

    /**
     * 529. Minesweeper 扫雷
     *    (typical Search) my childhood game
     *    Given a 2D char matrix representing the game board.
     *    'M' represents an unrevealed mine,
     *    'E' represents an unrevealed empty square,
     *    'B' represents a revealed blank square that has no adjacent (above, below, left, right, and all 4 diagonals) mines,
     *    digit ('1' to '8') represents how many mines are adjacent to this revealed square, and finally 'X' represents a revealed mine.
     *    Update rule:
     *    1. If a mine ('M') is revealed, then the game is over - change it to 'X'.
     *    2. If an empty square ('E') with no adjacent mines is revealed, then change it
     *    to revealed blank ('B') and all of its adjacent unrevealed squares should be revealed recursively.
     *    3. If an empty square ('E') with at least one adjacent mine is revealed,
     *    then change it to a digit ('1' to '8') representing the number of adjacent mines.
     *    4. Return the board when no more squares will be revealed.
     *    Can use both DFS and BFS
     * @param board
     * @param click
     * @return
     */
    private char[][] updateBoard(char[][] board, int[] click) {
        int m = board.length, n = board[0].length;
        int row = click[0], col = click[1];

        if (board[row][col] == 'M') { // Mine
            board[row][col] = 'X';
        }
        else { // Empty
            // Get number of mines first.
            int count = 0;
            for (int i = -1; i < 2; i++) {
                for (int j = -1; j < 2; j++) {
                    if (i == 0 && j == 0) continue;
                    int r = row + i, c = col + j;
                    if (r < 0 || r >= m || c < 0 || c < 0 || c >= n) continue;
                    if (board[r][c] == 'M' || board[r][c] == 'X') count++;
                }
            }

            if (count > 0) { // If it is not a 'B', stop further DFS.
                board[row][col] = (char)(count + '0');
            }
            else { // Continue DFS to adjacent cells.
                board[row][col] = 'B';
                for (int i = -1; i < 2; i++) {
                    for (int j = -1; j < 2; j++) {
                        if (i == 0 && j == 0) continue;
                        int r = row + i, c = col + j;
                        if (r < 0 || r >= m || c < 0 || c < 0 || c >= n) continue;
                        if (board[r][c] == 'E') updateBoard(board, new int[] {r, c});
                    }
                }
            }
        }

        return board;
    }

    private class Node {
        public Node parent;
        public int[][] matrix;

        // Blank tile cordinates
        public int x, y;

        // Number of misplaced tiles
        public int cost;

        // The number of moves so far
        public int level;

        private Node(int[][] matrix, int x, int y, int newX, int newY, int level, Node parent) {
            this.parent = parent;
            this.matrix = new int[matrix.length][];
            for (int i = 0; i < matrix.length; i++) {
                this.matrix[i] = matrix[i].clone();
            }

            // Swap value
            this.matrix[x][y]       = this.matrix[x][y] + this.matrix[newX][newY];
            this.matrix[newX][newY] = this.matrix[x][y] - this.matrix[newX][newY];
            this.matrix[x][y]       = this.matrix[x][y] - this.matrix[newX][newY];

            this.cost = Integer.MAX_VALUE;
            this.level = level;
            this.x = newX;
            this.y = newY;
        }
    }

    private class Puzzle {
        private int dimension = 3;

        // Bottom, left, top, right
        int[] row = {1, 0, -1, 0};
        int[] col = {0, -1, 0, 1};

        private int calculateCost(int[][] initial, int[][] goal) {
            int count = 0;
            int n = initial.length;
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (initial[i][j] != 0 && initial[i][j] != goal[i][j]) {
                        count++;
                    }
                }
            }
            return count;
        }

        private void printMatrix(int[][] matrix) {
            for (int i = 0; i < matrix.length; i++) {
                for (int j = 0; j < matrix.length; j++) {
                    System.out.print(matrix[i][j] + " ");
                }
                System.out.println();
            }
        }

        private boolean isSafe(int x, int y) {
            return (x >= 0 && x < dimension && y >= 0 && y < dimension);
        }

        private void printPath(Node root) {
            if (root == null) {
                return;
            }
            printPath(root.parent);
            printMatrix(root.matrix);
            System.out.println();
        }

        /* We count the # of invertibles to determine is the problem is solvable/
           # of invertibles is the # of grids that are placed out of order
           Notice that any move does not change the # of invertibles.
           The goal is to have 0 intervible (even #)
        */
        private boolean isSolvable(int[][] matrix) {
            int count = 0;
            List<Integer> array = new ArrayList<Integer>();

            for (int i = 0; i < matrix.length; i++) {
                for (int j = 0; j < matrix.length; j++) {
                    array.add(matrix[i][j]);
                }
            }

            Integer[] anotherArray = new Integer[array.size()];
            array.toArray(anotherArray);

            for (int i = 0; i < anotherArray.length - 1; i++) {
                for (int j = i + 1; j < anotherArray.length; j++) {
                    if (anotherArray[i] != 0 && anotherArray[j] != 0 && anotherArray[i] > anotherArray[j]) {
                        count++;
                    }
                }
            }

            return count % 2 == 0;
        }

        private void solve(int[][] initial, int[][] goal, int x, int y) {
            PriorityQueue<Node> pq = new PriorityQueue<Node>(1000, (a, b) -> (a.cost + a.level) - (b.cost + b.level));
            Node root = new Node(initial, x, y, x, y, 0, null);
            root.cost = calculateCost(initial, goal);
            pq.add(root);

            while (!pq.isEmpty()) {
                Node min = pq.poll();
                if (min.cost == 0) {
                    printPath(min);
                    return;
                }

                for (int i = 0; i < 4; i++) {
                    if (isSafe(min.x + row[i], min.y + col[i])) {
                        Node child = new Node(min.matrix, min.x, min.y, min.x + row[i], min.y + col[i], min.level + 1, min);
                        child.cost = calculateCost(child.matrix, goal);
                        pq.add(child);
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        int[][] initial = { {1, 8, 2}, {0, 4, 3}, {7, 6, 5} };
        int[][] goal    = { {1, 2, 3}, {4, 5, 6}, {7, 8, 0} };

        // White tile coordinate
        int x = 1, y = 0;
        System.out.println(x);
//            Puzzle puzzle = new Puzzle();
//            if (puzzle.isSolvable(initial)) {
//                puzzle.solve(initial, goal, x, y);
//            }
//            else {
//                System.out.println("The given initial is impossible to solve");
//            }
    }
}
