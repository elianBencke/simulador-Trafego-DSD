package br.udesc.simulador.trafego.util;

public class ImageUtils {

    public static String getImagePath() {
        return "./src/main/java/resources/assets/";
    }

    public static String createImagePath(String image) {
        return createImagePath(image, "png");
    }

    public static String createImagePath(String image, String extension) {
        return getImagePath()+"/"+image+"."+extension;
    }
}