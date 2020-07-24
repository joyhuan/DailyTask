import java.util.*;

public class Trees {

    public int[] countSubTrees(int n, int[][] edges, String labels) {
        Map<Integer, List<Integer>> g = new HashMap<>();
        for (int[] e : edges) {
            g.computeIfAbsent(e[0], l -> new ArrayList<>()).add(e[1]);
            g.computeIfAbsent(e[1], l -> new ArrayList<>()).add(e[0]);
        }
        int[] ans = new int[n];
        dfs(g, 0, -1, labels, ans);
        return ans;
    }
    private int[] dfs(Map<Integer, List<Integer>> g, int node, int parent, String labels, int[] ans) {
        int[] cnt = new int[26];
        char c = labels.charAt(node);
        for (int child : g.getOrDefault(node, Collections.emptyList())) {
            if (child != parent) {
                int[] sub = dfs(g, child, node, labels, ans);
                for (int i = 0; i < 26; ++i) {
                    cnt[i] += sub[i];
                }
            }
        }
        ++cnt[c - 'a'];
        ans[node] = cnt[c - 'a'];
        return cnt;
    }
//    My half-working solution
    public int[] countSubTrees2(int n, int[][] edges, String labels) {
        HashMap<Integer, ArrayList<Integer>> priorityLevel = new HashMap<>(); // level to numbers on this level
        ArrayList<Integer> forZero = new ArrayList<Integer>(0);
        forZero.add(0);
        priorityLevel.put(0, forZero);
        HashMap<Integer, Integer> nodeToLevel = new HashMap<>();
        nodeToLevel.put(0,0);
        int maxPriority = 0;
        HashMap<Integer, ArrayList<Integer>> treeStruct = new HashMap<>();
        treeStruct.put(0, new ArrayList<Integer>());
        for(int[] pair: edges){
            if(treeStruct.containsKey(pair[0])){
                ArrayList<Integer> previous = treeStruct.get(pair[0]);
                previous.add(pair[1]);
                treeStruct.put(pair[0], previous);
            }else if(treeStruct.containsKey(pair[1])){
                ArrayList<Integer> previous = treeStruct.get(pair[1]);
                previous.add(pair[0]);
                treeStruct.put(pair[1], previous);

//                ArrayList<Integer> place = new ArrayList<Integer>();
//                place.add(pair[1]);
//                treeStruct.put(pair[0], place);
            }else{

            }
            if(nodeToLevel.containsKey(pair[0])){
                nodeToLevel.put(pair[1], 1+nodeToLevel.get(pair[0]));

                ArrayList<Integer> previous = priorityLevel.getOrDefault(1+nodeToLevel.get(pair[0]), new ArrayList<Integer>());
                previous.add(pair[1]);
                priorityLevel.put(1+nodeToLevel.get(pair[0]), previous);
                maxPriority = Math.max(maxPriority, 1+nodeToLevel.get(pair[0]));
                System.out.println("pair 0 and pair 1 " + pair[0] + " " + pair[1]);
                System.out.println("maxPriority " + maxPriority);
                System.out.println(priorityLevel.get(maxPriority));
            }
        }
        System.out.println(maxPriority);
        HashMap<Integer, HashMap<String, Integer>> map = new HashMap<>();
        int[] finalAnswer = new int[labels.length()];
        while(maxPriority >=0){
            System.out.println("maxPriority current is "+maxPriority);
            ArrayList<Integer> leaves = priorityLevel.get(maxPriority);
            System.out.println(leaves);
            for(int i: leaves){
                HashMap<String, Integer> toAdd = new HashMap<>();
                if(maxPriority == 0) System.out.println("i is "+i);
                toAdd.put(labels.substring(i, i+1), 1);
                if(treeStruct.get(i)!= null){
                    for(int j: treeStruct.get(i)){
                        if(maxPriority == 0) System.out.println("j is "+j);
                        HashMap<String, Integer> mapOfJ = map.get(j);
                        for(String k: mapOfJ.keySet()){
                            toAdd.put(k, toAdd.getOrDefault(k, 0) +mapOfJ.get(k));
                        }
                    }
                }
                map.put(i, toAdd);
                finalAnswer[i] = map.get(i).get(labels.substring(i,i+1));
            }
            maxPriority -= 1;
        }

        return finalAnswer;
    }

    public static void main(String[] args){
        Trees trees = new Trees();
        int[] result = trees.countSubTrees(8, new int[][]{{0,1},{1,2},{2,3},{1,4},{2,5},{2,6},{4,7}},"leetcode");
        System.out.println(Arrays.toString(result));
    }
}
