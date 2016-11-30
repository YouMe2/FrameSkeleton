import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.security.Key;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public abstract class FrameSkeleton3 extends JFrame implements Runnable {

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
	private Grid grid;
	private BufferedImage img;
	private String title = "FrameSkeleton";
	
	private boolean gridlines = false;
	private boolean keyListenerEnabled = false;
	
	
	

	public FrameSkeleton3(int cellsize, int gridW, int gridH) {
		this.cellsize = cellsize;
		draw_w = cellsize * gridW;
		draw_h = cellsize * gridH;
		grid = new Grid(gridW, gridH);
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
	
	public void setKeyListenerEnabled(boolean b){
		keyListenerEnabled = b;
	}
	
	public boolean isKeyListenerEnabled(){
		return keyListenerEnabled;
	}

	private void on_btnStop() {
		running = false;
		thread = null;
		btn_start.setEnabled(true);
		btn_stop.setEnabled(false);
		btn_step.setEnabled(true);
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

	public boolean isStarted(){
		return running;
	}
	
	@Override
	public void run() {
		while (running) {
			onRun();
		}

	}

	public Grid getGrid() {
		return grid;
	}

	public void setGridlinesVisible(boolean visible){
		gridlines = visible;
	}
	
	public abstract void onNew();

	public abstract void onRun();

	public abstract void onStep();
	
	public abstract void onKeyPressed(KeyEvent e);
	
	public abstract void onKeyReleased(KeyEvent e);

	public void printGrid() {
		
		graphics_img.setColor(grid.getGridBackgroundColor());
		graphics_img.fillRect(0, 0, draw_w, draw_h);
		
		graphics_pnl = drawingPane.getGraphics();

		for (int i = 0; i < grid.getW(); i++) {
			for (int j = 0; j < grid.getH(); j++) {
				graphics_img.setColor(grid.getCellColor(i, j));
				if(grid.getCellShape(i, j) == Grid.RECT_SHAPE)
					graphics_img.fillRect(i * cellsize, j * cellsize, cellsize, cellsize);
				else
					graphics_img.fillOval(i * cellsize, j * cellsize, cellsize, cellsize);
			}
		}
		
		if(gridlines){
			for (int i = 0; i < grid.getW(); i++) {
				graphics_img.setColor(Color.DARK_GRAY);
				graphics_img.drawLine(i*cellsize, 0, i*cellsize, grid.getW()*cellsize-1);
			}
			for (int j = 0; j < grid.getH(); j++) {
				graphics_img.setColor(Color.DARK_GRAY);
				graphics_img.drawLine(0, j*cellsize, grid.getH()*cellsize-1, j*cellsize);
			}
		}

		graphics_pnl.drawImage(img, 0, 0, null);
	}

	public class Grid {
		
		private static final boolean RECT_SHAPE = false;
		private static final boolean CIRCLE_SHAPE = true;
		
		private int[][] grid;
		private Color grid_bg;
		private Map<Integer, Color> celltypeColors = new HashMap<Integer, Color>();
		private Map<Integer, Boolean> celltypeShapes = new HashMap<Integer, Boolean>();
		
		public Grid(int w, int h) {
			grid = new int[w][h];
			setGridBackground(Color.WHITE);
			newCellType(0, Color.WHITE, RECT_SHAPE);
			newCellType(1, Color.BLACK, RECT_SHAPE);
			newCellType(2, Color.RED, RECT_SHAPE);
			newCellType(3, Color.GREEN, RECT_SHAPE);
			newCellType(4, Color.BLUE,CIRCLE_SHAPE);
			newCellType(5, Color.MAGENTA, CIRCLE_SHAPE);
			newCellType(6, Color.YELLOW, CIRCLE_SHAPE);
			newCellType(7, Color.CYAN, CIRCLE_SHAPE);
			newCellType(8, Color.LIGHT_GRAY, RECT_SHAPE);
			newCellType(9, Color.DARK_GRAY, RECT_SHAPE);
		}

		public void setGridBackground(Color color) {
			grid_bg = color;			
		}
		
		public Color getGridBackgroundColor(){
			return grid_bg;
		}

		public void newCellType(int type, Color color, boolean shape) {
			if (celltypeColors.containsKey(type))
				celltypeColors.remove(type);
			celltypeColors.put(type, color);
			if (celltypeShapes.containsKey(type))
				celltypeShapes.remove(type);
			celltypeShapes.put(type, shape);		
		}

		public void setCellType(int x, int y, int type) {
			grid[x][y] = type;
		}

		public int getCellType(int x, int y) {
			return grid[x][y];
		}

		public Color getCellColor(int x, int y) {
//			Color c = celltypeColors.get(getCellType(x, y));
//			if(c == null){
//				c = Color.PINK;
//			}
//			return c;
			return celltypeColors.get(getCellType(x, y));
			
		}
		
		public boolean getCellShape(int x, int y) {
			return celltypeShapes.get(getCellType(x, y));
		}

		public int getW() {
			return grid.length;
		}

		public int getH() {
			return grid[0].length;
		}

		public void resetGridValues() {

			for (int i = 0; i < grid.length; i++) {
				Arrays.fill(grid[i], 0);
			}

		}

	}

}
