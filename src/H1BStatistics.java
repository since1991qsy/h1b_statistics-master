import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

public class H1BStatistics {
    private void oldStatistics(String[] args) {
        System.out.println("h1b_statistics starts counting");
        String fileName = ".//input//h1b_input.csv";
        if (args.length == 1) {
            fileName = args[0];
        }

        TopTenCalculator h1B_counting = new TopTenCalculator();
        h1B_counting.count(fileName);
        System.out.println("finish counting, you can check the answer at output folder");
    }

    private List<AttributesTotal> getTopK(Map<String, Integer> map, int K) {
        // reverse order top K PriorityQueue
        PriorityQueue<AttributesTotal> topKQueue = new PriorityQueue<>();
        map.forEach((key, value) -> {
            topKQueue.offer(new AttributesTotal(key, value));
            if (topKQueue.size() > K) topKQueue.poll();

        });
        int capacity = Math.min(K, topKQueue.size());
        AttributesTotal[] topK = new AttributesTotal[capacity];
        for (int i = capacity - 1; i >= 0; i--) {
            topK[i] = topKQueue.poll();
        }
        return Arrays.asList(topK);
    }

    private void outputList(String filePath, String header, List<AttributesTotal> attributes,
                            int totalNumbers){
        File file = new File(filePath);
        try {
            file.createNewFile();
        } catch (IOException e) {
            System.out.println("Failed to create new File");
            e.printStackTrace();
            System.exit(1);
        }

        try(FileOutputStream fileStream = new FileOutputStream(file, false);
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fileStream, "UTF-8"))) {
            writer.write(header);
            attributes.forEach(ele -> {
                try {
                    writer.write(ele.mkString(totalNumbers));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

    }

    public static void main(String[] args) {
	// write your code here
        String path = "/Users/zxj/Downloads/h1b_input.csv";
        String currentPath = Paths.get(System.getProperty("user.dir")).toString();
        Path occupationPath = Paths.get(currentPath, "output", "top_10_occupations.txt");
        String occupationHeader = "TOP_OCCUPATIONS;NUMBER_CERTIFIED_APPLICATIONS;PERCENTAGE\n";
        String stateHeader = "TOP_STATES;NUMBER_CERTIFIED_APPLICATIONS;PERCENTAGE\n";
        Path statesPath = Paths.get(currentPath, "output", "top_10_states.txt");

        StringUtil util = new StringUtil();
        H1BStatistics stat = new H1BStatistics();
        RecordField recordField = new RecordField("SOC_NAME", "CASE_STATUS",
            "WORKSITE_STATE");
        FileStream stream = new FileStream(recordField);

        Optional<Triple<Map<String, Integer>, Map<String, Integer>, Integer>> optionalResult = stream.filePathToStream(path);
        if (optionalResult.isPresent()){
            Triple<Map<String, Integer>, Map<String, Integer>, Integer> mapTriple = optionalResult.get();
            int total = mapTriple.third;
            List<AttributesTotal> topOccupations = stat.getTopK(mapTriple.first, 10);
            List<AttributesTotal> topStates = stat.getTopK(mapTriple.second, 10);
            stat.outputList(occupationPath.toString(), occupationHeader, topOccupations, total);
            stat.outputList(statesPath.toString(), stateHeader, topStates, total);
        }
    }
}


