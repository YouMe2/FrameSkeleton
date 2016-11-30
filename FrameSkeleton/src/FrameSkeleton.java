import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;


/**
 * @author Yannik Alexander Eikmeier 
 * 
 */
public abstract class FrameSkeleton extends JFrame implements Runnable{

	private JPanel contentPane;
	private JPanel drawingPane;
	private JButton btn_new;
	private JButton btn_start;
	private JButton btn_stop;
	private JButton btn_step;
	private Graphics graphics; // use this obj to draw in the drawingPane
	private Thread thread;
	private boolean running = false;
	private int drawing_wh = 500; // use this int to change the size of the drawingPane
	
	private String title = "Frame Seleton Test";
	
	public FrameSkeleton(){
		System.out.println("FRAME");
		contentPane = new JPanel();
		contentPane.setLayout(null);
		contentPane.setBackground(Color.DARK_GRAY);
		this.setContentPane(contentPane);		
		this.setResizable(true);
		this.setTitle(title);
//		this.setUndecorated(true);		
		this.setSize(drawing_wh+10, drawing_wh+55);
		this.setLocationRelativeTo(null);
		
		drawingPane = new JPanel();
		drawingPane.setBounds(5, 5, drawing_wh, drawing_wh);
		drawingPane.setBackground(Color.WHITE);
		this.add(drawingPane);
		
		btn_new = new JButton("NEW");
		btn_new.setBounds(5, drawing_wh+10, 100, 40);
		btn_new.addActionListener(new ActionListener() {		
			@Override
			public void actionPerformed(ActionEvent e) {
				on_btnNew();			
			}
		});
		this.add(btn_new);
		
		btn_start = new JButton("START");
		btn_start.setBounds(110, drawing_wh+10, 100, 40);
		btn_start.addActionListener(new ActionListener() {		
			@Override
			public void actionPerformed(ActionEvent e) {
				onStart();			
			}
		});
		this.add(btn_start);
		
		btn_stop = new JButton("STOP");
		btn_stop.setBounds(215, drawing_wh+10, 100, 40);
		btn_stop.addActionListener(new ActionListener() {		
			@Override
			public void actionPerformed(ActionEvent e) {
				onStop();			
			}
		});
		this.add(btn_stop);
		
		btn_step = new JButton("STEP");
		btn_step.setBounds(320, drawing_wh+10, 100, 40);
		btn_step.addActionListener(new ActionListener() {		
			@Override
			public void actionPerformed(ActionEvent e) {
				onStep();			
			}
		});
		this.add(btn_step);
		
		
		this.setVisible(true);
		try {
			graphics = drawingPane.getGraphics();
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		this.addWindowListener(new WindowListener() {
			
			@Override
			public void windowOpened(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowIconified(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowDeiconified(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowDeactivated(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowClosing(WindowEvent arg0) {
				System.exit(0);
				
			}
			
			@Override
			public void windowClosed(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowActivated(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		btn_start.setEnabled(false);
		btn_stop.setEnabled(false);
		btn_step.setEnabled(false);
	}


	private void onStop() {
		running = false;		
		thread = null;
		btn_start.setEnabled(true);
		btn_stop.setEnabled(false);
		btn_step.setEnabled(true);
	}

	private void onStart() {
		if(thread == null){
			thread = new Thread(this);
		}
		
		running = true;
		thread.start();
		btn_start.setEnabled(false);
		btn_stop.setEnabled(true);
		btn_step.setEnabled(false);
	}

	private void on_btnNew() {
		onStop();
		onNew();
		btn_start.setEnabled(true);
		btn_stop.setEnabled(false);
		btn_step.setEnabled(true);
	}
	
	@Override
	public void run() {
		while(running){
			onRun();						
		}
		
	}
	
	public Graphics getGr(){
		return graphics;
	}
	
	public int getDrawingPaneSize(){
		return drawing_wh;
	}
	

	public abstract void onNew();
	
	public abstract void onRun();
	
	public abstract void onStep();

}
