package br.udesc.simulador.trafego.ui.component;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import br.udesc.simulador.trafego.model.GlobalConstants;
import br.udesc.simulador.trafego.model.piece.PieceModel;

public class PieceIconRenderer implements Icon {
    private PieceModel piece;
    private int iconWidth;
    private int iconHeight;
    private ImageIcon pieceIcon;

    public PieceIconRenderer(PieceModel piece) {
        this.piece = piece;
        this.pieceIcon = this.piece.getImage(40);
        this.iconWidth = this.pieceIcon.getIconWidth();
        this.iconHeight = this.pieceIcon.getIconHeight();
    }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        ImageIcon pieceImg = this.pieceIcon;
        Image newImagePiece = this.piece.getImage(4).getImage();
        g.drawImage(newImagePiece, 0, 0, GlobalConstants.GRID_COLUMN_WIDTH, GlobalConstants.GRID_HEIGHT, pieceImg.getImageObserver());
        if(piece.hasCar()) {
            this.paintCar(g);
        }
    }

    private void paintCar(Graphics g) {
        ImageIcon car = this.piece.getCarIcon(iconHeight);
        this.drawImage(g, car);
    }

    private void drawImage(Graphics g, ImageIcon car) {
        int x = 0;
        int y = 0;
        Image newImageCar = car.getImage();
        g.drawImage(newImageCar, x, y, (car.getIconWidth()-10), (car.getIconHeight()-10), car.getImageObserver());
    }

    @Override
    public int getIconWidth() {
        return this.iconWidth;
    }

    @Override
    public int getIconHeight() {
        return this.iconHeight;
    }
}