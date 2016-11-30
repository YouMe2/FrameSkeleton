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
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.Font;
/**
* The FrameSkeleton class provides a simple way to implement
* a vast variety of algorithms and games that take place in a
* grid. First of all the {@link #getGrid} method should be use to get an instance of the used Grid.
* 
* @author  Yannik Alexander Eikmeier
* @version 1.2
* @since   2016-07-15 
*/
public abstract class FrameSkeleton extends JFrame implements Runnable {

	private JPanel contentPane;
	private JPanel drawingPane;
	private JButton btn_new;
	private JButton btn_start;
	private JButton btn_stop;
	private JButton btn_step;
	private JLabel[] lbl_inputNr = new JLabel[3];
	private JTextField[] txt_input = new JTextField[3];
	private JLabel[] lbl_outputNr = new JLabel[3];
	private JLabel[] lbl_output = new JLabel[3];
	private JLabel lbl_msg;
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
		contentPane.setForeground(Color.WHITE);
		contentPane.setLayout(null);
		contentPane.setBackground(Color.DARK_GRAY);
		this.setResizable(true);
		this.setTitle(title);
		this.setContentPane(contentPane);
//		this.setSize((draw_w < 375?385:draw_w+10), draw_h + 135);			/*Standart (unter Linux passende Größe)*/
		this.setSize((draw_w < 375?385:draw_w+10) +15 , draw_h + 135 + 40);	/*unter Windows passende Größe...*/
		this.setLocationRelativeTo(null);


		drawingPane = new JPanel();
		drawingPane.setBounds(5, 5, draw_w, draw_h);
		drawingPane.setBackground(Color.WHITE);
		contentPane.add(drawingPane);

