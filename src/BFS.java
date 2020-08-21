import java.nio.channels.ScatteringByteChannel;
import java.util.*;

public class BFS {

    class Solution {
        /**
         * 317. Shortest Distance from All Buildings
         * 难能之处在于逆向思维 从房子入手留路上的痕迹 这样就避免重复
         * Each 0 marks an empty land which you can pass by freely.
         * Each 1 marks a building which you cannot pass through.
         * Each 2 marks an obstacle which you cannot pass through.
         * @param grid
         * @return shortest distance to all the houses
         */
        public int shortestDistance(int[][] grid) {
            if (grid == null || grid.length == 0) return -1;
            int rows = grid.length;
            int cols = grid[0].length;

            int[][] dist = new int[rows][cols];
            int[][] reach = new int[rows][cols];

            int totalBuildings = 0;
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    if (grid[i][j] == 1) {
                        bfs(grid, reach, dist, i, j);
                        totalBuildings++;
                    }
                }
            }
            int minDistance = Integer.MAX_VALUE;
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    if (grid[i][j] == 0 && reach[i][j] == totalBuildings)
                        minDistance = Math.min(minDistance, dist[i][j]);
                }
            }
            return minDistance == Integer.MAX_VALUE ? -1 : minDistance;
        }
        private void bfs(int[][] grid, int[][] reach, int[][] dist, int row, int col) {
            int rows = grid.length;
            int cols = grid[0].length;

            Queue<int[]> queue = new LinkedList<>();
            queue.offer(new int[]{row, col});
            boolean[][] visited = new boolean[rows][cols];
            visited[row][col] = true;

            int[][] dirs = {{1, 0}, {0, 1}, {-1, 0}, {0, -1}};

            int distance = 0;

            while (!queue.isEmpty()) {
                distance++;

                for (int count = queue.size(); count > 0; count--) {
                    int[] current = queue.poll();
                    int x = current[0];
                    int y = current[1];

                    for (int[] dir : dirs) {
                        int nx = x + dir[0];
                        int ny = y + dir[1];

                        if (!isValid(grid, nx, ny, visited)) continue;

                        queue.offer(new int[] {nx, ny});
                        visited[nx][ny] = true;
                        reach[nx][ny]++;
                        dist[nx][ny] += distance;
                    }
                }
            }
        }
        private boolean isValid(int[][] grid, int row, int col, boolean[][] visited) {
            return row >= 0 && row < grid.length && col >= 0 && col < grid[0].length &&
                    !visited[row][col] && grid[row][col] == 0;
        }
    }

    /**
     * 269. Alien Dictionary
     * Meat在于建图时degree的设计 大大简化建order过程
     * @param grid
     * words are sorted lexicographically by the rules of this new language.
     * @return Derive the order of letters in this language.
     */
    public String alienOrder(String[] words) throws Exception {
        // corner case
        if(words == null || words.length == 0) return "";

        Map<Character, Set<Character>> map = new HashMap<>();
        Map<Character, Integer> degree = new HashMap<>();
        initializeDegree(degree, words);
        try{
            buildMapAndDegree(map, degree, words);
        }catch (Exception e){
            return "";
        }
        return buildOrder(map, degree);
    }

    private void initializeDegree(Map<Character, Integer> degree, String[] words){
        for(String s: words){
            for(char c: s.toCharArray())
                degree.put(c,0);
        }
    }

    private void buildMapAndDegree(Map<Character, Set<Character>> map, Map<Character, Integer> degree, String[] words) throws Exception {
        for(int i =0; i<words.length-1; i++){
            int j =0;
            while(j<words[i].length() && j<words[i+1].length() && words[i].charAt(j) == words[i+1].charAt(j)){
                j++;
            }
            if(j!=words[i].length()){
                if(j == words[i+1].length()) throw new Exception();
                Set<Character> set = new HashSet<>();
                if(map.containsKey(words[i].charAt(j))) set = map.get(words[i].charAt(j));
                if(!set.contains(words[i+1].charAt(j))){
                    set.add(words[i+1].charAt(j));
                    map.put(words[i].charAt(j),set);
                    degree.put(words[i+1].charAt(j), degree.get(words[i+1].charAt(j))+1);
                }
            }
        }
    }

    private String buildOrder(Map<Character, Set<Character>> map, Map<Character, Integer> degree){
        String result = "";
        Queue<Character> q = new LinkedList<Character>();
        for(char c: degree.keySet()){
            if(degree.get(c) == 0) q.add(c);
        }
        while(!q.isEmpty()){
            char c = q.remove();
            result +=c;
            if(map.containsKey(c)){
                for(char c2: map.get(c)){
                    degree.put(c2,degree.get(c2)-1);
                    if(degree.get(c2) == 0) q.add(c2);
                }
            }
        }

        if(result.length() != degree.size()) return "";
        return result;
    }

}
