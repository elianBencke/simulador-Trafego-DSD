package br.udesc.simulador.trafego.controller;

public interface AbstractTableDataProvider {
    public int getColumnCount();
    public int getRowCount();
    public Object getValueAt(int rowIndex, int columnIndex);
}
