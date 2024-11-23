package assign10;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * A useful utility for checking the behavior of your GridCanvas, 
 * TrackEditor, and SongEditor classes. Uncomment the sections at
 * the beginning of the constructor as you complete your classes.
 * Note that some of the controls will appear not to work with your
 * TrackEditor and SongEditor classes.
 * 
 * @author Eric Heisler
 * @version 2024-11-12
 */
public class Assign10Utility extends JFrame implements ActionListener, ChangeListener{
	
	private GridCanvas grid;
	private JButton clearButton;
	private JToggleButton restrictXButton, restrictYButton;
	private JSlider rowsSlider, colsSlider;
	private int xRestrict, yRestrict;
	
	/**
	 * Creates the GUI for this test utility.
	 */
	public Assign10Utility() {
		//////////////////////////////////////////////////////////////////
		// Use this first after creating the GridCanvas class
		grid = new TestGrid(800, 800, 20, 20, 5, 5);
		
		// Uncomment this after completing TrackEditor
		grid = new TrackEditor(800, 800, 0, new SimpleSynthesizer());
		
		// Uncomment this after completing SongEditor
		BetterDynamicArray<TrackPanel> trackPanels = new BetterDynamicArray<TrackPanel>();
		trackPanels.add(new TrackPanel(100, 100, 0, new SimpleSynthesizer()));
		trackPanels.get(0).setLength(4);
		SongEditor se = new SongEditor(800, 800);
		se.setTrackList(trackPanels);
		grid = se;
		
		//////////////////////////////////////////////////////////////////
		
		clearButton = new JButton("Clear");
		clearButton.setPreferredSize(new Dimension(190, 40));
		clearButton.addActionListener(this);
		
		xRestrict = -1;
		yRestrict = -1;
		restrictXButton = new JToggleButton("Restrict columns");
		restrictXButton.setPreferredSize(new Dimension(190, 40));
		restrictXButton.addActionListener(this);
		
		restrictYButton = new JToggleButton("Restrict rows");
		restrictYButton.setPreferredSize(new Dimension(190, 40));
		restrictYButton.addActionListener(this);
		
		rowsSlider = new JSlider(SwingConstants.VERTICAL, 0, 100, 20);
		rowsSlider.setPreferredSize(new Dimension(50, 350));
		rowsSlider.setMajorTickSpacing(20);
		rowsSlider.setMinorTickSpacing(5);
		rowsSlider.setPaintTicks(true);
		rowsSlider.setPaintLabels(true);
		rowsSlider.addChangeListener(this);
		
		colsSlider = new JSlider(SwingConstants.VERTICAL, 0, 100, 20);
		colsSlider.setPreferredSize(new Dimension(50, 350));
		colsSlider.setMajorTickSpacing(20);
		colsSlider.setMinorTickSpacing(5);
		colsSlider.setPaintTicks(true);
		colsSlider.setPaintLabels(true);
		colsSlider.addChangeListener(this);
		
		JPanel sliderPanel = new JPanel();
		sliderPanel.setPreferredSize(new Dimension(200, 370));
		sliderPanel.add(rowsSlider);
		sliderPanel.add(colsSlider);
		
		JPanel controlPanel = new JPanel();
		controlPanel.setPreferredSize(new Dimension(200, 800));
		controlPanel.add(clearButton);
		controlPanel.add(restrictXButton);
		controlPanel.add(restrictYButton);
		controlPanel.add(new JLabel("rows         cols"));
		controlPanel.add(sliderPanel);
		
		JPanel fullPanel = new JPanel();
		fullPanel.setLayout(new BorderLayout());
		fullPanel.add(controlPanel, BorderLayout.WEST);
		fullPanel.add(grid, BorderLayout.CENTER);
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setContentPane(fullPanel);
		pack();
	}
	
	/**
	 * Handles button clicks.
	 */
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == clearButton) {
			grid.clear();
		} else if(e.getSource() == restrictXButton) {
			if(restrictXButton.isSelected())
				xRestrict = 1;
			else
				xRestrict = -1;
			grid.setRestrictions(yRestrict, xRestrict);
		} else if(e.getSource() == restrictYButton) {
			if(restrictYButton.isSelected())
				yRestrict = 1;
			else
				yRestrict = -1;
			grid.setRestrictions(yRestrict, xRestrict);
		}
	}
	
	/**
	 * Handles changes to the sliders.
	 */
	public void stateChanged(ChangeEvent e) {
		if(e.getSource() == rowsSlider) {
			grid.setRows(rowsSlider.getValue());
		} else if(e.getSource() == colsSlider) {
			grid.setColumns(colsSlider.getValue());
		}
	}
	
	/**
	 * The main program that runs this GUI.
	 * @param args
	 */
	public static void main(String[] args) {
		Assign10Utility frame = new Assign10Utility();
		frame.setVisible(true);
	}
	
	/**
	 * A class that extends your GridCanvas class for testing.
	 */
	private class TestGrid extends GridCanvas {
		public TestGrid(int width, int height, int rows, int columns, 
							int rowMajorTics, int columnMajorTics) {
			super(width, height, rows, columns, rowMajorTics, columnMajorTics);
		}
		
		// These methods don't do anything in this test class.
		// They are needed to extend the abstract class.
		public void onCellPressed(int row, int col, int rowSpan, int colSpan) {}
		public void onCellDragged(int row, int col, int rowSpan, int colSpan) {}
		public void onCellReleased(int row, int col, int rowSpan, int colSpan) {}
		public void onCellRemoved(int row, int col) {}
		
		// Required by a serializable class (ignore for now)
		@SuppressWarnings("unused")
		private static final long serialVersionUID = 1L;
	}
	
	// Required by a serializable class (ignore for now)
	@SuppressWarnings("unused")
	private static final long serialVersionUID = 1L;
}
