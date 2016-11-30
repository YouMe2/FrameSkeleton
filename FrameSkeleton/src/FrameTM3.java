import java.awt.event.KeyEvent;


public class FrameTM3 extends FrameSkeleton3 {

	Grid grid;
	
	public static void main(String[] args) {
		new FrameTM3();

	}
	
	public FrameTM3() {
		super(15, 20, 20);
		grid = getGrid();
	}

	@Override
	public void onNew() {
		grid.setCellType(0, 0, 0);
		grid.setCellType(1, 1, 1);
		grid.setCellType(2, 2, 2);
		grid.setCellType(3, 3, 3);
		grid.setCellType(4, 4, 4);
		grid.setCellType(5, 5, 5);
		grid.setCellType(6, 6, 6);
		grid.setCellType(7, 7, 7);
		grid.setCellType(8, 8, 8);
		grid.setCellType(9, 9, 9);
		
		printGrid();
	}

	@Override
	public void onRun() {
		// TODO Auto-generated method stub

	}
	
	boolean b = false;
	@Override
	public void onStep() {	
		setGridlinesVisible(b = !b);
		setKeyListenerEnabled(b);
		printGrid();
	}

	@Override
	public void onKeyPressed(KeyEvent e) {
		System.out.println("PRESS");
		
	}

	@Override
	public void onKeyReleased(KeyEvent e) {
		System.out.println("RE");
		
	}

	

}
