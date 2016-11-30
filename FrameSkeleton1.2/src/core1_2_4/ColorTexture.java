package core1_2_4;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;

public class ColorTexture implements TextureType{

	private Color c;
	private final String NAME;
	public ColorTexture(Color c, String name) {
		this.c = c;
		this.NAME = name;
	}
	
	@Override
	public Image getImage() {
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
		
		if(obj instanceof ColorTexture)
			return this.getColor() == ((ColorTexture) obj).getColor() && this.getNAME() == ((ColorTexture) obj).getNAME();
		return false;
	}
	
}