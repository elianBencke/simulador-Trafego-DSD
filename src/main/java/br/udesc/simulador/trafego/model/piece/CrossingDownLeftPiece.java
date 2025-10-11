package br.udesc.simulador.trafego.model.piece;

import br.udesc.simulador.trafego.model.GlobalConstants;
import br.udesc.simulador.trafego.util.ImageUtils;

public class CrossingDownLeftPiece extends PieceModel{

    public CrossingDownLeftPiece(int type) {
        super(type);
    }

    @Override
    public String getPathImageIcon() {
        return ImageUtils.createImagePath("/road/cruzamento-down-left");
    }

    @Override
    public String getPathImageCar() {
        String path = "carDown";
        if(this.getDirection() == GlobalConstants.LEFT) {
            path = "carLeft";
        }
        return ImageUtils.createImagePath("/car/" + path);
    }

}