package assign10;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Vector;

public class TrackEditor extends GridCanvas {
	
	private SimpleSynthesizer synthesizer;
	private SimpleSequencer sequencer;
	private int trackNumber;
	private int width;
	private int height;
	private int currentPitch;
	
	
	public TrackEditor(int width, int height, int trackNumber, SimpleSynthesizer synthesizer) {
		super(120, 1, 120, 120, 4, 4);
		
		this.width = width;
		this.height = height;
		this.trackNumber = trackNumber;
		this.synthesizer = synthesizer;
		this.sequencer = new SimpleSequencer(32);
		
		addMouseListener(this);
		addMouseMotionListener(this);
		
	}
	
	public void setLength(int length) {
		sequencer.setLength(length);
		setColumns(length);
	}
	
	public int getLength() {
		return sequencer.getLength();
	}
	
	public void setVolume(int volume) {
		synthesizer.setVolume(volume, volume);
	}
	
	public int getVolume() {
		return synthesizer.getVolume(currentPitch);
	}
	
	public void setMute(boolean mute) {
		synthesizer.setMute(currentPitch, mute);
	}

	public void setInstrument(int instrument) {
		synthesizer.setInstrument(instrument, instrument);
	}
	
	public Vector<String> getInstrumentNames(){
		return new Vector<>(synthesizer.getInstrumentNames());
	}
	
	public void clear() {
		super.clear();
		sequencer.stop();
		sequencer.clear();
	}
	
	public SimpleSequencer getSequencer() {
		return sequencer;
	}
	
	public void setEvents(BetterDynamicArray<AudioEvent> newEvents) {
		clear();
		for (int i = 0; i < newEvents.size(); i ++) {
			AudioEvent event = newEvents.get(i); //access each event
			if(event instanceof NoteEvent) {
				NoteEvent note = (NoteEvent) event;
				addCell(note.getPitch(), note.getTime(), 1, note.getDuration());
			}
		}
		sequencer.updateSequence(newEvents);
		
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		int timeIndicator = (int) (sequencer.getElapsedTime() * width / sequencer.getLength());
		g.setColor(Color.RED);
		g.fillRect(timeIndicator,  0,  5, getHeight());
		
		repaint();
		
	}

	@Override
	public void onCellPressed(int row, int col, int rowSpan, int colSpan) {
		currentPitch = row;
		synthesizer.noteOn(row, trackNumber);
		
	}

	@Override
	public void onCellDragged(int row, int col, int rowSpan, int colSpan) {
		if(row != currentPitch) {
			synthesizer.noteOff(currentPitch,  trackNumber);
			currentPitch = row;
			synthesizer.noteOn(row, trackNumber);
		}
		
	}

	@Override
	public void onCellReleased(int row, int col, int rowSpan, int colSpan) {
		if(colSpan > 0 ) {
			NoteEvent note = new NoteEvent(col, "Note", trackNumber, colSpan, row, synthesizer);
			sequencer.add(note);
			synthesizer.noteOff(row, trackNumber);
		}
		
	}

	@Override
	public void onCellRemoved(int row, int col) {
		for(AudioEvent event : sequencer) {
			if(event instanceof NoteEvent) {
				NoteEvent note = (NoteEvent) event;
				if(note.getPitch() == row && note.getTime() == col) {
					sequencer.remove(note);
					break;
				}
			}
		}
		
	}
	
}
