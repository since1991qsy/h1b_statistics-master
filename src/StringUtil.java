import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class StringUtil {
    private final Pattern nonCharacter = Pattern.compile("^[^a-zA-Z]+|[^a-zA-Z]+$");
    public List<String> split(String s, char delimiter){
        List<String> ret = new ArrayList<>();
        int i = 0;
        StringBuilder sb = new StringBuilder();
        char[] sc = s.toCharArray();
        while (i < sc.length) {
            if (sc[i] == delimiter) {
                ret.add(sb.toString());
                sb = new StringBuilder();
            } else if (sc[i] == '"') {
                int tail = i + 1;
                while (s.charAt(tail) != '"') tail++;
                sb.append(s.substring(i, tail + 1));
                i = tail;
            } else {
                sb.append(sc[i]);
            }
            i++;
        }

        // if last element is empty, we add empty
        // if last element is not empty, we add it to result
        ret.add(sb.toString());
        return ret;
    }
    public String trim(String rawString) {
        return nonCharacter.matcher(rawString).replaceAll("");
    }
}
