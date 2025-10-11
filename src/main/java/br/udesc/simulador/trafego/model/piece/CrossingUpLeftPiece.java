package br.udesc.simulador.trafego.model.piece;

import br.udesc.simulador.trafego.model.GlobalConstants;
import br.udesc.simulador.trafego.util.ImageUtils;

public class CrossingUpLeftPiece extends PieceModel{

    public CrossingUpLeftPiece(int type) {
        super(type);
    }

    @Override
    public String getPathImageIcon() {
        return ImageUtils.createImagePath("/road/cruzamento-left-up");
    }

    @Override
    public String getPathImageCar() {
        String path = "carLeft";
        if(this.getDirection() == GlobalConstants.UP) {
            path = "carUp";
        }
        return ImageUtils.createImagePath("/car/" + path);
    }

}