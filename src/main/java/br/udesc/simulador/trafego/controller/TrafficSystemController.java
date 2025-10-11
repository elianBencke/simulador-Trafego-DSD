package br.udesc.simulador.trafego.controller;

import java.util.ArrayList;
import java.util.List;

import br.udesc.simulador.trafego.model.GlobalConstants;
import br.udesc.simulador.trafego.model.node.AbstractNode;
import br.udesc.simulador.trafego.model.observer.ObserverNode;
import br.udesc.simulador.trafego.model.piece.PieceModel;
import br.udesc.simulador.trafego.model.thread.Car;
import br.udesc.simulador.trafego.model.thread.CarGenerator;
import br.udesc.simulador.trafego.model.singleton.MeshRepository;


public class TrafficSystemController implements AbstractTableDataProvider, ObserverNode{
    private ObserverNode observerNode;
    private int[][] roadMesh;
    private boolean interruptClick;
    private AbstractNode[][] nodeMesh;
    public PieceModel[][] pieces;
    private List<Car> cars;
    private int maxThreads = 0;
    private int insertionInterval = 1000;

    public TrafficSystemController() {
        super();
        interruptClick = false;
        this.roadMesh = MeshRepository.getInstance().getRoadMesh();
        pieces = MeshRepository.getInstance().getPieces();
    }

    @Override
    public int getRowCount() {
        return roadMesh.length;
    }

    @Override
    public int getColumnCount() {
        return roadMesh[0].length;
    }

    @Override
    public PieceModel getValueAt(int rowIndex, int columnIndex) {
        return pieces[rowIndex][columnIndex];
    }

    public boolean hasElementAt(int row, int column, int[][] state) {
        if (state[row][column] == 0) {
            return false;
        } else {
            return true;
        }
    }

    public List<Car> getCars(){
        return cars;
    }

    public void startSimulation(String maxThreadsStr, String intervalStr) {
        interruptClick = false;
        nodeMesh = MeshRepository.getInstance().createNodeMesh(this);
        mapEntranceNodes();
        cars = new ArrayList<>();

        if (intervalStr.matches("^\\d+$")) {
            this.insertionInterval = Integer.parseInt(intervalStr);
        } else {
            this.insertionInterval = 1000; // Default
        }

        if(maxThreadsStr.matches("^\\d+$")){
            int numThreads = Integer.parseInt(maxThreadsStr);
            this.maxThreads = numThreads;

            CarGenerator generator = new CarGenerator(numThreads, cars, numThreads, this.insertionInterval);
            generator.start();
        }
    }

    public void generateCar(){
        CarGenerator generator = new CarGenerator(1, cars, maxThreads, this.insertionInterval);
        generator.start();
    }

    public void stopAllCars(){
        interruptClick = true;
        for (Car car: cars) {
            car.markAsInterrupted();
        }
        cars.clear();
    }

    private void mapEntranceNodes() {
        for (int column = 0; column < roadMesh[0].length; column++) {
            if (roadMesh[0][column] == GlobalConstants.DOWN) {
                MeshRepository.getInstance().addEntranceNode(nodeMesh[0][column]);
            }
            if (roadMesh[roadMesh.length - 1][column] == GlobalConstants.UP) {
                MeshRepository.getInstance().addEntranceNode(nodeMesh[roadMesh.length - 1][column]);
            }
        }
        for (int row = 0; row < roadMesh.length - 1; row++) {
            if (roadMesh[row][0] == GlobalConstants.RIGHT) {
                MeshRepository.getInstance().addEntranceNode(nodeMesh[row][0]);
            }
            if (roadMesh[row][roadMesh[0].length - 1] == GlobalConstants.LEFT) {
                MeshRepository.getInstance().addEntranceNode(nodeMesh[row][roadMesh[0].length - 1]);
            }
        }
    }

    public void addObserver(ObserverNode observer) {
        observerNode = observer;
    }

    @Override
    public void notifyStartCar(int line, int column) {
        observerNode.notifyStartCar(line, column);
    }

    @Override
    public void notifyMoveCar(int pastLine, int pastColumn, int newLine, int newColumn) {
        observerNode.notifyMoveCar(pastLine, pastColumn, newLine, newColumn);
    }

    @Override
    public void notifyEndCar(int line, int column, Car car) {
        observerNode.notifyEndCar(line, column, car);
        if (!interruptClick) {
            cars.remove(car);
            generateCar();
        }
    }
}