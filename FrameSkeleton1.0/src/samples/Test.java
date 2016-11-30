package samples;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import core.FrameSkeleton;

public class Test extends FrameSkeleton {

	public Test(int cellsize, int gridW, int gridH) {
		super(cellsize, gridW, gridH);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onNew() {Random r = ThreadLocalRandom.current();getGrid().setCellType(r.nextInt(10), r.nextInt(10), 1);
		getGrid().newCellType(10, Color.PINK, getGrid().SHAPE_RECT);
		setKeyListenerEnabled(true);
	}

	@Override
	public void onRun() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStep() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onKeyPressed(KeyEvent e) {
		System.out.println( " HALLLOOOOO");

	}

	@Override
	public void onKeyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	public static void main(String[] args) {new Test(10, 10, 10);
	}

}
