package core;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * The FrameSkeleton class provides a simple way to implement a vast variety of
 * algorithms and games that take place in a grid. First of all the
 * {@link #getGrid} method should be use to get an instance of the used Grid.
 * 
 * @author Yannik Alexander Eikmeier
 * @version 1.2
 * @since 2016-07-15
 */
public abstract class FrameSkeleton extends JFrame implements Runnable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 975418976938484755L;
	private JPanel contentPane;
	private Canvas canvas;

	private JButton btn_new;
	private JButton btn_start;
	private JButton btn_stop;
	private JButton btn_step;

	private JLabel[] lbl_inputNr = new JLabel[3];
	private JTextField[] txt_input = new JTextField[3];
	private JLabel[] lbl_outputNr = new JLabel[3];
	private JLabel[] lbl_output = new JLabel[3];
	private JLabel lbl_msg;

	private Thread thread;
	private boolean running = false;
	private int draw_w = 500;
	private int draw_h = 500;
	private int cellsize;
	private FrameGrid grid;
	private BufferedImage img;
	private String title = "FrameSkeleton";

	private boolean gridlines = false;

	/**
	 * @param cellsize
	 *            the width and height of one cell (in pixels)
	 * @param gridW
	 *            the width of the grid (in cells)
	 * @param gridH
	 *            the height of the grid (in cells)
	 */
	public FrameSkeleton(int cellsize, int gridW, int gridH) {
		this.cellsize = cellsize;
		draw_w = cellsize * gridW;
		draw_h = cellsize * gridH;
		grid = new FrameGrid(gridW, gridH);
		img = new BufferedImage(draw_w, draw_h, BufferedImage.TYPE_INT_RGB);

		contentPane = new JPanel();
		contentPane.setForeground(Color.WHITE);
		contentPane.setLayout(null);
		contentPane.setBackground(Color.DARK_GRAY);
		contentPane.setPreferredSize(new Dimension(draw_w + 10, draw_h + 140));
		this.setTitle(title);
		this.setContentPane(contentPane);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);

		canvas = new Canvas();
		canvas.setBounds(5, 5, draw_w, draw_h);
		canvas.setBackground(Color.WHITE);
		contentPane.add(canvas);

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
		// this.requestFocus();

		btn_start.setEnabled(false);
		btn_stop.setEnabled(false);
		btn_step.setEnabled(false);

		setInputEnabled(0, false);
		setInputEnabled(1, false);
		setInputEnabled(2, false);
		setOutputEnabled(0, false);
		setOutputEnabled(1, false);
		setOutputEnabled(2, false);

		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		printGrid();
	}

	private void on_keyReleased(KeyEvent e) {
		onKeyReleased(e);
	}

	private void on_keyPressed(KeyEvent e) {
		onKeyPressed(e);
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
		if (isStarted())
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
	 * @return true after the "Start"-button was pressed and false after the
	 *         "Stop" or the "New"-button was pressed.
	 */
	public boolean isStarted() {
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
	 * Enables (or disables) the gridlines. The gridlines will only start to
	 * show after calling the {@code paintGrid} methode.
	 * 
	 * @param visible
	 *            true to enable the gridlines, otherwise false
	 */
	public void setGridlinesVisible(boolean visible) {
		gridlines = visible;
	}

	/**
	 * This method is called after the "New"-button was pressed. It can be used
	 * to initialise the program.
	 */
	public abstract void onNew();

	/**
	 * This method will continuously be called after the "Start"-button was
	 * pressed until the "Stop"-button is pressed.
	 */
	public abstract void onRun();

	/**
	 * This method is called after the "Step"button was pressed.
	 */
	public abstract void onStep();

	/**
	 * This method is called after the "Stop"button was pressed and the loop was
	 * stopped. This method will also be automatically called before the
	 * "onNew"-method will be called.
	 */
	public abstract void onStop();

	/**
	 * This method will be called after the press of any Key on the Keyboard,
	 * provided that the Frame is focused and the keylistener was enabled using
	 * the {@code setKeyListenerEnabled} method.
	 * 
	 * @param e
	 *            the KeyEvent that was triggered
	 */
	public void onKeyPressed(KeyEvent e) {

	}

	/**
	 * This method will be called after any Key on the Keyboard was released,
	 * provided that the Frame is focused and the keylistener was enabled using
	 * the {@code setKeyListenerEnabled} method.
	 * 
	 * @param e
	 *            the KeyEvent that was triggered
	 */
	public void onKeyReleased(KeyEvent e) {

	}

	/**
	 * This method will print all the data from the grid into the drawing pane.
	 */
	public void printGrid() {

		Graphics gr = img.getGraphics();

		gr.setColor(grid.getGridBackgroundColor());
		gr.fillRect(0, 0, draw_w, draw_h);

		for (int i = 0; i < grid.getWidth(); i++) {
			for (int j = 0; j < grid.getHeight(); j++) {
				gr.setColor(grid.getCellColor(i, j));
				if (grid.getCellShape(i, j) == FrameGrid.SHAPE_RECT)
					gr.fillRect(i * cellsize, j * cellsize, cellsize, cellsize);
				else
					gr.fillOval(i * cellsize, j * cellsize, cellsize, cellsize);
			}
		}

		if (gridlines) {
			for (int i = 0; i < grid.getWidth(); i++) {
				gr.setColor(Color.DARK_GRAY);
				gr.drawLine(i * cellsize, 0, i * cellsize, grid.getWidth() * cellsize - 1);
			}
			for (int j = 0; j < grid.getHeight(); j++) {
				gr.setColor(Color.DARK_GRAY);
				gr.drawLine(0, j * cellsize, grid.getHeight() * cellsize - 1, j * cellsize);
			}
		}

		gr.dispose();
		gr = canvas.getGraphics();
		gr.drawImage(img, 0, 0, null);
		gr.dispose();
	}

	/**
	 * This method enables or disables the output label specified by the id.
	 * 
	 * @param id
	 *            of the output label
	 * @param enabled
	 */
	public void setOutputEnabled(int id, boolean enabled) {
		lbl_output[id].setVisible(enabled);
		lbl_outputNr[id].setVisible(enabled);
		lbl_output[id].setEnabled(enabled);
	}

	/**
	 * This method enables or disables the input textfield specified by the id.
	 * 
	 * @param id
	 *            of the textfield
	 * @param enabled
	 */
	public void setInputEnabled(int id, boolean enabled) {
		txt_input[id].setVisible(enabled);
		lbl_inputNr[id].setVisible(enabled);
		txt_input[id].setEnabled(enabled);
	}

	/**
	 * This method sets a text into an output label.
	 * 
	 * @param id
	 *            of the label
	 * @param txt
	 *            to be set
	 */
	public void setOutputText(int id, String txt) {
		if (lbl_output[id].isEnabled())
			lbl_output[id].setText(txt);
	}

	/**
	 * This method returns the text in a textfield specified by the id.
	 * 
	 * @param id
	 *            of the textfield
	 * @return
	 */
	public String getInputText(int id) {
		if (txt_input[id].isEnabled())
			return txt_input[id].getText();
		return null;
	}

	/**
	 * This method sets a text into an input textfield.
	 * 
	 * @param id
	 *            of the textfiled
	 * @param txt
	 *            to be set
	 */
	public void setInputText(int id, String txt) {
		if (txt_input[id].isEnabled())
			txt_input[id].setText(txt);
	}

	/**
	 * This method prints a message into the msg label.
	 * 
	 * @param txt
	 *            message
	 */
	public void setMessageText(String txt) {
		lbl_msg.setText(txt);
	}
}