		lbl_msg = new JLabel("Msg:");
		lbl_msg.setForeground(Color.WHITE);
		lbl_msg.setBounds(10, draw_h + 15, 375, 15);
		contentPane.add(lbl_msg);
		
		
		btn_new = new JButton("NEW");
		btn_new.setBounds(5, draw_h + 40, 86, 30);
		btn_new.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				on_btnNew();
			}
		});
		contentPane.add(btn_new);

		btn_start = new JButton("START");
		btn_start.setBounds(101, draw_h + 40, 86, 30);
		btn_start.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				on_btnStart();
			}
		});
		contentPane.add(btn_start);

		btn_stop = new JButton("STOP");
		btn_stop.setBounds(197, draw_h + 40, 86, 30);
		btn_stop.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				on_btnStop();
			}
		});
		contentPane.add(btn_stop);

		btn_step = new JButton("STEP");
		btn_step.setBounds(293, draw_h + 40, 86, 30);
		btn_step.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				on_btnStep();
				
			}
		});
		contentPane.add(btn_step);
		
		lbl_inputNr[0] = new JLabel("0:");
		lbl_inputNr[0].setFont(new Font("Tahoma", Font.BOLD, 11));
		lbl_inputNr[0].setBounds(10, draw_h + 110, 15, 20);
		lbl_inputNr[0].setForeground(Color.LIGHT_GRAY);
		contentPane.add(lbl_inputNr[0]);
		
		txt_input[0] = new JTextField(" input");
		txt_input[0].setBounds(25, draw_h + 110, 100, 20);
		txt_input[0].setEnabled(true);
		contentPane.add(txt_input[0]);
		
		lbl_inputNr[1] = new JLabel("1:");
		lbl_inputNr[1].setFont(new Font("Tahoma", Font.BOLD, 11));
		lbl_inputNr[1].setBounds(135, draw_h + 110, 15, 20);
		lbl_inputNr[1].setForeground(Color.LIGHT_GRAY);
		contentPane.add(lbl_inputNr[1]);
		
		txt_input[1] = new JTextField(" input");
		txt_input[1].setBounds(150, draw_h + 110, 100, 20);
		txt_input[1].setEnabled(true);
		contentPane.add(txt_input[1]);
		
		lbl_inputNr[2] = new JLabel("2:");
		lbl_inputNr[2].setFont(new Font("Tahoma", Font.BOLD, 11));
		lbl_inputNr[2].setBounds(260, draw_h + 110, 15, 20);
		lbl_inputNr[2].setForeground(Color.LIGHT_GRAY);
		contentPane.add(lbl_inputNr[2]);
		
		txt_input[2] = new JTextField(" input");
		txt_input[2].setBounds(275, draw_h + 110, 100, 20);
		txt_input[2].setEnabled(true);
		contentPane.add(txt_input[2]);
		
		
		lbl_outputNr[0] = new JLabel("0:");
		lbl_outputNr[0].setFont(new Font("Tahoma", Font.BOLD, 11));
		lbl_outputNr[0].setBounds(10, draw_h + 80, 15, 20);
		lbl_outputNr[0].setForeground(Color.LIGHT_GRAY);
		contentPane.add(lbl_outputNr[0]);
		
		lbl_output[0] = new JLabel(" output");
		lbl_output[0].setBounds(25, draw_h + 80, 100, 20);
		lbl_output[0].setForeground(Color.WHITE);
		contentPane.add(lbl_output[0]);
		
		lbl_outputNr[1] = new JLabel("1:");
		lbl_outputNr[1].setFont(new Font("Tahoma", Font.BOLD, 11));
		lbl_outputNr[1].setBounds(135, draw_h + 80, 15, 20);
		lbl_outputNr[1].setForeground(Color.LIGHT_GRAY);
		contentPane.add(lbl_outputNr[1]);
		
		lbl_output[1] = new JLabel(" output");
		lbl_output[1].setBounds(150, draw_h + 80, 100, 20);
		lbl_output[1].setForeground(Color.WHITE);
		contentPane.add(lbl_output[1]);
		
		lbl_outputNr[2] = new JLabel("2:");
		lbl_outputNr[2].setFont(new Font("Tahoma", Font.BOLD, 11));
		lbl_outputNr[2].setBounds(260, draw_h + 80, 15, 20);
		lbl_outputNr[2].setForeground(Color.LIGHT_GRAY);
		contentPane.add(lbl_outputNr[2]);
		
		lbl_output[2] = new JLabel(" output");
		lbl_output[2].setBounds(275, draw_h + 80, 100, 20);
		lbl_output[2].setForeground(Color.WHITE);
		contentPane.add(lbl_output[2]);
		
		
		
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
		
		setInputEnabled(0, false);
		setInputEnabled(1, false);
		setInputEnabled(2, false);
		setOutputEnabled(0, false);
		setOutputEnabled(1, false);
		setOutputEnabled(2, false);

		
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

	public void on_btnStop() {
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
		if(isStarted())
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

	/**
	 * This method enables or disables the output label specified by the id. 
	 * 
	 * @param id of the output label
	 * @param enabled 
	 */
	public void setOutputEnabled(int id, boolean enabled){
		lbl_output[id].setVisible(enabled);
		lbl_outputNr[id].setVisible(enabled);
		lbl_output[id].setEnabled(enabled);
	}
	
	/**
	 * This method enables or disables the input textfield specified by the id. 
	 * 
	 * @param id of the textfield
	 * @param enabled
	 */
	public void setInputEnabled(int id, boolean enabled){
		txt_input[id].setVisible(enabled);
		lbl_inputNr[id].setVisible(enabled);
		txt_input[id].setEnabled(enabled);
	}
	
	/**
	 * This method sets a text into an output label.
	 * 
	 * @param id of the label
	 * @param txt to be set
	 */
	public void setOutputText(int id, String txt){
		if(lbl_output[id].isEnabled())
			lbl_output[id].setText(txt);
	}
	
	/**
	 * This method returns the text in a textfield specified by the id.
	 * 
	 * @param id of the textfield
	 * @return
	 */
	public String getInputText(int id){
		if(txt_input[id].isEnabled())
			return txt_input[id].getText();
		return null;
	}
	
	/**
	 * This method sets a text into an input textfield.
	 * 
	 * @param id of the textfiled
	 * @param txt to be set
	 */
	public void setInputText(int id, String txt){
		if(txt_input[id].isEnabled())
			txt_input[id].setText(txt);
	}
	
	/**
	 * This method prints a message into the msg label.
	 * 
	 * @param txt message
	 */
	public void setMessageText(String txt){
		lbl_msg.setText(txt);
	}
}
