package br.udesc.simulador.trafego.model.piece;

import br.udesc.simulador.trafego.util.ImageUtils;

public class CrossingLeftPiece extends PieceModel{

    public CrossingLeftPiece(int type) {
        super(type);
    }

    @Override
    public String getPathImageIcon() {
        return ImageUtils.createImagePath("/road/cruzamento-left");
    }

    @Override
    public String getPathImageCar() {
        return ImageUtils.createImagePath("/car/carLeft");
    }

}