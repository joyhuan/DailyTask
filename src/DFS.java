import java.util.ArrayList;
import java.util.HashMap;

public class DFS {
    public class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;
        TreeNode() {}
        TreeNode(int val) { this.val = val; }
        TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val;
            this.left = left;
            this.right = right;
        }
    }

    /** 1530. Number of Good Leaf Nodes Pairs
     * Given the root of a binary tree and an integer distance. A pair of two different leaf nodes of a binary tree is
     * said to be good if the length of the shortest path between them is less than or equal to distance.
     * @param root
     * @param distance
     * @return the number of good leaf node pairs in the tree.
     */
    private int res;

    public int countPairs(TreeNode root, int distance) {

        res = 0;
        helper(root, distance);
        return res;
    }

    private int[] helper(TreeNode node, int distance) {

        if (node == null) return new int[11];

        int[] left = helper(node.left, distance);
        int[] right = helper(node.right, distance);

        int[] A = new int[11];

        // node is leaf node, no child, just return
        if (node.left == null && node.right == null) {
            A[1] = 1;
            return A;
        }

        // find all nodes satisfying distance
        for (int i = 0; i <= 10; ++i) {
            for (int j = 0; j <= 10; ++j) {
                if (i + j <= distance) res += (left[i] * right[j]);
            }
        }

        // increment all by 1, ignore the node distance larger than 10
        for (int i = 0; i <= 9; ++i) {
            A[i + 1] += left[i];
            A[i + 1] += right[i];
        }

        return A;
    }

    /** My Solution
     * The key optimization is the distance map of length 11
     * So for the add phase, better to do extensive search from distance to required distance
     * rather than the other way round
     */
    public int countPairs2(TreeNode root, int distance) {
        int count = 0;
        ArrayList<Integer> result = depthToLeaves(root);
        HashMap<Integer, Integer> distanceToTimes = new HashMap<>();
        for(int i: result){
            if(distanceToTimes.containsKey(i)) distanceToTimes.put(i, distanceToTimes.get(i)+1);
            else distanceToTimes.put(i, 1);
        }
        for(int j = 1; j < distance/2; j++){
            if(j != distance - j){
                if(distanceToTimes.containsKey(j) && distanceToTimes.containsKey(distance - j))
                    count += distanceToTimes.get(j) * distanceToTimes.get(distance - j);
            }else{
                if(distanceToTimes.containsKey(j) && distanceToTimes.get(j) > 1){
                    count += factorial(distanceToTimes.get(j))/2;
                }
            }
        }
        return count;
    }

    public static int factorial(int n)
    {
        if (n == 0)
            return 1;

        return n*factorial(n-1);
    }

    public ArrayList<Integer> depthToLeaves(TreeNode root){
        ArrayList<Integer> result = new ArrayList<>();
        if(root == null) return result;
        if(root.left == null && root.right == null) result.add(1);
        if(root.left != null) result.addAll(depthToLeaves(root.left));
        if(root.right != null) result.addAll(depthToLeaves(root.left));
        return result;
    }
}
