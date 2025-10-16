package br.udesc.simulador.trafego.model.node;

import br.udesc.simulador.trafego.model.GlobalConstants;
import br.udesc.simulador.trafego.model.observer.ObserverNode;
import br.udesc.simulador.trafego.model.piece.PieceModel;
import br.udesc.simulador.trafego.model.singleton.MeshRepository;
import br.udesc.simulador.trafego.model.thread.Car;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class NodeSemaphore extends AbstractNode{

    private Semaphore nodeSemaphore = new Semaphore(1);

    public NodeSemaphore(int x, int y, int type, ObserverNode observer) {
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

                int chosenDirection = chooseCrossingExit(nextNode);
                car.setDirection(chosenDirection);

                List<AbstractNode> crossingNodes = getCrossingRoute(nextNode, car);

                boolean isTraversalOK = tryAcquireAllLocks(crossingNodes);

                if (isTraversalOK) {
                    processCrossingMove(car, this, crossingNodes);
                } else {
                }
            }

        } catch (InterruptedException e) {
            this.release();
            throw new InterruptedException();
        }
    }

    public List<AbstractNode> getCrossingRoute(AbstractNode initialNode, Car car) {

        List<AbstractNode> route = new ArrayList<>();
        int chosenDirection = car.getDirection();

        route.add(initialNode);

        AbstractNode nextNode = null;
        switch (chosenDirection) {
            case GlobalConstants.UP: nextNode = initialNode.getNextNodeUp(); break;
            case GlobalConstants.DOWN: nextNode = initialNode.getNextNodeDown(); break;
            case GlobalConstants.LEFT: nextNode = initialNode.getNextNodeLeft(); break;
            case GlobalConstants.RIGHT: nextNode = initialNode.getNextNodeRight(); break;
        }

        if (nextNode != null) {
            route.add(nextNode);
        } else {
        }

        String routeDetail = route.stream()
                .map(n -> "(" + n.getRow() + "," + n.getColumn() + ")")
                .collect(Collectors.joining(" -> "));
        return route;
    }

    @Override
    public List<AbstractNode> getCrossingRoute(AbstractNode initialNode) {
        return new ArrayList<>();
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

    private int chooseCrossingExit(AbstractNode initialCrossingNode) {
        List<Integer> possibleExits = new ArrayList<>();

        if (initialCrossingNode.canMoveUp() && !initialCrossingNode.getNextNodeUp().isCrossing())
            possibleExits.add(GlobalConstants.UP);
        if (initialCrossingNode.canMoveDown() && !initialCrossingNode.getNextNodeDown().isCrossing())
            possibleExits.add(GlobalConstants.DOWN);
        if (initialCrossingNode.canMoveRight() && !initialCrossingNode.getNextNodeRight().isCrossing())
            possibleExits.add(GlobalConstants.RIGHT);
        if (initialCrossingNode.canMoveLeft() && !initialCrossingNode.getNextNodeLeft().isCrossing())
            possibleExits.add(GlobalConstants.LEFT);

        if (possibleExits.isEmpty()) {
            return GlobalConstants.UP;
        }

        return possibleExits.get(new Random().nextInt(possibleExits.size()));
    }

    private String directionToString(int direction) {
        switch (direction) {
            case GlobalConstants.UP: return "UP";
            case GlobalConstants.DOWN: return "DOWN";
            case GlobalConstants.LEFT: return "LEFT";
            case GlobalConstants.RIGHT: return "RIGHT";
            default: return "UNKNOWN";
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
        } else {
            this.release();
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

    public AbstractNode getNextNodeSimple(AbstractNode initialNode) {

        List<AbstractNode> possibleNextNodes = new ArrayList<>();

        if (initialNode.canMoveLeft()) possibleNextNodes.add(initialNode.getNextNodeLeft());
        if (initialNode.canMoveDown()) possibleNextNodes.add(initialNode.getNextNodeDown());
        if (initialNode.canMoveRight()) possibleNextNodes.add(initialNode.getNextNodeRight());
        if (initialNode.canMoveUp()) possibleNextNodes.add(initialNode.getNextNodeUp());

        if (possibleNextNodes.isEmpty()) {
            return null;
        }

        Random random = new Random();
        int randomIndex = random.nextInt(possibleNextNodes.size());

        return possibleNextNodes.get(randomIndex);
    }

    @Override
    public boolean tryNext() throws InterruptedException {
        nodeSemaphore.acquire();
        return true;
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