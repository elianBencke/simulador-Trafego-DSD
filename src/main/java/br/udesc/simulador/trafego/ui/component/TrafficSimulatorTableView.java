package br.udesc.simulador.trafego.ui.component;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.BorderFactory;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import br.udesc.simulador.trafego.controller.AbstractTableDataProvider;
import br.udesc.simulador.trafego.model.GlobalConstants;
import br.udesc.simulador.trafego.model.TrafficSimulatorTableModel;

public class TrafficSimulatorTableView extends JTable{
    private static final long serialVersionUID = 1L;
    private AbstractTableDataProvider trafficController;
    private TrafficSimulatorTableModel model;
    private TableCellRenderer cellRenderer;

    public TrafficSimulatorTableView(AbstractTableDataProvider controller) {
        this.trafficController = controller;
        initialize();
    }

    private void initialize(){
        this.defineProperties();
        this.initializeComponents();
        this.addComponents();
    }

    private void defineProperties() {
        this.setOpaque(false);
        this.setBackground(new Color (220, 220, 220));
        Color gridColor = new Color(42, 94, 157);
        setRowHeight(GlobalConstants.GRID_HEIGHT);
        this.setBorder(BorderFactory.createLineBorder(gridColor));
        this.setGridColor(gridColor);
    }

    private void initializeComponents(){
        this.model = new TrafficSimulatorTableModel(this.trafficController);
        this.cellRenderer = new TrafficPieceCellRenderer(GlobalConstants.GRID_COLUMN_WIDTH);
    }

    private void addComponents(){
        this.setModel(this.model);
        this.setDefaultRenderer(Object.class, this.cellRenderer);
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(getBackground());
        g.fillRect(0, 0, getWidth(), getHeight());
        super.paintComponent(g);
    }

    public void refresh(){
        this.initialize();
    }

}