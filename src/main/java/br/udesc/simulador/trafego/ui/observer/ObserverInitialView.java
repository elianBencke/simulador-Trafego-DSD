package br.udesc.simulador.trafego.ui.observer;

public interface ObserverInitialView {

    void activateInitialButton();
    void updatePathText(String path);
    void notifyErrorFile();
    void navigateNextView();
}