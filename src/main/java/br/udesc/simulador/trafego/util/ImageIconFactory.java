package br.udesc.simulador.trafego.util;

import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;

public class ImageIconFactory {
     private static List<String> createdImagePaths = new ArrayList<>();
     private static List<ImageIcon> createdIcons = new ArrayList<>();

     public static ImageIcon create(String imagePath) {
        int imageIndex = createdImagePaths.indexOf(imagePath);
        if(imageIndex >= 0) {
            return createdIcons.get(imageIndex);
        } else {
            ImageIcon imageIcon = new ImageIcon(imagePath);
             createdImagePaths.add(imagePath);
             createdIcons.add(imageIcon);
             return imageIcon;
         }
     }
}