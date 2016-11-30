import java.util.concurrent.ThreadLocalRandom;


public class SierpinskiTestF extends FrameSkeleton2 {

	Grid grid;
	int x;
	int y;
	
	static int size = 400;
	
	
	public SierpinskiTestF() {
		super(1, size, size);
		grid = super.getGrid();
	}

	@Override
	public void onNew() {
		grid.resetGridValues();
//		x = (int) (Math.random()*size);
//		y = (int) (Math.random()*size);
		showGrid();
	}

	@Override
	public void onRun() {
		for(int i=0; i < 100; i++)
			onStep();
		showGrid();
	}

	@Override
	public void onStep() {
		grid.setCell(x, y, 1);
		int[] d = getDConer((int) (Math.random()*3));

		x = x + (d[0]-x)/2;
		y = y + (d[1]-y)/2;
		grid.setCell(x, y, 1);
	}

	public int[] getDConer(int id){
		switch (id) {
		case 0:
			return new int[]{5, size-5};
			
		case 1:
			return new int[]{size-4, size-5};
			
		case 2:
			return new int[]{size/2, (int) ((size-5)-(size-10)*Math.sin(Math.PI/3))};
			
		default:
			return null;
		}
	}
	
 	public static void main(String[] args) {
		new SierpinskiTestF();

	}

}
