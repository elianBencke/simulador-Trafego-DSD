package br.udesc.simulador.trafego.model.thread;

import java.util.Random;

import br.udesc.simulador.trafego.model.node.AbstractNode;

public class Car extends Thread {
    private boolean isFirstMove;
    private boolean isInterrupted;
    private int sleepTime;
    private AbstractNode currentNode;

    public Car(AbstractNode currentNode) {
        this.isFirstMove = true;
        this.isInterrupted = false;
        this.sleepTime = new Random().nextInt(2001 - 500) + 500;
        this.currentNode = currentNode;
    }

    @Override
    public void run() {
        try {
            while (!isInterrupted && !Thread.currentThread().isInterrupted()) {
                if (isFirstMove) {
                    if (currentNode.tryNext()) {
                        currentNode.getObserver().notifyStartCar(currentNode.getRow(), currentNode.getColumn());
                        markFirstMoveDone();
                        sleepThread();
                    }
                } else {
                    currentNode.moveCar(this);
                }
            }

            currentNode.getObserver().notifyEndCar(currentNode.getRow(), currentNode.getColumn(), this);
            Thread.currentThread().interrupt();
        } catch (InterruptedException e) {
            currentNode.getObserver().notifyEndCar(currentNode.getRow(), currentNode.getColumn(), this);
            Thread.currentThread().interrupt();
        }
    }

    public void markAsInterrupted() {
        isInterrupted = true;
    }

    public void markFirstMoveDone() {
        isFirstMove = false;
    }

    public void sleepThread() throws InterruptedException {
        Thread.currentThread().sleep(sleepTime);
    }

    public boolean isFirstMove() {
        return isFirstMove;
    }

    public boolean isInterrupted() {
        return isInterrupted;
    }

    public void setFirstMove(boolean isFirstMove) {
        this.isFirstMove = isFirstMove;
    }

    public void setInterrupted(boolean isInterrupted) {
        this.isInterrupted = isInterrupted;
    }

    public int getSleepTime() {
        return sleepTime;
    }

    public void setSleepTime(int sleepTime) {
        this.sleepTime = sleepTime;
    }

    public AbstractNode getCurrentNode() {
        return currentNode;
    }

    public void setCurrentNode(AbstractNode currentNode) {
        this.currentNode = currentNode;
    }
}