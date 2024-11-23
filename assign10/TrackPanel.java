package assign10;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * This class represents a panel for managing an individual track in the SoundSketcher program. 
 * It allows the user to control track specific properties such as mute status, track length, volume, 
 * and instrument selection. It also integrates with the TrackEditor to display and control the sequence of audio events.
 * The panel includes controls for the user to modify these properties and a central area for visual track editing.
 */
public class TrackPanel extends SketchingPanel implements ActionListener, ChangeListener {
    private TrackEditor trackEditor;
    private JToggleButton muteButton;
    private JSpinner lengthSpinner;
    private JSlider volumeSlider;
    private JComboBox<String> instrumentComboBox;
    private int trackNumber;
    private int instrumentNumber;

    /**
     * Constructs a TrackPanel with the specified dimensions and track number.
     * Initializes the TrackEditor, layout, and control components such as mute button, 
     * track length spinner, volume slider, and instrument combo box.
     * 
     * @param width The width of the track panel.
     * @param height The height of the track panel.
     * @param trackNumber The track number used for identification.
     */
    public TrackPanel(int width, int height, int trackNumber, SimpleSynthesizer synthesizer) {
        this.trackNumber = trackNumber;
        setPreferredSize(new Dimension(width, height));

        // Initialize the TrackEditor
        trackEditor = new TrackEditor(width, height, trackNumber, synthesizer);

        // Set up layout
        setLayout(new BorderLayout());

        // Add the TrackEditor to the main area
        add(trackEditor, BorderLayout.CENTER);

        // Create the control panel
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));

        // Mute/Unmute button
        muteButton = new JToggleButton("Mute");
        muteButton.addActionListener(this);
        controlPanel.add(muteButton);

        // Length spinner
        lengthSpinner = new JSpinner(new SpinnerListModel(new Integer[]{4, 8, 16, 32, 64, 128, 256, 512}));
        lengthSpinner.addChangeListener(this);
        controlPanel.add(new JLabel("Track Length:"));
        controlPanel.add(lengthSpinner);

        // Volume slider
        volumeSlider = new JSlider(SwingConstants.HORIZONTAL, 0, 127, trackEditor.getVolume());
        volumeSlider.setMajorTickSpacing(32);
        volumeSlider.setMinorTickSpacing(8);
        volumeSlider.setPaintTicks(true);
        volumeSlider.setPaintLabels(true);
        volumeSlider.addChangeListener(this);
        controlPanel.add(new JLabel("Volume:"));
        controlPanel.add(volumeSlider);

        // Instrument selection
        instrumentComboBox = new JComboBox<>(trackEditor.getInstrumentNames());
        instrumentComboBox.addActionListener(this);
        controlPanel.add(new JLabel("Instrument:"));
        controlPanel.add(instrumentComboBox);

        // Add the control panel to the edge
        add(controlPanel, BorderLayout.SOUTH);
    }

    // Abstract methods inherited from SketchingPanel
    @Override
    public SimpleSequencer getSequencer() {
        return trackEditor.getSequencer();
    }

    @Override
    public void setLength(int length) {
        trackEditor.setLength(length);
        lengthSpinner.setValue(length);
    }

    @Override
    public void setEvents(BetterDynamicArray<AudioEvent> events) {
        trackEditor.setEvents(events);
    }

    @Override
    public void clear() {
        trackEditor.clear();
    }

    // TrackPanel-specific methods
    /**
     * Gets the current volume of the track.
     * 
     * @return The volume level of the track.
     */
    public int getVolume(int channel) {
        return trackEditor.getVolume();
    }

    /**
     * Sets the volume for the track.
     * 
     * @param volume The desired volume level.
     */
    public void setVolume(int volume) {
        trackEditor.setVolume(volume);
        volumeSlider.setValue(volume);
    }

    /**
     * Gets the current instrument number for the track.
     * 
     * @return The instrument number.
     */
    public int getInstrument() {
        return instrumentNumber;
    }

    /**
     * Sets the instrument for the track.
     * 
     * @param instrument The index of the instrument in the combo box.
     */
    public void setInstrument(int channel, int instrument) {
        this.instrumentNumber = instrument;
        instrumentComboBox.setSelectedIndex(instrument);
        trackEditor.setInstrument(instrument);
    }

    // Handle button and combo box actions
    /**
     * Handles action events for the mute button and instrument combo box.
     * Toggles mute status and updates the instrument selection.
     * 
     * @param event The action event triggered by a user interaction.
     */
    @Override
    public void actionPerformed(ActionEvent event) {
        Object source = event.getSource();
        if (source == muteButton) {
            boolean isMuted = muteButton.isSelected();
            muteButton.setText(isMuted ? "Unmute" : "Mute");
            trackEditor.setMute(isMuted);
        } else if (source == instrumentComboBox) {
            setInstrument(instrumentComboBox.getSelectedIndex(), instrumentNumber);
            requestFocus(); // Return focus to the panel
        }
    }

    // Handle spinner and slider changes
    /**
     * Responds to changes in the length spinner and volume slider.
     * Updates the track properties accordingly.
     * 
     * @param event The change event triggered by a user interaction.
     */
    @Override
    public void stateChanged(ChangeEvent event) {
        Object source = event.getSource();
        if (source == lengthSpinner) {
            int length = (int) lengthSpinner.getValue();
            setLength(length);
        } else if (source == volumeSlider) {
            int volume = volumeSlider.getValue();
            setVolume(volume);
        }
    }
}
