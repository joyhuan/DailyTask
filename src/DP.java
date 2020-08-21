import java.util.HashMap;

public class DP {
    /**
     * 10. Regular Expression Matching
     * '.' Matches any single character.
     * '*' Matches zero or more of the preceding element.
     * 悔不当初 道行未满 仍需修行
     * @param s
     * @param p
     * @return Whether regular expression p matches with string s
     */
    public boolean isMatch(String s, String p) {

        if (s == null || p == null) {
            return false;
        }
        boolean[][] dp = new boolean[s.length()+1][p.length()+1];
        dp[0][0] = true;
        for (int i = 0; i < p.length(); i++) {
            if (p.charAt(i) == '*' && dp[0][i-1]) {
                dp[0][i+1] = true;
            }
        }
        for (int i = 0 ; i < s.length(); i++) {
            for (int j = 0; j < p.length(); j++) {
                if (p.charAt(j) == '.') {
                    dp[i+1][j+1] = dp[i][j];
                }
                if (p.charAt(j) == s.charAt(i)) {
                    dp[i+1][j+1] = dp[i][j];
                }
                if (p.charAt(j) == '*') {
                    if (p.charAt(j-1) != s.charAt(i) && p.charAt(j-1) != '.') {
                        dp[i+1][j+1] = dp[i+1][j-1];
                    } else {
                        dp[i+1][j+1] = (dp[i+1][j] || dp[i][j+1] || dp[i+1][j-1]);
                    }
                }
            }
        }
        return dp[s.length()][p.length()];
    }
//    public boolean isMatch(String s, String p) {
//        boolean[][] DP = new boolean[s.length()][p.length()];
//        for(int i =0; i < s.length(); i++){
//            char sChar = s.charAt(i);
//            for(int j = 0; j < p.length(); j++){
//                char pChar = p.charAt(j);
//                if(pChar == '.') DP[i][j] = (i==0 && (j==0 || DP[i][j-1])) || (j != 0 && DP[i-1][j-1]);
//                else if(pChar == '*') DP[i][j] = DP[i][j-1] || (j > 1 && DP[i][j-2]) || (i > 0 && DP[i-1][j-1] && s.charAt(i) == p.charAt(j-1));
//                else if(pChar == sChar){
//                    if((i == 0 && j == 0) || (i > 0 && j > 0 && DP[i-1][j-1]))
//                        DP[i][j] = true;
//                    if(i == 0){
//                        for(int k = j-1; j >= 0; j-=2){
//                            if(p.charAt(k) != '*'){
//                                break;
//                            }
//                        }
//                        DP[i][j] = true;
//                    }
//                }else{ // pChar != '.' && pChar != '*' && pChar != sChar
//                    DP[i][j] = false;
//                }
//            }
//        }
//        return DP[s.length()-1][p.length()-1];
//    }
////        if(i == s.length() && j < p.length()){
////            while(j < p.length()) {
////                if (p.charAt(j) != '*') return false;
////                j++;
////            }
////            return true;
////        }
////        if(i < s.length() && j == p.length()){
////            char pLastChar = p.charAt(j-1);
////            if(pLastChar != '*') return false;
////            while(i < s.length()){
////                if(s.charAt(i) != pLastChar) return false;
////                i++;
////            }
////            return true;
////        }


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
