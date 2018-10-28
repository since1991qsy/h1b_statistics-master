import org.w3c.dom.ls.LSException;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Stream;

/**
 * Created by zxj on 10/27/18.
 */

class Counter {
    private int value;

    public Counter(int value) {
        this.value = value;
    }

    public Counter() {
        this.value = 0;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void increment() {
        this.value += 1;
    }
}
public class FileStream {
    private RecordField recordField;

    public FileStream(RecordField recordField) {
        this.recordField = recordField;
    }

    public Stream<Record> getCertifiedRecords(
        RecordField field,
        Stream<String> lineStream) {
        StringUtil util = new StringUtil();
        Stream<Optional<Record>> recordStream = lineStream.map(line -> {
            List<String> recordList = util.split(line.toUpperCase(), ';');
            String certifiedValue = recordList.get(field.getCertifiedIndex());
            if (!certifiedValue.equals("CERTIFIED")) {
                return Optional.empty();
            }
            String trimmedOccupation = util.trim(recordList.get(field.getOccupationIndex()));
            String trimmedState = util.trim(recordList.get(field.getStateIndex()));
            return Optional.of(new Record(trimmedOccupation, trimmedState));
        });
        return recordStream.filter(Optional::isPresent)
            .map(Optional::get);
    }

    public Optional<Triple<Map<String, Integer>, Map<String, Integer>, Integer>> filePathToStream(String filePath) {
        HashMap<String, Integer> occupationMap = new HashMap<>();
        HashMap<String, Integer> stateMap = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            StringUtil util = new StringUtil();
            String firstLine = br.readLine();
            if (firstLine == null) {
                return Optional.empty();
            } else {
                Stream<String> restLines = br.lines();
                recordField.setIndexFromList(util.split(firstLine, ';'));
                Stream<Record> recordStream = getCertifiedRecords(recordField, restLines);
                Counter counter = new Counter();
                recordStream.forEach(record -> {
                    if (!record.emptyState()){
                        int stateTotal = stateMap.getOrDefault(record.state, 0);
                        stateMap.put(record.state, stateTotal + 1);
                    }
                    if (!record.emptyOccupation()){
                        int occupationTotal = occupationMap.getOrDefault(record.occupation, 0);
                        occupationMap.put(record.occupation, occupationTotal + 1);
                    }
                    counter.increment();
                });
                return Optional.of(new Triple<>(occupationMap, stateMap, counter.getValue()));
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
