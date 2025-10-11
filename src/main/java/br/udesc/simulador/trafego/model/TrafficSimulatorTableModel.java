package br.udesc.simulador.trafego.model;

import javax.swing.table.AbstractTableModel;
import br.udesc.simulador.trafego.controller.AbstractTableDataProvider;

public class TrafficSimulatorTableModel extends AbstractTableModel{

    private AbstractTableDataProvider dataController;
    private static final long serialVersionUID = 1L;

    public TrafficSimulatorTableModel(AbstractTableDataProvider dataController) {
        super();
        this.dataController = dataController;
    }

    @Override
    public int getRowCount() {
        return dataController.getRowCount();
    }

    @Override
    public int getColumnCount() {
        return dataController.getColumnCount();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return dataController.getValueAt(rowIndex, columnIndex);
    }

}