package br.udesc.simulador.trafego.util;

import java.awt.Image;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;


public class ResizedImageIconFactory {

    private static class ResizedImageIcon {

        private ImageIcon imageIcon;
        private String imagePath;
        private int width;
        private int height;

        public ResizedImageIcon(String imagePath, int width, int height) {
            initialize(imagePath, width, height);
        }

        private void initialize(String imagePath, int width, int height) {
            this.imagePath = imagePath;
            this.width = width;
            this.height = height;
        }

        public ImageIcon getImageIcon() {
            return imageIcon;
        }

        public void setImageIcon(ImageIcon imageIcon) {
            this.imageIcon = imageIcon;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + height;
            result = prime * result + ((imagePath == null) ? 0 : imagePath.hashCode());
            result = prime * result + width;
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            ResizedImageIcon other = (ResizedImageIcon) obj;
            if (height != other.height)
                return false;
            if (imagePath == null) {
                if (other.imagePath != null)
                    return false;
            } else if (!imagePath.equals(other.imagePath))
                return false;
            if (width != other.width)
                return false;
            return true;
        }

    }

    private static List<ResizedImageIcon> createdResizedIcons = new ArrayList<>();

    public static ImageIcon create(String imagePath, int width, int height) {
        ResizedImageIcon resized = new ResizedImageIcon(imagePath, width, height);
        int resizedIndex = createdResizedIcons.indexOf(resized);
        if(resizedIndex >= 0) {
            return createdResizedIcons.get(resizedIndex).getImageIcon();
        } else {
            ImageIcon imageIcon = createResizedIcon(imagePath, width, height);
            resized.setImageIcon(imageIcon);
            createdResizedIcons.add(resized);
            return imageIcon;
        }
    }

    private static ImageIcon createResizedIcon(String imagePath, int width, int height) {
        ImageIcon imageIcon = ImageIconFactory.create(imagePath);
        if(imageIcon != null && width > 0 && height > 0) {
            Image imageResized = imageIcon.getImage().getScaledInstance(width, height,  Image.SCALE_SMOOTH);
            ImageIcon imageIconResized = new ImageIcon(imageResized);
            return imageIconResized;
        } else {
            return null;
        }
    }

}