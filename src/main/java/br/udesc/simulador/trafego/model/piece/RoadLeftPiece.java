package br.udesc.simulador.trafego.model.piece;

import br.udesc.simulador.trafego.util.ImageUtils;

public class RoadLeftPiece extends PieceModel{

    public RoadLeftPiece(int type) { // Refatorado 'tipo' para 'type'
        super(type);
    }

    @Override
    public String getPathImageIcon() {
        return ImageUtils.createImagePath("/road/left");
    }

    @Override
    public String getPathImageCar() {
        return ImageUtils.createImagePath("/car/carLeft");
    }

}