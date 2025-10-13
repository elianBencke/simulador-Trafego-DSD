package br.udesc.simulador.trafego.ui.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.table.TableModel;

import br.udesc.simulador.trafego.ui.component.TrafficSimulatorTableView;
import br.udesc.simulador.trafego.controller.TrafficSystemController;
import br.udesc.simulador.trafego.model.GlobalConstants;
import br.udesc.simulador.trafego.model.observer.ObserverNode;
import br.udesc.simulador.trafego.model.piece.PieceModel;
import br.udesc.simulador.trafego.model.thread.Car;

public class TrafficSimulatorView extends JFrame implements ObserverNode {

    private static final long serialVersionUID = 1L;
    private TrafficSystemController controller;
    private JLabel lblCurrentThreadCount;
    private TrafficSimulatorTableView board;

    private JTextField txtMaxThreads;
    private JTextField txtInsertionInterval;

    private JButton btnStart;
    private JButton btnStopInsertion;
    private JButton btnStopAll;

    public TrafficSimulatorView(){
        super();
        controller = new TrafficSystemController();
        controller.addObserver(this);
        initialize();
    }

    private void initialize() {
        setProperties();
        addComponents();
    }

    private void setProperties() {
        setTitle("Traffic Simulator");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(GlobalConstants.SCREEN_WIDTH, GlobalConstants.SCREEN_HEIGHT));
        setLayout(new BorderLayout());
        pack();
    }

    private void addComponents() {
        board = new TrafficSimulatorTableView(controller);

        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints constraints = new GridBagConstraints();

        JLabel lblTitleMaxThreads = new JLabel("Quantidade");
        lblTitleMaxThreads.setPreferredSize(new Dimension(GlobalConstants.SCREEN_WIDTH/6, GlobalConstants.GRID_COLUMN_WIDTH));

        JLabel lblTitleInterval = new JLabel("Intervalo (ms)");
        lblTitleInterval.setPreferredSize(new Dimension(GlobalConstants.SCREEN_WIDTH/6, GlobalConstants.GRID_COLUMN_WIDTH));

        JLabel lblTitleCurrentThread = new JLabel("Threads rodando");
        lblTitleCurrentThread.setPreferredSize(new Dimension(GlobalConstants.SCREEN_WIDTH/6, GlobalConstants.GRID_COLUMN_WIDTH));

        btnStart = new JButton("Começar");
        btnStart.setPreferredSize(new Dimension(GlobalConstants.SCREEN_WIDTH/6, GlobalConstants.GRID_COLUMN_WIDTH));

        btnStopAll = new JButton("Parar Simulação");
        btnStopAll.setPreferredSize(new Dimension(GlobalConstants.SCREEN_WIDTH/6, GlobalConstants.GRID_COLUMN_WIDTH));
        btnStopAll.setEnabled(false);

        btnStopInsertion = new JButton("Parar inserção");
        btnStopInsertion.setPreferredSize(new Dimension(GlobalConstants.SCREEN_WIDTH/6, GlobalConstants.GRID_COLUMN_WIDTH));
        btnStopInsertion.setEnabled(false);

        txtMaxThreads = new JTextField("10");
        txtMaxThreads.setPreferredSize(new Dimension(GlobalConstants.SCREEN_WIDTH/6, GlobalConstants.GRID_COLUMN_WIDTH));

        txtInsertionInterval = new JTextField("1000");
        txtInsertionInterval.setPreferredSize(new Dimension(GlobalConstants.SCREEN_WIDTH/6, GlobalConstants.GRID_COLUMN_WIDTH));

        lblCurrentThreadCount = new JLabel();
        lblCurrentThreadCount.setText("0");
        lblCurrentThreadCount.setPreferredSize(new Dimension(GlobalConstants.SCREEN_WIDTH/6, GlobalConstants.GRID_COLUMN_WIDTH));

        JPanel panButtonLines = new JPanel();
        panButtonLines.setLayout(layout);

        constraints.gridx = 0; constraints.gridy = 0; constraints.insets = new Insets(0, 10, 0, 10);
        panButtonLines.add(lblTitleMaxThreads, constraints);

        constraints.gridx = 1;
        panButtonLines.add(lblTitleInterval, constraints);

        constraints.gridx = 2;
        panButtonLines.add(lblTitleCurrentThread, constraints);

        constraints.gridx = 0; constraints.gridy = 1; constraints.insets = new Insets(10, 10, 0, 10);
        panButtonLines.add(txtMaxThreads, constraints);

        constraints.gridx = 1;
        panButtonLines.add(txtInsertionInterval, constraints);

        constraints.gridx = 2;
        panButtonLines.add(lblCurrentThreadCount, constraints);

        constraints.gridx = 0; constraints.gridy = 2; constraints.insets = new Insets(10, 10, 10, 10);
        panButtonLines.add(btnStart, constraints);

        constraints.gridx = 1;
        panButtonLines.add(btnStopInsertion, constraints);

        constraints.gridx = 2;
        panButtonLines.add(btnStopAll, constraints);

        JPanel jpTraffic = new JPanel();
        jpTraffic.setLayout(layout);
        jpTraffic.add(board, constraints);

        JPanel panLayout = new JPanel();
        panLayout.setLayout(layout);
        panLayout.setSize(GlobalConstants.SCREEN_WIDTH, GlobalConstants.SCREEN_HEIGHT);

        constraints.gridx = 0; constraints.gridy = 0;
        panLayout.add(panButtonLines, constraints);

        constraints.gridy = 1;
        panLayout.add(jpTraffic, constraints);

        JScrollPane scpScroll = new JScrollPane(panLayout);
        setTitle("Road Mesh");
        setVisible(true);
        setSize(GlobalConstants.SCREEN_WIDTH, GlobalConstants.SCREEN_HEIGHT);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setContentPane(scpScroll);

        btnStart.addActionListener(e -> {
            controller.startSimulation(txtMaxThreads.getText(), txtInsertionInterval.getText());
            btnStopAll.setEnabled(true);
            btnStopInsertion.setEnabled(true);
            btnStart.setEnabled(false);
        });

        btnStopInsertion.addActionListener(e -> {
            controller.stopInsertion();
            btnStopInsertion.setEnabled(false);
        });

        btnStopAll.addActionListener(e -> {
            controller.stopAllCars();
            btnStopAll.setEnabled(false);
            btnStopInsertion.setEnabled(false);
            btnStart.setEnabled(true);
        });
    }

    public synchronized void updateThreadCount() {
        lblCurrentThreadCount.setText(String.valueOf(controller.getCars().size()));
        lblCurrentThreadCount.repaint();
    }


    @Override
    public void notifyStartCar(int line, int column) {
        TableModel model = board.getModel();
        PieceModel pieceAtual = (PieceModel) model.getValueAt(line, column);
        pieceAtual.setHasCar(true);
        model.setValueAt(pieceAtual, line, column);
        controller.pieces[line][column] = pieceAtual;
        board.repaint();
        updateThreadCount();
    }

    @Override
    public void notifyMoveCar(int pastLine, int pastColumn, int newLine, int newColumn) {
        TableModel model = board.getModel();
        PieceModel pieceAtual = (PieceModel) model.getValueAt(pastLine, pastColumn);
        PieceModel pieceNext = (PieceModel) model.getValueAt(newLine, newColumn);
        pieceAtual.setHasCar(false);
        pieceNext.setHasCar(true);
        model.setValueAt(pieceAtual, pastLine, pastColumn);
        model.setValueAt(pieceNext, newLine, newColumn);
        controller.pieces[pastLine][pastColumn] = pieceAtual;
        controller.pieces[newLine][newColumn] = pieceNext;
        board.repaint();
    }

    @Override
    public void notifyEndCar(int line, int column, Car car) {
        TableModel model = board.getModel();
        PieceModel pieceAtual = (PieceModel) model.getValueAt(line, column);
        pieceAtual.setHasCar(false);
        model.setValueAt(pieceAtual, line, column);
        controller.pieces[line][column] = pieceAtual;
        board.repaint();
        updateThreadCount();

        if (!controller.isInsertionActive() && controller.getCars().isEmpty()) {
            btnStopAll.setEnabled(false);
            btnStopInsertion.setEnabled(false);
            btnStart.setEnabled(true);
        }
    }
}