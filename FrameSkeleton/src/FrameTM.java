import java.awt.Color;


public class FrameTM extends FrameSkeleton2 {
	
	
	Grid grid;
	int x;
	int y;
	
	
	public static void main(String[] args) {
		new FrameTM();
	}
	
	public FrameTM() {
		super(20, 20, 20);
		grid = super.getGrid();
//		grid.newCellTyp(0, Color.WHITE);  is already default
		
	}

	@Override
	public void onNew() {
		x = 0;
		y = 3;
		grid.resetGridValues();
		
		grid.setCell(5, 5, 1);
		grid.setCell(4, 5, 1);
		grid.setCell(3, 5, 1);
		grid.setCell(2, 5, 1);
		grid.setCell(1, 5, 1);
		
		grid.setCell(0, 0, 7);
		grid.setCell(19, 0, 6);
		grid.setCell(0, 19, 5);
		grid.setCell(19, 19, 4);
		
		
		
		showGrid();
//		System.out.println();
	}

	@Override
	public void onRun() {
		onStep();
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onStep() {
		
		grid.setCell(x, y, 0);		
		grid.setCell( x < grid.getW()-1 ? (++x) : (x=0) , y, 2);		
		showGrid();
	}

	

}
