package br.udesc.simulador.trafego.controller;

import java.util.ArrayList;
import java.util.List;

import br.udesc.simulador.trafego.model.GlobalConstants;
import br.udesc.simulador.trafego.model.factory.NodeFactory;
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
    private boolean isInsertionActive = false;
    private NodeFactory factory;

    private CarGenerator initialCarGenerator = null;

    public TrafficSystemController() {
        super();
        interruptClick = false;
        this.factory = MeshRepository.getInstance().getFactory();
        this.roadMesh = MeshRepository.getInstance().getRoadMesh();
        this.pieces = MeshRepository.getInstance().getPieces();
        this.cars = new ArrayList<>();
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
    public Object getValueAt(int rowIndex, int columnIndex) {
        return pieces[rowIndex][columnIndex];
    }

    public List<Car> getCars() {
        return cars;
    }

    public boolean isInsertionActive() {
        return isInsertionActive;
    }

    public void startSimulation(String maxThreadsStr, String intervalStr) {
        if (this.isInsertionActive) {
            return;
        }

        interruptClick = false;

        this.nodeMesh = MeshRepository.getInstance().createAndLinkNodeMesh(this);
        mapEntranceNodes();

        if (intervalStr.matches("^\\d+$")) {
            this.insertionInterval = Integer.parseInt(intervalStr);
        } else {
            this.insertionInterval = 1000;
        }

        if(maxThreadsStr.matches("^\\d+$")){
            int numThreads = Integer.parseInt(maxThreadsStr);
            this.maxThreads = numThreads;

            this.isInsertionActive = true;

            initialCarGenerator = new CarGenerator(numThreads, cars, numThreads, this.insertionInterval);
            initialCarGenerator.start();
        }
    }

    public void generateCar(){
        CarGenerator generator = new CarGenerator(1, cars, maxThreads, this.insertionInterval);
        generator.start();
    }

    public synchronized void stopInsertion() {
        this.isInsertionActive = false;
        if (initialCarGenerator != null && initialCarGenerator.isAlive()) {
            initialCarGenerator.interrupt();
        }
    }

    public synchronized void stopAllCars(){
        interruptClick = true;
        this.isInsertionActive = false;

        if (initialCarGenerator != null && initialCarGenerator.isAlive()) {
            initialCarGenerator.interrupt();
        }

        List<Car> carsToStop = new ArrayList<>(cars);
        for (Car car: carsToStop) {
            /*
            if (car.getCurrentNode() != null) {
                car.getCurrentNode().getObserver().notifyEndCar(car.getCurrentNode().getRow(), car.getCurrentNode().getColumn(), car);
            }
            */
            car.markAsInterrupted();
        }
    }

    private void mapEntranceNodes() {
        MeshRepository.getInstance().getEntranceNodes().clear();
        int rows = roadMesh.length;
        int columns = roadMesh[0].length;

        for (int column = 0; column < columns; column++) {
            if (roadMesh[0][column] == GlobalConstants.DOWN) {
                MeshRepository.getInstance().addEntranceNode(nodeMesh[0][column]);
            }
            if (roadMesh[rows - 1][column] == GlobalConstants.UP) {
                MeshRepository.getInstance().addEntranceNode(nodeMesh[rows - 1][column]);
            }
        }

        for (int row = 0; row < rows; row++) {
            if (roadMesh[row][0] == GlobalConstants.RIGHT) {
                MeshRepository.getInstance().addEntranceNode(nodeMesh[row][0]);
            }
            if (roadMesh[row][columns - 1] == GlobalConstants.LEFT) {
                MeshRepository.getInstance().addEntranceNode(nodeMesh[row][columns - 1]);
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

        synchronized (cars) {
            cars.remove(car);

            if (!interruptClick) {
                if (isInsertionActive && cars.size() < maxThreads) {
                    generateCar();
                }
            }
        }
    }
}