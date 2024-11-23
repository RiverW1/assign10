package assign10;

import java.awt.Graphics;
import java.util.ArrayList;

/**
 * Represents the editor for a song consisting of multiple tracks.
 * It extends GridCanvas to interpret the grid's vertical axis as tracks 
 * and horizontal axis as time.
 * 
 * @author River Whitten
 * @version 2024-11-22
 */
public class SongEditor extends GridCanvas {

    private SimpleSequencer sequencer;
    private BetterDynamicArray<TrackPanel> trackPanels;
    private int currentTrack;

    /**
     * Constructs a SongEditor instance with a specified width and height.
     * Initializes the SimpleSequencer and sets the number of rows and columns based
     * on the trackPanels and the sequencer's length.
     *
     * @param width the width of the editor
     * @param height the height of the editor
     */
    public SongEditor(int width, int height) {
        super(width, height, 1, 1, 1, 1);  // Default, will be updated later
        this.trackPanels = new BetterDynamicArray<>();
        this.sequencer = new SimpleSequencer(16);  // Default length of 16
        this.currentTrack = 0;
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    // Setter and Getter methods

    public void setLength(int length) {
        sequencer.setLength(length);
        setColumns(length);  // Set grid columns based on sequencer length
    }

    public int getLength() {
        return sequencer.getLength();
    }

    public SimpleSequencer getSequencer() {
        return sequencer;
    }

    public void clear() {
        sequencer.clear();
    }

    public void setEvents(BetterDynamicArray<AudioEvent> newEvents) {
        sequencer.updateSequence(newEvents);
    }

    public void setTrackList(BetterDynamicArray<TrackPanel> trackList) {
        this.trackPanels = trackList;
        setRows(trackList.size());  // Set the number of rows based on the number of tracks
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw time indicator (could be more sophisticated, e.g., moving line)
        int time = (int) sequencer.getElapsedTime();
        int x = (int) (getWidth() * (time / (double) sequencer.getLength()));
        g.drawLine(x, 0, x, getHeight());  // Vertical line indicating time
    }

    // GridCanvas's abstract methods

    @Override
    public void onCellPressed(int row, int col, int rowSpan, int colSpan) {
        // Set current track to the selected row and adjust restrictions
        currentTrack = row;
        setRestrictions(1, trackPanels.get(currentTrack).getSequencer().getLength());
    }

    @Override
    public void onCellDragged(int row, int col, int rowSpan, int colSpan) {
        // Update restrictions if the track number changes
        if (row != currentTrack) {
            currentTrack = row;
            setRestrictions(1, trackPanels.get(currentTrack).getSequencer().getLength());
        }
    }

    @Override
    public void onCellReleased(int row, int col, int rowSpan, int colSpan) {
        // Create a new TrackEvent when a cell is released
        TrackEvent newEvent = new TrackEvent(col, "Track Event", currentTrack, colSpan, trackPanels.get(currentTrack).getSequencer());
        sequencer.add(newEvent);  // Add TrackEvent to sequencer
    }

    @Override
    public void onCellRemoved(int row, int col) {
        // Remove TrackEvent associated with the cell
        for (AudioEvent event : sequencer) {
            if (event instanceof TrackEvent && event.getChannel() == row && event.getTime() == col) {
                sequencer.remove(event);
            }
        }
    }
}
