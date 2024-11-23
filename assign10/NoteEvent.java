package assign10;

/**
 * This class represents an event where a note is played by an instrument.
 * It extends the audio event class and adds specific attributes like duration and pitch.
 * 
 */
public class NoteEvent extends AudioEvent {

    private int duration;
    private int pitch;
    private SimpleSynthesizer synthesizer;

    /**
     * Constructs a note event with the specified parameters.
     *
     * @param time      the time at which the note is played
     * @param instrument the name of the instrument playing the note
     * @param channel   the channel number
     * @param duration  the duration of the note in milliseconds
     * @param pitch     the pitch of the note
     */
    public NoteEvent(int time, String instrument, int channel, int duration, int pitch, SimpleSynthesizer synthesizer) {
        super(time, instrument, channel);
        this.duration = duration;
        this.pitch = pitch;
        this.synthesizer = synthesizer;
    }

    /**
     * Returns the duration of the note event.
     *
     * @return the duration of the note in milliseconds
     */
    public int getDuration() {
        return duration;
    }

    /**
     * Returns the pitch of the note event.
     *
     * @return the pitch of the note
     */
    public int getPitch() {
        return pitch;
    }

    /**
     * Executes the note event by printing its details.
     * 
     */
    @Override
    public void execute() {
        synthesizer.noteOn(getChannel(), pitch);
    }

    @Override
    public void complete() {
    	synthesizer.noteOff(getChannel(), pitch);
       
    }

    @Override
    public void cancel() {
    	synthesizer.noteOff(getChannel(), pitch);
        
    }

    /**
     * Returns a string representation of the note event
     * 
     * @return a string describing the note event
     */
    @Override
    public String toString() {
        return getName() + "[" + getChannel() + ", " + getTime() + ", " + duration + ", " + pitch + "]";
    }

    /**
     * Compares this note event with another audio event for ordering.
     *
     * @param other the other audio event to compare to
     * @return a negative integer, zero, or a positive integer as this event is less than, equal to, or greater than the other event
     */
    @Override
    public int compareTo(AudioEvent other) {
        if (getTime() != other.getTime()) {
            return Integer.compare(getTime(), other.getTime());
        }
        if (other instanceof ChangeEvent) {
            return 1;
        }
        if (other instanceof TrackEvent) {
            return -1;
        }
        return 0;
    }
}
