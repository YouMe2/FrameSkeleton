package core1_2_4;

import java.awt.Image;

public class ImageTexture implements TextureType{

		private Image img;
		private boolean trans;
		private final String NAME;
		
		public ImageTexture(Image img, boolean transparency, String name) {
			this.img = img;
			this.NAME = name;
			this.trans = transparency;
		}
		
		@Override
		public Image getImage() {
			
			return img;
		}

		@Override
		public boolean hasTrasparency() {
			
			return trans;
		}

		@Override
		public String getNAME() {
			return toString();
		}

		@Override
		public String toString() {
		return NAME;
		}
		
	}