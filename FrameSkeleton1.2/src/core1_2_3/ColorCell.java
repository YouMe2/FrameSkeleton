package core1_2_3;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;

public class ColorCell implements CellType{

	private Color c;
	private final String NAME;
	public ColorCell(Color c, String name) {
		this.c = c;
		this.NAME = name;
	}
	
	@Override
	public Image getTexture() {
		Image img = new BufferedImage(1, 1, BufferedImage.TYPE_4BYTE_ABGR);
		
		img.getGraphics().setColor(c);
		img.getGraphics().fillRect(0,0,1,1);
		
		return img;
	}

	@Override
	public boolean hasTrasparency() {
		
		return c.getAlpha() != 255;
	}
	
	@Override
	public String getNAME() {
		return toString();
	}
	
	public Color getColor(){
		return c;
	}

	@Override
	public String toString() {
	return NAME;
	}
	
	@Override
	public boolean equals(Object obj) {
		
		if(obj instanceof ColorCell)
			return this.getColor() == ((ColorCell) obj).getColor() && this.getNAME() == ((ColorCell) obj).getNAME();
		return false;
	}
	
}