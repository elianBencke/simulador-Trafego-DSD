package br.udesc.simulador.trafego.model.node;

import br.udesc.simulador.trafego.model.observer.ObserverNode;
import br.udesc.simulador.trafego.model.thread.Car;

public abstract class AbstractNode {
    private AbstractNode nextNodeUp;
    private AbstractNode nextNodeDown;
    private AbstractNode nextNodeRight;
    private AbstractNode nextNodeLeft;
    private int row;
    private int column;
    private int type;
    private boolean isCrossing;
    private ObserverNode observer;

    public AbstractNode(int x, int y, int type, ObserverNode observer, boolean isCrossing) {
        this.nextNodeUp = null;
        this.nextNodeDown = null;
        this.nextNodeRight = null;
        this.nextNodeLeft = null;
        this.row = x;
        this.column = y;
        this.type = type;
        this.observer = observer;
        this.isCrossing = isCrossing;
    }

    public void setMoveUp(AbstractNode nextNodeUp) {
        this.nextNodeUp = nextNodeUp;
    }

    public void setMoveDown(AbstractNode nextNodeDown) {
        this.nextNodeDown = nextNodeDown;
    }

    public void setMoveRight(AbstractNode nextNodeRight) {
        this.nextNodeRight = nextNodeRight;
    }

    public void setMoveLeft(AbstractNode nextNodeLeft) {
        this.nextNodeLeft = nextNodeLeft;
    }

    public AbstractNode getNextNodeUp() {
        return nextNodeUp;
    }

    public AbstractNode getNextNodeDown() {
        return nextNodeDown;
    }

    public AbstractNode getNextNodeRight() {
        return nextNodeRight;
    }

    public AbstractNode getNextNodeLeft() {
        return nextNodeLeft;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public int getType() {
        return type;
    }

    public boolean isCrossing() {
        return isCrossing;
    }

    public ObserverNode getObserver() {
        return this.observer;
    }

    public boolean canMoveUp(){
        return this.nextNodeUp != null;
    }

    public boolean canMoveDown(){
        return this.nextNodeDown != null;
    }

    public boolean canMoveRight(){
        return this.nextNodeRight != null;
    }

    public boolean canMoveLeft(){
        return this.nextNodeLeft != null;
    }

    public abstract void moveCar(Car car) throws InterruptedException;
    public abstract AbstractNode getNextNode(Car car);
    public abstract boolean tryNext() throws InterruptedException;
    public abstract void block() throws InterruptedException;
    public abstract void release();
}