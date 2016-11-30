package core;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
/**
* The FrameSkeleton class provides a simple way to implement
* a vast variety of algorithms and games that take place in a
* grid.
* 
* @author  Yannik Alexander Eikmeier
* @version 1.1
* @since   2016-06-22 
*/
public abstract class FrameSkeleton extends JFrame implements Runnable {

	private JPanel contentPane;
	private JPanel drawingPane;
	private JButton btn_new;
	private JButton btn_start;
	private JButton btn_stop;
	private JButton btn_step;
	private Graphics graphics_pnl;
	private Graphics graphics_img;
	private Thread thread;
	private boolean running = false;
	private int draw_w = 500;
	private int draw_h = 500;
	private int cellsize;
	private FrameGrid grid;
	private BufferedImage img;
	private String title = "FrameSkeleton";
	
	private boolean gridlines = false;
	private boolean keyListenerEnabled = false;
	
	
	

	/**
	 * @param cellsize the width and height of one cell (in pixels)
	 * @param gridW the width of the grid (in cells)
	 * @param gridH the height of the grid (in cells)
	 */
	public FrameSkeleton(int cellsize, int gridW, int gridH) {
		this.cellsize = cellsize;
		draw_w = cellsize * gridW;
		draw_h = cellsize * gridH;
		grid = new FrameGrid(gridW, gridH);
		img = new BufferedImage(draw_w, draw_h, BufferedImage.TYPE_INT_RGB);
		graphics_img = img.getGraphics().create();

		contentPane = new JPanel();
		contentPane.setLayout(null);
		contentPane.setBackground(Color.DARK_GRAY);
		this.setResizable(true);
		this.setTitle(title);
		this.setContentPane(contentPane);
		this.setSize((draw_w < 415?425:draw_w+10) + 15, draw_h + 55 + 40);
//		this.setSize((draw_w < 415?425:draw_w+10), draw_h + 55);
		this.setLocationRelativeTo(null);

		drawingPane = new JPanel();
		drawingPane.setBounds(5, 50, draw_w, draw_h);
		drawingPane.setBackground(Color.WHITE);
		contentPane.add(drawingPane);

		btn_new = new JButton("NEW");
		btn_new.setBounds(5, 5, 100, 40);
		btn_new.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				on_btnNew();
			}
		});
		contentPane.add(btn_new);

		btn_start = new JButton("START");
		btn_start.setBounds(110, 5, 100, 40);
		btn_start.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				on_btnStart();
			}
		});
		contentPane.add(btn_start);

		btn_stop = new JButton("STOP");
		btn_stop.setBounds(215, 5, 100, 40);
		btn_stop.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				on_btnStop();
			}
		});
		contentPane.add(btn_stop);

		btn_step = new JButton("STEP");
		btn_step.setBounds(320, 5, 100, 40);
		btn_step.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				on_btnStep();
				
			}
		});
		contentPane.add(btn_step);
		
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {				
				on_btnStop();
				System.exit(0);
			}
		});
		
		this.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				on_keyPressed(e);
				
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				on_keyReleased(e);
			}

			
		});
