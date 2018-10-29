import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.Stream;

/**
 * Created by zxj on 10/27/18.
 */

class IntegerCounter {
    private int value;

    public IntegerCounter(int value) {
        this.value = value;
    }

    public IntegerCounter() {
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

    public RecordField getRecordField() {
        return recordField;
    }

    public void setRecordField(RecordField recordField) {
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
                List<String> fieldList = util.split(firstLine, ';');
                recordField.setIndexFromList(fieldList);
                if (recordField.existsEmptyIndex()) {
                    recordField = new RecordField("LCA_CASE_SOC_NAME", "STATUS", "LCA_CASE_WORKLOC1_STATE");
                    recordField.setIndexFromList(fieldList);
                    if (recordField.existsEmptyIndex()) return Optional.empty();
                }

                Stream<Record> recordStream = getCertifiedRecords(recordField, restLines);
                IntegerCounter integerCounter = new IntegerCounter();
                recordStream.forEach(record -> {
                    if (!record.emptyState()){
                        stateMap.computeIfPresent(record.state, ((key, value) -> value + 1));
                        stateMap.putIfAbsent(record.state, 1);
                    }
                    if (!record.emptyOccupation()){
                        occupationMap.computeIfPresent(record.occupation, ((key, value)-> value + 1));
                        occupationMap.putIfAbsent(record.occupation, 1);
                    }
                    integerCounter.increment();
                });
                return Optional.of(new Triple<>(occupationMap, stateMap, integerCounter.getValue()));
            }
        } catch (FileNotFoundException e) {
            System.out.println(String.format("File does not exist in path: %s;", filePath));
            e.printStackTrace();
            System.exit(1);
            return Optional.empty();
        } catch (IOException e) {
            System.out.println(String.format("Failed to open file: %s;", filePath));
            e.printStackTrace();
            System.exit(1);
            return Optional.empty();
        }
    }
}
