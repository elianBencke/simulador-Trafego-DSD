package br.udesc.simulador.trafego.model.piece;

import br.udesc.simulador.trafego.util.ImageUtils;

public class RoadUpPiece extends PieceModel {

    public RoadUpPiece(int type) {
        super(type);
    }

    @Override
    public String getPathImageIcon() {
        return ImageUtils.createImagePath("/road/up");
    }

    @Override
    public String getPathImageCar() {
        return ImageUtils.createImagePath("/car/carUp");
    }
}