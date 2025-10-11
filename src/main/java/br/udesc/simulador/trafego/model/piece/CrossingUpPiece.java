package br.udesc.simulador.trafego.model.piece;

import br.udesc.simulador.trafego.util.ImageUtils;

public class CrossingUpPiece extends PieceModel{

    public CrossingUpPiece(int type) {
        super(type);
    }

    @Override
    public String getPathImageIcon() {
        return ImageUtils.createImagePath("/road/cruzamento-up");
    }

    @Override
    public String getPathImageCar() {
        return ImageUtils.createImagePath("/car/carUp");
    }

}