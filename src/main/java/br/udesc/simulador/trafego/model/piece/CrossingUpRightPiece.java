package br.udesc.simulador.trafego.model.piece;

import br.udesc.simulador.trafego.model.GlobalConstants;
import br.udesc.simulador.trafego.util.ImageUtils;

public class CrossingUpRightPiece extends PieceModel{

    public CrossingUpRightPiece(int type) {
        super(type);
    }

    @Override
    public String getPathImageIcon() {
        return ImageUtils.createImagePath("/road/cruzamento-up-right");
    }

    @Override
    public String getPathImageCar() {
        String path = "carUp";
        if(this.getDirection() == GlobalConstants.RIGHT) {
            path = "carRight";
        }
        return ImageUtils.createImagePath("/car/" + path);
    }

}