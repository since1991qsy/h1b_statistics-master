/**
 * Created by zxj on 10/27/18.
 */
class Record {
    public final String occupation;
    public final String state;

    public Record(String occupation, String state) {
        this.occupation = occupation;
        this.state = state;
    }

    public boolean emptyOccupation(){
        return occupation.isEmpty();
    }

    public boolean emptyState(){
        return state.isEmpty();
    }

    public String toString(){
        return occupation + "\t" + state;
    }
}

class AttributesTotal implements Comparable<AttributesTotal> {
    public final String attribute;
    public final int number;

    public AttributesTotal(String attribute, int number) {
        this.attribute = attribute;
        this.number = number;
    }

    @Override
    public int compareTo(AttributesTotal other) {
        return (number == other.number) ? attribute.compareTo(other.attribute): number - other.number;
    }

    public String mkString(int total) {
        return String.format("%s;%d;%.1f%%\n", attribute, number,  100.0 * number / total);
    }
}