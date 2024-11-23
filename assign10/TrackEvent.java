package assign10;

import java.util.Arrays;

public class TrackEvent extends AudioEvent {
    private int duration;
    private SimpleSequencer sequencer;
    
    public TrackEvent(int time, String trackName, int channel, int duration, SimpleSequencer sequencer) {
        super(time, trackName, channel);
        this.duration = duration;
        this.sequencer = sequencer;
    }

    public int getDuration() {
        return duration;
    }

    public SimpleSequencer getSequence() {
        return sequencer;
    }

    @Override
    public void execute() {
        sequencer.start();
    }

    @Override
    public void complete() {
        // No implementation for now
    }

    @Override
    public void cancel() {
        sequencer.stop();
        
    }

    @Override
    public String toString() {
        String result = getName() + "[" + getChannel() + ", " + getTime() + ", " + getDuration() + "]";
        for (AudioEvent event : sequencer) {
        	result += event;
        }
        return result;
    }

	@Override
	public int compareTo(AudioEvent o) {
		// TODO Auto-generated method stub
		return 0;
	}
}
