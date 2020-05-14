import java.util.HashMap;

public class DP {
    /**
     * 978. Longest Turbulent Subarray
     *   A subarray A[i], A[i+1], ..., A[j] of A is said to be turbulent if and only if:
     *
     *   For i <= k < j, A[k] > A[k+1] when k is odd, and A[k] < A[k+1] when k is even;
     *   OR, for i <= k < j, A[k] > A[k+1] when k is even, and A[k] < A[k+1] when k is odd.
     *   That is, the subarray is turbulent if the comparison sign flips between each adjacent
     *   pair of elements in the subarray.
     *   Return the length of a maximum size turbulent subarray of A.
     *
     *   Personal Thoughts: it is super easy to get confused by traversing the array
     *   DP, on the other hand, can hardly go wrong if your logic is correct
     *
     *   To illustrate my point, I have attached both traversal and DP solution
     * @param A
     * @return the length of a maximum size turbulent subarray of A.
     */
    private int maxTurbulenceSize(int[] A) {
        int n = A.length;
        int idx = 0;
        int f = 1;
        int max = 1;
        /** f = idx + 1
         max is the maxTur in A[0, idx-1]
         Now calculating max starting from A[idx]
         */

        while( f < n){
            if(A[f] == A[idx]){
                f++;
                idx++;
                continue;
            }else{
                while(f+1 < n && A[f+1]!=A[f] && ((A[f+1]-A[f])/Math.abs(A[f+1]-A[f])) * ((A[f]-A[f-1])/Math.abs(A[f]-A[f-1])) < 0 ){
                    f++;
                }
                if( f == n-1) return Math.max(max, f- idx+1);
                max = Math.max(max, f-idx+1);
                idx = f;
                f++;
            }
        }
        return max;
    }

    private int maxTurbulenceSize1(int[] A) {
        int inc = 1, dec = 1, result = 1;
        for (int i = 1; i < A.length; i++) {
            if (A[i] < A[i - 1]) {
                dec = inc + 1;
                inc = 1;
            } else if (A[i] > A[i - 1]) {
                inc = dec + 1;
                dec = 1;
            } else {
                inc = 1;
                dec = 1;
            }
            result = Math.max(result, Math.max(dec, inc));
        }
        return result;
    }


    /**
     * 1027. Longest Arithmetic Sequence
     * @param A an array of integers,
     * @return the length of the longest arithmetic subsequence in A.
     */
    private int longestArithmeticSequence(int[] A) {
        int res = 2, n = A.length;
        HashMap<Integer, Integer>[] dp = new HashMap[n];
        for (int j = 0; j < A.length; j++) {
            dp[j] = new HashMap<>();
            for (int i = 0; i < j; i++) {
                int d = A[j] - A[i];
                dp[j].put(d, dp[i].getOrDefault(d, 1) + 1);
                res = Math.max(res, dp[j].get(d));
            }
        }
        return res;
    }

    public static void main(String[] args) {
        System.out.println("Starting DP!");
    }
}
