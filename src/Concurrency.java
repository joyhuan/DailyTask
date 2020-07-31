import java.util.List;

public class Concurrency {
    /**
     * // This is the HtmlParser's API interface.
     * // You should not implement it, or speculate about its implementation
     * */
//     interface HtmlParser {
//       public List<String> getUrls(String url) {}
//     }

//    public List<String> crawl(String startUrl, HtmlParser htmlParser) {
//        String hostname = getHostname(startUrl);
//
//        // We don't want to re-crawl pages, so we're going to use a Set that can be modified concurrently
//        // The following link describes how it is implemented, if you're interested
//        // https://github.com/frohoff/jdk8u-jdk/blob/master/src/share/classes/java/util/concurrent/ConcurrentHashMap.java#L271
//        Set<String> visited = ConcurrentHashMap.newKeySet();
//        visited.add(startUrl);
//
//        // This is similar to map-reduce, but instead of reducing to a single value we collect the values
//        return crawl(startUrl, htmlParser, hostname, visited)
//                .collect(Collectors.toList());
//    }
//
//    private Stream<String> crawl(String startUrl, HtmlParser htmlParser, String hostname, Set<String> visited) {
//        // htmlParser#getUrls returns a List<String>
//        Stream<String> stream = htmlParser.getUrls(startUrl)
//                // We process each url in parallel. The number of threads (parallelism) is decided by the JDK.
//                .parallelStream()
//                // We filter out external urls, meaning we don't continue processing them and they're not a part of the result
//                // This method (isSameHostname) is thread-safe
//                .filter(url -> isSameHostname(url, hostname))
//                // visited is the concurrent set. The add method returns false if the url is already in the set. In that case we ignore the url
//                // A Set is normally not thread-safe but the one we're using is.
//                .filter(url -> visited.add(url))
//                // If the url passed both filters above, we call crawl on it (recursivelly). A url generates a Stream<String>
//                // If we were to use .map we would get something like a List<Stream<String>>
//                // but we need to return a single Stream so we can chain the calls. flatMap concats themultiple streams into a single one
//                .flatMap(url -> crawl(url, htmlParser, hostname, visited));
//
//        // We want to return the original crawled url as well as the url's we found by crawling it
//        // Since startUrl is not part of the stream, we need to add it
//        //
//        // Think about what happens to the original url in the flatMap phase: it gets replaced by the Stream<String> of the crawl method
//        // To keep the startUrl we concat it to the stream.
//        return Stream.concat(Stream.of(startUrl), stream);
//    }
//
//    /** Returns the url without the path. (method name wasn't the best) */
//    private String getHostname(String url) {
//        // Start the search after the protocol (http:// is always in the url)
//        int idx = url.indexOf('/', 7);
//        // Return the url without the path
//        return (idx != -1) ? url.substring(0, idx) : url;
//    }
//
//    private boolean isSameHostname(String url, String hostname) {
//        if (!url.startsWith(hostname)) {
//            return false;
//        }
//        return url.length() == hostname.length() || url.charAt(hostname.length()) == '/';
//    }

    public static void main(String[] args) {


    }


    class TrafficLight {
        boolean isGreen;
        public TrafficLight() {
            this.isGreen = true;
        }

        public void carArrived(
                int carId,           // ID of the car
                int roadId,          // ID of the road the car travels on. Can be 1 (road A) or 2 (road B)
                int direction,       // Direction of the car
                Runnable turnGreen,  // Use turnGreen.run() to turn light to green on current road
                Runnable crossCar    // Use crossCar.run() to make car cross the intersection
        ) {
            synchronized(this){
                if(roadId==1){
                    if(!this.isGreen){
                        this.isGreen = true;
                        turnGreen.run();
                    }
                }else{
                    if(this.isGreen){
                        this.isGreen = false;
                        turnGreen.run();
                    }
                }
                crossCar.run();
            }
        }
    }

}
