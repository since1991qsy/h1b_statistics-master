import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Stream;

/**
 * Created by zxj on 10/27/18.
 */
public class FileStream {
    public Optional<Tuple<String, Stream<String>>> filePathToStream(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String firstLine = br.readLine();
            if (firstLine == null) {
                return Optional.empty();
            } else {
                Stream<String> restLines = br.lines();
                return Optional.of(new Tuple<>(firstLine, restLines));
            }
        } catch (FileNotFoundException e) {
            System.out.println(String.format("File does not exist in path: %s;", filePath));
            e.printStackTrace();
            return Optional.empty();
        } catch (IOException e) {
            System.out.println(String.format("Failed to open file: %s;", filePath));
            e.printStackTrace();
            return Optional.empty();
        }
    }
}
