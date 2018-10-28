import java.util.Optional;

/**
 * Created by zxj on 10/27/18.
 */
public class RecordField {
    public final String occupation;
    public final String certified;
    public final String state;
    private int occupationIndex;
    private int certifiedIndex;
    private int stateIndex;

    public RecordField(String occupation, String certified, String state) {
        this.occupation = occupation;
        this.certified = certified;
        this.state = state;
        this.occupationIndex = -1;
        this.occupationIndex = -1;
        this.stateIndex = -1;
    }

    public void setOccupationIndex(int occupationIndex) {
        this.occupationIndex = occupationIndex;
    }

    public void setCertifiedIndex(int certifiedIndex) {
        this.certifiedIndex = certifiedIndex;
    }

    public void setStateIndex(int stateIndex) {
        this.stateIndex = stateIndex;
    }

    public int getOccupationIndex() {
        return occupationIndex;
    }

    public int getCertifiedIndex() {
        return certifiedIndex;
    }

    public int getStateIndex() {
        return stateIndex;
    }
}
