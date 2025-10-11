package br.udesc.simulador.trafego.model.piece;

import br.udesc.simulador.trafego.model.GlobalConstants;
import br.udesc.simulador.trafego.util.ImageUtils;

public class CrossingRightDownPiece extends PieceModel{

    public CrossingRightDownPiece(int type) {
        super(type);
    }

    @Override
    public String getPathImageIcon() {
        return ImageUtils.createImagePath("/road/cruzamento-right-down");
    }

    @Override
    public String getPathImageCar() {
        String path = "carRight";
        if(this.getDirection() == GlobalConstants.DOWN) {
            path = "carDown";
        }
        return ImageUtils.createImagePath("/car/" + path);
    }

}