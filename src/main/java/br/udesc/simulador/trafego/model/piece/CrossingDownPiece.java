package br.udesc.simulador.trafego.model.piece;

import br.udesc.simulador.trafego.util.ImageUtils;

public class CrossingDownPiece extends PieceModel{

    public CrossingDownPiece(int type) {
        super(type);
    }

    @Override
    public String getPathImageIcon() {
        return ImageUtils.createImagePath("/road/cruzamento-down");
    }

    @Override
    public String getPathImageCar() {
        return ImageUtils.createImagePath("/car/carDown");
    }

}