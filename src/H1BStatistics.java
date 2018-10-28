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
        FileStream stream = new FileStream();
        StringUtil util = new StringUtil();
        Optional<Tuple<String, Stream<String>>> optionalResult = stream.filePathToStream(path);
        if (optionalResult.isPresent()){
            Tuple<String, Stream<String>> resultTuple = optionalResult.get();
            String filesLine = resultTuple.first;
            Stream<String> resultLines = resultTuple.second;
            List<String> filesList = util.split(filesLine, ';');

        }
    }
}


