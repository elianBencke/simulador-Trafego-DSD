package br.udesc.simulador.trafego.model.piece;

import br.udesc.simulador.trafego.util.ImageUtils;

public class RoadDownPiece extends PieceModel{

    public RoadDownPiece(int type) {
        super(type);
    }

    @Override
    public String getPathImageIcon() {
        return ImageUtils.createImagePath("/road/down");
    }

    @Override
    public String getPathImageCar() {
        return ImageUtils.createImagePath("/car/carDown");
    }

}