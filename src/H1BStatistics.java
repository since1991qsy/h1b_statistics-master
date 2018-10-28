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

    public static void main(String[] args) {
	// write your code here
        String path = "/Users/zxj/Downloads/h1b_input.csv";
        StringUtil util = new StringUtil();
        H1BStatistics stat = new H1BStatistics();
        RecordField recordField = new RecordField("SOC_NAME", "CASE_STATUS",
            "WORKSITE_STATE");
        FileStream stream = new FileStream(recordField);

        Optional<Triple<Map<String, Integer>, Map<String, Integer>, Integer>> optionalResult = stream.filePathToStream(path);
        if (optionalResult.isPresent()){
            Triple<Map<String, Integer>, Map<String, Integer>, Integer> mapTriple = optionalResult.get();
            
        }
    }
}


