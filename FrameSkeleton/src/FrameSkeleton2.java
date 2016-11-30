import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public abstract class FrameSkeleton2 extends JFrame implements Runnable {

	private JPanel contentPane;
	private Canvas drawingCanvas;
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
	private String title = "Frame Seleton Test";

	public FrameSkeleton2(int cellsize, int gridW, int gridH) {
		
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
		// this.setUndecorated(true);
		this.setContentPane(contentPane);
		this.setSize(draw_w + 10 + 16, draw_h + 55 + 40);
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
		
		
	
		this.setVisible(true);


		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				on_btnStop();
				System.exit(0);
			}
		});

		
		btn_start.setEnabled(false);
		btn_stop.setEnabled(false);
		btn_step.setEnabled(false);
	}

	private void on_btnStop() {
		running = false;
		thread = null;
		btn_start.setEnabled(true);
		btn_stop.setEnabled(false);
		btn_step.setEnabled(true);
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
	}

	static int count = 0;

	private void on_btnNew() {
		
		on_btnStop();
		
		onNew();
		
		btn_start.setEnabled(true);
		btn_stop.setEnabled(false);
		btn_step.setEnabled(true);
		
	}


	private void on_btnStep() {
		onStep();
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

	public abstract void onNew();

	public abstract void onRun();

	public abstract void onStep();

	public void showGrid() {

		
		graphics_pnl = drawingPane.getGraphics();

		for (int i = 0; i < grid.getW(); i++) {
			for (int j = 0; j < grid.getH(); j++) {
				graphics_img.setColor(grid.getCellColor(i, j));
				graphics_img.fillRect(i * cellsize, j * cellsize, cellsize,
						cellsize);
			}
		}

		graphics_pnl.drawImage(img, 0, 0, null);
	}

	public class Grid {
		private int[][] grid;
		private Map<Integer, Color> celltypes = new HashMap<Integer, Color>();

		public Grid(int w, int h) {
			grid = new int[w][h];
			newCellTyp(0, Color.WHITE);
			newCellTyp(1, Color.BLACK);
			newCellTyp(2, Color.RED);
			newCellTyp(3, Color.GREEN);
			newCellTyp(4, Color.BLUE);
			newCellTyp(5, Color.MAGENTA);
			newCellTyp(6, Color.YELLOW);
			newCellTyp(7, Color.CYAN);
			newCellTyp(8, Color.LIGHT_GRAY);
			newCellTyp(9, Color.DARK_GRAY);
		}

		public void newCellTyp(int value, Color color) {
			if (celltypes.containsKey(value))
				celltypes.remove(value);
			celltypes.put(value, color);
		}

		public void setCell(int x, int y, int value) {
			grid[x][y] = value;
		}

		public int getCellValue(int x, int y) {
			return grid[x][y];
		}

		public Color getCellColor(int x, int y) {
			// System.out.println(x+" "+y+" "+getCellValue(x, y));
			return celltypes.get(getCellValue(x, y));
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
