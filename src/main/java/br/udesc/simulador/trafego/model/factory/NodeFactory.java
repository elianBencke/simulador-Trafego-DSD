package br.udesc.simulador.trafego.model.factory;

import br.udesc.simulador.trafego.model.node.AbstractNode;
import br.udesc.simulador.trafego.model.observer.ObserverNode;

public abstract class NodeFactory {
    public abstract AbstractNode createNode(int row, int column, int type, ObserverNode observer);
    public abstract AbstractNode createCrossNode(int row, int column, int type, ObserverNode observer);
}