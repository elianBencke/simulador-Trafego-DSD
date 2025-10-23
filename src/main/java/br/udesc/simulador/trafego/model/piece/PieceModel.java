package br.udesc.simulador.trafego.model.piece;

import javax.swing.ImageIcon;

import br.udesc.simulador.trafego.controller.TrafficSystemController;
import br.udesc.simulador.trafego.util.ResizedImageIconFactory;

public abstract class PieceModel {
    private int type;
    private boolean hasCar;
    private volatile int direction;

    public PieceModel(int type) {
        this.type = type;
        this.hasCar = false;
        this.direction = 0;
    }

    public int getType() {
        return type;
    }

    public void setHasCar(boolean hasCar) {
        this.hasCar = hasCar;
    }

    public boolean hasCar() {
        return hasCar;
    }

    protected void setType(int type) {
        this.type = type;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public ImageIcon getImage(TrafficSystemController controller) {
        int sizeIcon = (int) (this.iconSize(controller.getRowCount()));
        String path = this.getPathImageIcon();
        ImageIcon icon = ResizedImageIconFactory.create((String) path, sizeIcon, sizeIcon);
        return icon;
    }

    public ImageIcon getImage(int size) {
        int sizeIcon = (int) (this.iconSize(size));
        String path = this.getPathImageIcon();
        ImageIcon icon = ResizedImageIconFactory.create((String) path, sizeIcon, sizeIcon);
        return icon;
    }

    public ImageIcon getCarIcon(int size){
        int sizeIcon = (int) (this.iconSize(size));
        String path = this.getPathImageCar();
        ImageIcon icon = ResizedImageIconFactory.create((String) path, sizeIcon, sizeIcon);
        return icon;
    }

    public abstract String getPathImageIcon();

    public abstract String getPathImageCar();


    private int iconSize(int lineCount) {
        final int MAX_SIZE = 550;
        return (int) (MAX_SIZE / lineCount);
    }
}