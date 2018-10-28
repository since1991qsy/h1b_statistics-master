/**
 * Created by zxj on 10/27/18.
 */
public class Record {
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

}
