import java.nio.channels.ScatteringByteChannel;
import java.util.*;

public class BFS {

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
