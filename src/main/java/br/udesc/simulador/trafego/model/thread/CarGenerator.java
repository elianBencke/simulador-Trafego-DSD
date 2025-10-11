package br.udesc.simulador.trafego.model.thread;

import java.util.List;
import java.util.Random;

import br.udesc.simulador.trafego.model.node.AbstractNode;
import br.udesc.simulador.trafego.model.singleton.MeshRepository;

public class CarGenerator extends Thread{
    private List<Car> carList;
    private int quantity;
    private int maxThreads;
    private int insertionInterval;

    public CarGenerator(int quantity, List<Car> carList, int maxThreads, int insertionInterval) {
        this.quantity = quantity;
        this.carList = carList;
        this.maxThreads = maxThreads;
        this.insertionInterval = insertionInterval;
    }

    private void generateCar(){
        int position = new Random().nextInt(MeshRepository.getInstance().getEntranceNodes().size());
        AbstractNode chosenNode = MeshRepository.getInstance().getEntranceNodes().get(position);
        Car car = new Car(chosenNode);
        carList.add(car);
        car.start();
    }

    @Override
    public void run() {
        try {
            if(carList.size() < maxThreads) {
                for(int i= 0; i < quantity; i++) {
                    Thread.sleep(insertionInterval);
                    generateCar();
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            Thread.currentThread().interrupt();
        }
    }
}