package test;

import java.awt.Color;
import java.io.Serializable;

public enum CellType implements CType {

	A(Color.BLACK, false), B(Color.WHITE, false), C(Color.PINK, true);

	private final Color c;
	private final boolean isCircle;

	private CellType(Color c, boolean isCircle) {
		this.c = c;
		this.isCircle = isCircle;
	}

	@Override
	public Color getColor() {
		return c;
	}

	@Override
	public boolean isCircle() {
		return isCircle;
	}

}
