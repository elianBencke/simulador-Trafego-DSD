package br.udesc.simulador.trafego.model.observer;

import br.udesc.simulador.trafego.model.thread.Car;

public interface ObserverNode {

    void notifyStartCar(int line, int column);
    void notifyMoveCar(int pastLine, int pastColumn, int newLine, int newColumn);
    void notifyEndCar(int line, int column, Car car);
}