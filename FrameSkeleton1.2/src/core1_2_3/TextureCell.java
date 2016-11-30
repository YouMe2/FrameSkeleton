package core1_2_3;

import java.awt.Image;

public class TextureCell implements CellType{

		private Image img;
		private boolean trans;
		private final String NAME;
		
		public TextureCell(Image img, boolean transparency, String name) {
			this.img = img;
			this.NAME = name;
			this.trans = transparency;
		}
		
		@Override
		public Image getTexture() {
			
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