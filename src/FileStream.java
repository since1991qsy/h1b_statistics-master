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

    public Stream<Record> getCertifiedRecords(
        RecordField field,
        Stream<String> lineStream) {
        StringUtil util = new StringUtil();
        Stream<Optional<Record>> recordStream = lineStream.map(line -> {
            List<String> recordList = util.split(line, ';');
            if (!recordList.get(field.getCertifiedIndex()).equals("CERTIFIED")) {
                return Optional.empty();
            }
            String trimmedOccupation = util.trim(recordList.get(field.getOccupationIndex()));
            String trimmedState = util.trim(recordList.get(field.getStateIndex()));
            return Optional.of(new Record(trimmedOccupation, trimmedState));
        });
        return recordStream.filter(Optional::isPresent)
            .map(Optional::get);
    }

    private RecordField getFieldIndex(List<String> fileds, RecordField recordField) {
        ListIterator<String> fieldIterator = fileds.listIterator();
        while (fieldIterator.hasNext()) {
            String nextField = fieldIterator.next();
            Integer index = fieldIterator.nextIndex();
            if (nextField.equals(recordField.state)){
                recordField.setStateIndex(index);
            }
            if (nextField.equals(recordField.certified)){
                recordField.setCertifiedIndex(index);
            }
            if (nextField.equals(recordField.occupation)){
                recordField.setOccupationIndex(index);
            }
        }
        return recordField;
    }

}
