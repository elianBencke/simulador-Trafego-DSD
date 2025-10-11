package br.udesc.simulador.trafego.controller;

import br.udesc.simulador.trafego.model.observer.ObserverNode;
import br.udesc.simulador.trafego.model.thread.Car;

public interface AbstractController {

    public void addObserver(ObserverNode observer);
    public void notifyStartCar(int line, int column);
    public void notifyMoveCar(int pastLine, int pastColumn, int newLine, int newColumn);
    public void notifyEndCar(int line, int column, Car car);
}