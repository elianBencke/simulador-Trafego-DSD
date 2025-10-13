package br.udesc.simulador.trafego.model.node;

import br.udesc.simulador.trafego.model.observer.ObserverNode;
import br.udesc.simulador.trafego.model.thread.Car;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class NodeCrossSemaphore extends AbstractNode {

    private Semaphore crossingSemaphore = new Semaphore(1);

    public NodeCrossSemaphore(int x, int y, int type, ObserverNode observer) {
        super(x, y, type, observer, true);
    }

    @Override
    public void moveCar(Car car) throws InterruptedException {
        throw new InterruptedException();
    }

    @Override
    public AbstractNode getNextNode(Car car) {
        return null;
    }

    @Override
    public List<AbstractNode> getCrossingRoute(AbstractNode initialNode) {
        List<AbstractNode> route = new ArrayList<>();
        route.add(initialNode);
        return route;
    }

    @Override
    public boolean tryNext() throws InterruptedException {
        return crossingSemaphore.tryAcquire(new Random().nextInt(2001 - 500) + 500, TimeUnit.MILLISECONDS);
    }

    @Override
    public void block() throws InterruptedException {
        crossingSemaphore.acquire();
    }

    @Override
    public void release() {
        crossingSemaphore.release();
    }

}