package test;

import java.awt.Color;

public class Main {

	public static void main(String[] args) {

		CType a = CellType.A;
		System.out.println(a);
		
		
		
		CType g = new CType() {
			
			@Override
			public boolean isCircle() {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public Color getColor() {
				// TODO Auto-generated method stub
				return Color.BLACK;
			}
		};
	}

}