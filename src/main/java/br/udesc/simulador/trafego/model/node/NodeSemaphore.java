package br.udesc.simulador.trafego.model.node;

import br.udesc.simulador.trafego.model.observer.ObserverNode;
import br.udesc.simulador.trafego.model.thread.Car;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.Random;

public class NodeSemaphore extends AbstractNode{

    private Semaphore nodeSemaphore = new Semaphore(1);

    public NodeSemaphore(int x, int y, int type, ObserverNode observer) {
        super(x, y, type, observer, false);
    }

    @Override
    public synchronized void moveCar(Car car) throws InterruptedException {
        AbstractNode nextNode = null;
        AbstractNode currentNode = car.getCurrentNode();
        AbstractNode firstNode = currentNode;
        List<AbstractNode> crossingNodes = new ArrayList<>();

        try{
            nextNode = getNextNode(car);
            if(nextNode == null) {
                car.setInterrupted(true);
                getObserver().notifyEndCar(getRow(), getColumn(), car);
                this.release();
            } else if (!nextNode.isCrossing()) {
                moveOne(car, nextNode);
            } else {
                boolean foundDestination = false;
                boolean isTraversalOK = true;

                if (nextNode.tryNext()) {
                    crossingNodes.add(nextNode);
                    currentNode = nextNode;
                    while (!foundDestination) {
                        nextNode = getNextNodeSimple(currentNode);
                        if (nextNode.isCrossing()){
                            if (nextNode.tryNext()) {
                                crossingNodes.add(nextNode);
                                currentNode = nextNode;
                            }else {
                                isTraversalOK = false;
                            }
                        } else {
                            if (nextNode.tryNext()) {
                                crossingNodes.add(nextNode);
                            }else {
                                isTraversalOK = false;
                            }
                            foundDestination = true;
                        }
                    }
                } else {
                    isTraversalOK = false;
                }

                if (isTraversalOK) {
                    processCrossingMove(car, firstNode, crossingNodes);
                } else {
                    for (AbstractNode nodeToRelease : crossingNodes) {
                        nodeToRelease.release();
                    }
                }
            }

        } catch (InterruptedException e) {
            this.release();
            throw new InterruptedException();
        }
    }

    private void processCrossingMove(Car car, AbstractNode firstNode, List<AbstractNode> crossingNodes) throws InterruptedException {
        for (AbstractNode node : crossingNodes) {
            car.setCurrentNode(node);
            firstNode.getObserver().notifyMoveCar(firstNode.getRow(), firstNode.getColumn(), node.getRow(), node.getColumn());
            firstNode.release();
            firstNode = node;
            car.sleepThread();
        }
    }

    private void moveOne(Car car, AbstractNode nextNode) throws InterruptedException {
        if (nextNode.tryNext()) {
            car.setCurrentNode(nextNode);
            this.getObserver().notifyMoveCar(this.getRow(), this.getColumn(), nextNode.getRow(), nextNode.getColumn());
            this.release();
            car.sleepThread();
        }
    }

    @Override
    public AbstractNode getNextNode(Car car) {
        AbstractNode currentNode = car.getCurrentNode();
        AbstractNode nextNode = null;

        AbstractNode[] directions = {
                currentNode.getNextNodeLeft(),
                currentNode.getNextNodeDown(),
                currentNode.getNextNodeRight(),
                currentNode.getNextNodeUp()
        };

        for (AbstractNode direction : directions) {
            if (direction != null) {
                nextNode = direction;
                break;
            }
        }
        return nextNode;
    }

    public AbstractNode getNextNodeSimple(AbstractNode initialNode) {
        AbstractNode currentNode = initialNode;
        AbstractNode nextNode = null;

        AbstractNode[] directions = {
                currentNode.getNextNodeLeft(),
                currentNode.getNextNodeDown(),
                currentNode.getNextNodeRight(),
                currentNode.getNextNodeUp()
        };

        Random random = new Random();

        while (nextNode == null) {
            int randomIndex = random.nextInt(directions.length);
            nextNode = directions[randomIndex];
        }

        return nextNode;
    }

    @Override
    public boolean tryNext() throws InterruptedException {
        return nodeSemaphore.tryAcquire(1000, TimeUnit.MILLISECONDS);
    }

    @Override
    public void block() throws InterruptedException {
        nodeSemaphore.acquire();
    }

    @Override
    public void release() {
        nodeSemaphore.release();
    }
}