package assign10;

/**
 * The class represents an event that modifies a property, such as volume or pitch.
 * It extends the audio event class and includes a new value to which the property will change.
 */
public class ChangeEvent extends AudioEvent {

    private int value;
    private SimpleSynthesizer synthesizer;
    

    /**
     * Constructs a change event with the specified parameters.
     *
     * @param time    the time at which the change occurs
     * @param type    the name of the property being changed (e.g., "volume")
     * @param channel the channel number
     * @param value   the new value for the property being changed
     */
    public ChangeEvent(int time, String type, int channel, int value, SimpleSynthesizer synthesizer) {
        super(time, type, channel);
        this.value = value;
        this.synthesizer = synthesizer;
    }

    /**
     * Returns the new value of the property being changed.
     *
     * @return the new value of the property
     */
    public int getValue() {
        return value;
    }

    @Override
    public void execute() {
        
    }

    @Override
    public void complete() {
        
    }

    @Override
    public void cancel() {
        
    }

    /**
     * Returns a string representation of the change event
     * 
     * @return a string describing the change event
     */
    @Override
    public String toString() {
        return getName() + "[" + getChannel() + ", " + getTime() + ", " + value + "]";
    }

    @Override
    public int compareTo(AudioEvent other) {
        if (getTime() != other.getTime()) {
            return Integer.compare(getTime(), other.getTime());
        }
        return (other instanceof ChangeEvent) ? 0 : -1;
    }
}
