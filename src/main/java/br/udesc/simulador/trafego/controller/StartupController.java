package br.udesc.simulador.trafego.controller;

import java.io.File;

import br.udesc.simulador.trafego.ui.observer.ObserverInitialView;
import br.udesc.simulador.trafego.model.singleton.MeshRepository;
import br.udesc.simulador.trafego.util.ReadFileMesh;

public class StartupController {
    private ObserverInitialView observer;
    private int[][] roadMesh;

    public StartupController(ObserverInitialView observer) {
        super();
        this.observer = observer;
    }

    public void updateRoadMesh(File file) {
        try {
            roadMesh = ReadFileMesh.generateRoadMesh(file);
            MeshRepository.getInstance().setRoadMesh(roadMesh);
            MeshRepository.getInstance().initializePieces();
            observer.activateInitialButton();
        } catch (Exception e) {
            observer.notifyErrorFile();
        }
    }
    public void navigateNextView(String maxThreads, String insertionInterval) {
        MeshRepository.getInstance().setRoadMesh(roadMesh);
        observer.navigateNextView(maxThreads, insertionInterval);
    }
}