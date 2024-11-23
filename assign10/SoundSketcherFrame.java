package assign10;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * This class represents the main window of the SoundSketcher application, 
 * containing a user interface for creating, editing, and controlling audio tracks and their playback. 
 * It provides controls for play, stop, loop, and tempo, as well as a tabbed pane for managing multiple tracks.
 * This frame is responsible for interacting with multiple track panels and controlling their properties 
 * such as tempo and looping. It also allows users to add new tracks up to a specified limit.
 */
public class SoundSketcherFrame extends JFrame implements ActionListener, ChangeListener {

    private static final int PANEL_WIDTH = 800;
    private static final int PANEL_HEIGHT = 800;
    private final int maxTracks = 16;
    private boolean addingTrack;

    // Instance variables
    private SongPanel songPanel;
    private BetterDynamicArray<TrackPanel> trackPanels;
    private JTabbedPane tracksPane;
    private JSlider tempoSlider;
    private JToggleButton playButton, loopButton;
    
    private SimpleSynthesizer synthesizer;

    /**
     * Constructs a SoundSketcherFrame, initializing the song panel, track panels, 
     * control buttons, and other components such as the tempo slider and tabbed pane.
     * Sets up the layout and display of the frame and its controls.
     */
    public SoundSketcherFrame() {
        super("SoundSketcher");
        
        synthesizer = new SimpleSynthesizer();

        // Initialize the song panel and track panels
        songPanel = new SongPanel(PANEL_WIDTH, PANEL_HEIGHT);
        trackPanels = new BetterDynamicArray<>();
        
        // Add the first track panel
        trackPanels.add(new TrackPanel(PANEL_WIDTH, PANEL_HEIGHT, 0, synthesizer));

        // Set up the control panel
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));

        // Play/Stop toggle button
        playButton = new JToggleButton("Play");
        playButton.addActionListener(this);
        controlPanel.add(playButton);

        // Loop toggle button
        loopButton = new JToggleButton("Loop");
        loopButton.addActionListener(this);
        controlPanel.add(loopButton);

        // Tempo slider
        tempoSlider = new JSlider(20, 200, 120); // BPM range
        tempoSlider.setMajorTickSpacing(40);
        tempoSlider.setMinorTickSpacing(10);
        tempoSlider.setPaintTicks(true);
        tempoSlider.setPaintLabels(true);
        tempoSlider.addChangeListener(this);
        controlPanel.add(new JLabel("Tempo (BPM):"));
        controlPanel.add(tempoSlider);

        // Set up the tabbed pane
        tracksPane = new JTabbedPane();
        tracksPane.addTab("Song", songPanel);
        tracksPane.addTab("Track 0", trackPanels.get(0));
        tracksPane.addTab("Add Track", new JPanel()); // Add Track tab
        tracksPane.setSelectedIndex(1); // Start with the first track visible
        tracksPane.addChangeListener(this);

        // Create a panel to hold the control panel and tabbed pane
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(controlPanel, BorderLayout.WEST);
        mainPanel.add(tracksPane, BorderLayout.CENTER);

        // Set up the JFrame
        setContentPane(mainPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setVisible(true);
    }

    /**
     * Adds a new track to the tabbed pane, provided the number of tracks does not exceed the maximum limit.
     * A new TrackPanel is created and added, and the tempo and loop state are applied to it.
     * Switches back to the last track after adding the new one.
     */
    public void addTrack() {
        addingTrack = true;
        if (trackPanels.size() < maxTracks) {
            TrackPanel newTrack = new TrackPanel(PANEL_WIDTH, PANEL_HEIGHT, trackPanels.size(), synthesizer);
            newTrack.setTempo(tempoSlider.getValue());
            newTrack.setLoop(loopButton.isSelected());
            trackPanels.add(newTrack);

            // Add the new track panel to the tabbed pane
            tracksPane.insertTab("Track " + (trackPanels.size() - 1), null,
                    trackPanels.get(trackPanels.size() - 1), null,
                    tracksPane.getTabCount() - 1);
        }
        tracksPane.setSelectedIndex(tracksPane.getTabCount() - 2); // Switch back to the last track
        addingTrack = false;
    }

    /**
     * Handles actions performed by the play and loop buttons.
     * Starts or stops playback for each track panel when the play button is toggled,
     * and sets the loop state for all track panels when the loop button is toggled.
     *
     * @param event the action event triggered by the button
     */
    @Override
    public void actionPerformed(ActionEvent event) {
        Object source = event.getSource();
        if (source == playButton) {
            // Play/Stop action
            if (playButton.isSelected()) {
                playButton.setText("Stop");
                // Call play() on each track panel using get() by index
                for (int i = 0; i < trackPanels.size(); i++) {
                    trackPanels.get(i).play();
                }
            } else {
                playButton.setText("Play");
                // Call stop() on each track panel using get() by index
                for (int i = 0; i < trackPanels.size(); i++) {
                    trackPanels.get(i).stop();
                }
            }
        } else if (source == loopButton) {
            // Set the loop feature for each track panel using get() by index
            for (int i = 0; i < trackPanels.size(); i++) {
                trackPanels.get(i).setLoop(loopButton.isSelected());
            }
        }
    }

    /**
     * Responds to changes in the tempo slider or tab selection.
     * Updates the tempo for all track panels and the song panel, 
     * and adds a new track if the "Add Track" tab is selected.
     *
     * @param event the change event triggered by the slider or tab change
     */
    @Override
    public void stateChanged(ChangeEvent event) {
        if (event.getSource() == tempoSlider) {
            // Set the tempo for each track panel and the song panel
            int tempo = tempoSlider.getValue();
            for (int i = 0; i < trackPanels.size(); i++) {
                trackPanels.get(i).setTempo(tempo);
            }
            songPanel.setTempo(tempo); // If the song panel has tempo control
        } else if ((event.getSource() == tracksPane) && (tracksPane.getSelectedIndex() == tracksPane.getTabCount() - 1) && !addingTrack) {
            addTrack(); // Add a new track when the "Add Track" tab is selected
        }
    }

   
}
