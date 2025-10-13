package br.udesc.simulador.trafego.model.node;

import br.udesc.simulador.trafego.model.GlobalConstants;
import br.udesc.simulador.trafego.model.observer.ObserverNode;
import br.udesc.simulador.trafego.model.piece.PieceModel;
import br.udesc.simulador.trafego.model.singleton.MeshRepository;
import br.udesc.simulador.trafego.model.thread.Car;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class NodeMonitor extends AbstractNode {

    private ReentrantLock nodeLock = new ReentrantLock();

    public NodeMonitor(int x, int y, int type, ObserverNode observer) {
        super(x, y, type, observer, false);
    }

    @Override
    public void moveCar(Car car) throws InterruptedException {
        AbstractNode nextNode = getNextNode(car);

        try{
            if(nextNode == null) {
                car.setInterrupted(true);
                getObserver().notifyEndCar(getRow(), getColumn(), car);
                this.release();
            } else if (!nextNode.isCrossing()) {
                moveOne(car, nextNode);
            } else {
                List<AbstractNode> crossingNodes = getCrossingRoute(nextNode);

                boolean isTraversalOK = tryAcquireAllLocks(crossingNodes);

                if (isTraversalOK) {
                    processCrossingMove(car, this, crossingNodes);
                }
            }

        } catch (InterruptedException e) {
            this.release();
            throw new InterruptedException();
        }
    }

    private boolean tryAcquireAllLocks(List<AbstractNode> crossingNodes) throws InterruptedException {
        for (AbstractNode node : crossingNodes) {
            if (!node.tryNext()) {
                for (AbstractNode acquiredNode : crossingNodes) {
                    if (acquiredNode == node) break;
                    acquiredNode.release();
                }
                return false;
            }
        }
        return true;
    }

    private void processCrossingMove(Car car, AbstractNode firstNode, List<AbstractNode> crossingNodes) throws InterruptedException {
        AbstractNode currentNode = firstNode;

        for (AbstractNode nextNode : crossingNodes) {

            int direction = determineDirection(currentNode, nextNode);
            car.setCurrentNode(nextNode);
            car.setDirection(direction);

            updatePieceDirection(nextNode.getRow(), nextNode.getColumn(), direction);

            currentNode.getObserver().notifyMoveCar(currentNode.getRow(), currentNode.getColumn(), nextNode.getRow(), nextNode.getColumn());
            currentNode.release();

            currentNode = nextNode;
            car.sleepThread();
        }

        if (currentNode != null && !currentNode.isCrossing()) {
            currentNode.release();
        }
    }

    private void moveOne(Car car, AbstractNode nextNode) throws InterruptedException {
        if (nextNode.tryNext()) {

            int direction = determineDirection(this, nextNode);
            car.setCurrentNode(nextNode);
            car.setDirection(direction);

            updatePieceDirection(nextNode.getRow(), nextNode.getColumn(), direction);

            this.getObserver().notifyMoveCar(this.getRow(), this.getColumn(), nextNode.getRow(), nextNode.getColumn());
            this.release();
            car.sleepThread();
        }
    }

    private int determineDirection(AbstractNode currentNode, AbstractNode nextNode) {
        if (nextNode.getRow() < currentNode.getRow()) return GlobalConstants.UP;
        if (nextNode.getRow() > currentNode.getRow()) return GlobalConstants.DOWN;
        if (nextNode.getColumn() > currentNode.getColumn()) return GlobalConstants.RIGHT;
        if (nextNode.getColumn() < currentNode.getColumn()) return GlobalConstants.LEFT;
        return 0;
    }

    private void updatePieceDirection(int row, int column, int direction) {
        PieceModel piece = MeshRepository.getInstance().getPieces()[row][column];
        if (piece != null) {
            piece.setDirection(direction);
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

    @Override
    public List<AbstractNode> getCrossingRoute(AbstractNode initialNode) {
        List<AbstractNode> route = new ArrayList<>();
        AbstractNode currentNode = initialNode;
        boolean foundDestination = false;

        while (!foundDestination) {
            route.add(currentNode);

            AbstractNode nextNode = getNextNodeSimple(currentNode);

            if (nextNode == null) {
                break;
            } else if (nextNode.isCrossing()) {
                currentNode = nextNode;
            } else {
                route.add(nextNode);
                foundDestination = true;
            }
        }
        return route;
    }

    public AbstractNode getNextNodeSimple(AbstractNode initialNode) {
        AbstractNode currentNode = initialNode;
        List<AbstractNode> possibleNextNodes = new ArrayList<>();

        if (currentNode.canMoveLeft()) possibleNextNodes.add(currentNode.getNextNodeLeft());
        if (currentNode.canMoveDown()) possibleNextNodes.add(currentNode.getNextNodeDown());
        if (currentNode.canMoveRight()) possibleNextNodes.add(currentNode.getNextNodeRight());
        if (currentNode.canMoveUp()) possibleNextNodes.add(currentNode.getNextNodeUp());

        if (possibleNextNodes.isEmpty()) {
            return null;
        }

        Random random = new Random();
        int randomIndex = random.nextInt(possibleNextNodes.size());
        return possibleNextNodes.get(randomIndex);
    }

    @Override
    public boolean tryNext() throws InterruptedException {
        nodeLock.lock();
        return true;
    }

    @Override
    public void block() throws InterruptedException {
        nodeLock.lock();
    }

    @Override
    public void release() {
        if (nodeLock.isHeldByCurrentThread()) {
            nodeLock.unlock();
        }
    }
}