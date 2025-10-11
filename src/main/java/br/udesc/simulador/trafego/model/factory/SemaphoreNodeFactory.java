package br.udesc.simulador.trafego.model.factory;

import br.udesc.simulador.trafego.model.node.AbstractNode;
import br.udesc.simulador.trafego.model.node.NodeCrossSemaphore;
import br.udesc.simulador.trafego.model.node.NodeSemaphore;
import br.udesc.simulador.trafego.model.observer.ObserverNode;

public class SemaphoreNodeFactory extends NodeFactory{

    @Override
    public AbstractNode createNode(int row, int column, int type, ObserverNode observer) {
        return new NodeSemaphore(row, column, type, observer);
    }

    @Override
    public AbstractNode createCrossNode(int row, int column, int type, ObserverNode observer) {
        return new NodeCrossSemaphore(row, column, type, observer);
    }

}