package assign10;

/**
 * represents a general audio event that can occur at a specific time.
 * It serves as a base class for different types of audio events.
 * This class implements the comparable interface to allow sorting of audio events by time.
 * 
 * @author - River Whitten
 * @version - 10.24.24
 */
public abstract class AudioEvent implements Comparable<AudioEvent> {
    
    private int time;
    private String name;
    private int channel;

    /**
     * Constructs an audio event with the specified time, name, and channel.
     *
     * @param time    the time at which the event occurs
     * @param name    the name describing the event
     * @param channel the channel number for the event
     */
    public AudioEvent(int time, String name, int channel) {
        this.time = time;
        this.name = name;
        this.channel = channel;
    }

    /**
     * Returns the time at which this audio event occurs.
     *
     * @return the time of the event
     */
    public int getTime() {
        return time;
    }

    /**
     * Returns the name of this audio event.
     *
     * @return the name of the event
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the channel of this audio event.
     *
     * @return the channel number
     */
    public int getChannel() {
        return channel;
    }

    /**
     * Executes the audio event.
     */
    public abstract void execute();

    /**
     * Completes the audio event.
     */
    public abstract void complete();

    /**
     * Cancels the audio event.
     */
    public abstract void cancel();
}
