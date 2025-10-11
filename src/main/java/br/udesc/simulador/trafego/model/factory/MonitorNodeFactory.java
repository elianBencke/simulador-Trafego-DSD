package br.udesc.simulador.trafego.model.factory;

import br.udesc.simulador.trafego.model.node.AbstractNode;
import br.udesc.simulador.trafego.model.node.NodeCrossMonitor;
import br.udesc.simulador.trafego.model.node.NodeMonitor;
import br.udesc.simulador.trafego.model.observer.ObserverNode;

public class MonitorNodeFactory extends NodeFactory{

    @Override
    public AbstractNode createNode(int row, int column, int type, ObserverNode observer) {
        return new NodeMonitor(row, column, type, observer);
    }

    @Override
    public AbstractNode createCrossNode(int row, int column, int type, ObserverNode observer) {
        return new NodeCrossMonitor(row, column, type, observer);
    }
}