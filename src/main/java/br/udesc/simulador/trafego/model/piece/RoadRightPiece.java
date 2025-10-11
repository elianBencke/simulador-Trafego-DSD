package br.udesc.simulador.trafego.model.piece;

import br.udesc.simulador.trafego.util.ImageUtils;

public class RoadRightPiece extends PieceModel {

    public RoadRightPiece(int type) {
        super(type);
    }

    @Override
    public String getPathImageIcon() {
        return ImageUtils.createImagePath("/road/right");
    }

    @Override
    public String getPathImageCar() {
        return ImageUtils.createImagePath("/car/carRight");
    }

}