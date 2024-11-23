package assign10;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The SongPanel class is a JPanel that represents a user interface for controlling a song's playback and settings. 
 * It includes buttons for playing and looping the song, a spinner to adjust the song length, 
 * and a display for managing the sequence of audio events.
 * This panel is responsible for interacting with the SongEditor to update and control the song's playback and properties.
 */
public class SongPanel extends SketchingPanel implements ActionListener, ChangeListener {

    private final SongEditor songEditor;
    private final JToggleButton playButton;
    private final JToggleButton loopButton;
    private final JSpinner lengthSpinner;
    private final JLabel lengthLabel;

    /**
     * Constructs a SongPanel with the specified width and height.
     * Initializes the layout and components, including the SongEditor, control buttons, and length spinner.
     * 
     * @param width  the width of the panel
     * @param height the height of the panel
     */
    public SongPanel(int width, int height) {
        // size
        setPreferredSize(new Dimension(width, height));

        setLayout(new BorderLayout());

        songEditor = new SongEditor(800, 800);
        add(songEditor, BorderLayout.CENTER);

        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
        add(controlPanel, BorderLayout.SOUTH);

        playButton = new JToggleButton("Play");
        playButton.addActionListener(this);
        controlPanel.add(playButton);

        loopButton = new JToggleButton("Loop");
        loopButton.addActionListener(this);
        controlPanel.add(loopButton);

        lengthLabel = new JLabel("Song Length: ");
        controlPanel.add(lengthLabel);

        lengthSpinner = new JSpinner(new SpinnerListModel(new Integer[] {4, 8, 16, 32, 64, 128, 256, 512}));
        lengthSpinner.addChangeListener(this);
        controlPanel.add(lengthSpinner);
    }

    /**
     * Returns the sequencer associated with this song panel.
     * 
     * @return the SimpleSequencer used by the song editor
     */
    @Override
    public SimpleSequencer getSequencer() {
        return songEditor.getSequencer();
    }

    /**
     * Sets the length of the song and updates the corresponding spinner value.
     * 
     * @param length the new length of the song
     */
    @Override
    public void setLength(int length) {
        songEditor.setLength(length);
        lengthSpinner.setValue(length);
    }

    /**
     * Sets the audio events for the song.
     * 
     * @param events the audio events to set
     */
    @Override
    public void setEvents(BetterDynamicArray<AudioEvent> events) {
        songEditor.setEvents(events);
    }

    /**
     * Clears the song editor, resetting all settings.
     */
    @Override
    public void clear() {
        songEditor.clear();
    }

    /**
     * Sets the track list in the song editor.
     * 
     * @param trackList the list of TrackPanels to set
     */
    public void setTrackList(BetterDynamicArray<TrackPanel> trackList) {
        songEditor.setTrackList(trackList);
    }

    /**
     * Handles actions performed by the play and loop buttons. 
     * Starts or stops playback when the play button is toggled and sets the loop state when the loop button is toggled.
     * 
     * @param event the action event triggered by the button
     */
    @Override
    public void actionPerformed(ActionEvent event) {
        Object source = event.getSource();

        if (source == playButton) {
            if (playButton.isSelected()) {
                play();
                playButton.setText("Stop");

            } else {
                stop();
                playButton.setText("Play");
            }

        } else if (source == loopButton) {
            setLoop(loopButton.isSelected());
        }
    }

    /**
     * Updates the song length when the spinner value changes.
     * 
     * @param event the change event triggered by the length spinner
     */
    @Override
    public void stateChanged(ChangeEvent event) {
        if (event.getSource() == lengthSpinner) {
            int length = (int) lengthSpinner.getValue();
            setLength(length);
        }
    }
}