//		this.setFocusable(true);
//		this.requestFocus();
		
		btn_start.setEnabled(false);
		btn_stop.setEnabled(false);
		btn_step.setEnabled(false);
		this.setVisible(true);
		printGrid();
	}

	private void on_keyReleased(KeyEvent e) {
		if(isKeyListenerEnabled())
			onKeyPressed(e);		
	}

	private void on_keyPressed(KeyEvent e) {
		if(isKeyListenerEnabled())
			onKeyReleased(e);		
	}
	
	/**
	 * Enables (or disables) the keylisterner.
	 * This must be set to {@code true} in order for the
	 * {@link #onKeyPressed()} and {@link #onKeyReleases} to be called.
	 * 
	 * @param enabled true to enable the keylisterner, otherwise false
	 */
	public void setKeyListenerEnabled(boolean enabled){
		keyListenerEnabled = enabled;
	}
	
	/**
	 * 
	 * 
	 * @return the state of the keylistener
	 */
	public boolean isKeyListenerEnabled(){
		return keyListenerEnabled;
	}

	private void on_btnStop() {
		running = false;
		thread = null;
		btn_start.setEnabled(true);
		btn_stop.setEnabled(false);
		btn_step.setEnabled(true);
		onStop();
		this.requestFocus();
	}

	private void on_btnStart() {
		if (thread == null) {
			thread = new Thread(this);
		}

		running = true;
		thread.start();
		btn_start.setEnabled(false);
		btn_stop.setEnabled(true);
		btn_step.setEnabled(false);
		this.requestFocus();
	}

	private void on_btnNew() {
		
		on_btnStop();
		
		onNew();
		
		btn_start.setEnabled(true);
		btn_stop.setEnabled(false);
		btn_step.setEnabled(true);
		this.requestFocus();
	}

	private void on_btnStep() {
		onStep();
		this.requestFocus();
	}

	/**
	 * @return true after the "Start"-button was pressed and false after the "Stop" or the "New"-button was pressed.
	 */
	public boolean isStarted(){
		return running;
	}
	
	@Override
	public void run() {
		while (running) {
			onRun();
		}

	}

	/**
	 * @return an instance of the grid that is being used by the Frame
	 */
	public FrameGrid getGrid() {
		return grid;
	}

	/**
	 * Enables (or disables) the gridlines.
	 * The gridlines will only start to show after calling the {@code paintGrid} methode.
	 * 
	 * @param visible true to enable the gridlines, otherwise false
	 */
	public void setGridlinesVisible(boolean visible){
		gridlines = visible;
	}
	
	/**
	 * This method is called after the "New"-button was pressed.
	 * It can be used to initialise the program.
	 */
	public abstract void onNew();

	/**
	 * This method will continuously be called after the "Start"-button
	 * was pressed until the "Stop"-button is pressed.
	 */
	public abstract void onRun();

	/**
	 * This method is called after the "Step"button was pressed.
	 */
	public abstract void onStep();
	
	/**
	 * This method is called after the "Stop"button was pressed 
	 * and the loop was stopped.
	 * This method will also be automatically called before the "onNew"-method will be called.
	 */
	public abstract void onStop();
	
	/**
	 * This method will be called after the press of any Key
	 * on the Keyboard, provided that the Frame is focused 
	 * and the keylistener was enabled using the {@code setKeyListenerEnabled} method.
	 * 
	 * @param e the KeyEvent that was triggered
	 */
	public abstract void onKeyPressed(KeyEvent e);
	
	/**
	 * This method will be called after any Key
	 * on the Keyboard was released, provided that the Frame is focused 
	 * and the keylistener was enabled using the {@code setKeyListenerEnabled} method.
	 * 
	 * @param e the KeyEvent that was triggered
	 */
	public abstract void onKeyReleased(KeyEvent e);

	/**
	 * This method will print all the data from the grid into the drawing pane.
	 */
	public void printGrid() {
		
		graphics_img.setColor(grid.getGridBackgroundColor());
		graphics_img.fillRect(0, 0, draw_w, draw_h);
		
		graphics_pnl = drawingPane.getGraphics();

		for (int i = 0; i < grid.getWidth(); i++) {
			for (int j = 0; j < grid.getHeight(); j++) {
				graphics_img.setColor(grid.getCellColor(i, j));
				if(grid.getCellShape(i, j) == FrameGrid.SHAPE_RECT)
					graphics_img.fillRect(i * cellsize, j * cellsize, cellsize, cellsize);
				else
					graphics_img.fillOval(i * cellsize, j * cellsize, cellsize, cellsize);
			}
		}
		
		if(gridlines){
			for (int i = 0; i < grid.getWidth(); i++) {
				graphics_img.setColor(Color.DARK_GRAY);
				graphics_img.drawLine(i*cellsize, 0, i*cellsize, grid.getWidth()*cellsize-1);
			}
			for (int j = 0; j < grid.getHeight(); j++) {
				graphics_img.setColor(Color.DARK_GRAY);
				graphics_img.drawLine(0, j*cellsize, grid.getHeight()*cellsize-1, j*cellsize);
			}
		}

		graphics_pnl.drawImage(img, 0, 0, null);
	}

	

}
