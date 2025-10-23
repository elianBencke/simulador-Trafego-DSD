package br.udesc.simulador.trafego.ui.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Color;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.table.TableModel;
import javax.swing.BorderFactory;
import javax.swing.border.TitledBorder;

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

    private String maxThreads;
    private String insertionInterval;

    private JScrollPane scpScroll;

    private JButton btnStart;
    private JButton btnStopInsertion;
    private JButton btnStopAll;

    public TrafficSimulatorView(String maxThreads, String insertionInterval){
        super();
        this.maxThreads = maxThreads;
        this.insertionInterval = insertionInterval;

        controller = new TrafficSystemController();
        controller.addObserver(this);
        initialize();

        pack();
        setLocationRelativeTo(null);
    }

    private void initialize() {
        setProperties();
        addComponents();
        if (scpScroll != null) {
            setContentPane(scpScroll);
        }
    }

    private void setProperties() {
        setTitle("Simulador");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setBackground(new Color(245, 245, 245));
        setPreferredSize(new Dimension(GlobalConstants.SCREEN_WIDTH, GlobalConstants.SCREEN_HEIGHT));
        setLayout(new BorderLayout());
    }

    private void addComponents() {
        board = new TrafficSimulatorTableView(controller);

        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints constraints = new GridBagConstraints();
        Font controlFont = new Font("Arial", Font.BOLD, 12);
        Font valueFont = new Font("Arial", Font.BOLD, 16);
        JLabel lblTitleMaxThreads = new JLabel("Máximo de Threads: " + maxThreads);
        lblTitleMaxThreads.setPreferredSize(new Dimension(GlobalConstants.SCREEN_WIDTH/6, GlobalConstants.GRID_COLUMN_WIDTH));
        lblTitleMaxThreads.setFont(controlFont);

        JLabel lblTitleInterval = new JLabel("Intervalo (ms): " + insertionInterval);
        lblTitleInterval.setPreferredSize(new Dimension(GlobalConstants.SCREEN_WIDTH/6, GlobalConstants.GRID_COLUMN_WIDTH));
        lblTitleInterval.setFont(controlFont);

        JLabel lblTitleCurrentThread = new JLabel("Threads rodando");
        lblTitleCurrentThread.setPreferredSize(new Dimension(GlobalConstants.SCREEN_WIDTH/6, GlobalConstants.GRID_COLUMN_WIDTH));
        lblTitleCurrentThread.setFont(controlFont);

        btnStart = new JButton("COMEÇAR");
        btnStart.setPreferredSize(new Dimension(GlobalConstants.SCREEN_WIDTH/6, GlobalConstants.GRID_COLUMN_WIDTH));
        btnStart.setBackground(new Color(0, 150, 0));
        btnStart.setForeground(Color.WHITE);
        btnStart.setOpaque(true);
        btnStart.setBorderPainted(false);

        btnStopInsertion = new JButton("PARAR INSERÇÃO");
        btnStopInsertion.setPreferredSize(new Dimension(GlobalConstants.SCREEN_WIDTH/6, GlobalConstants.GRID_COLUMN_WIDTH));
        btnStopInsertion.setBackground(new Color(255, 165, 0));
        btnStopInsertion.setForeground(Color.BLACK);
        btnStopInsertion.setOpaque(true);
        btnStopInsertion.setBorderPainted(false);
        btnStopInsertion.setEnabled(false);

        btnStopAll = new JButton("PARAR SIMULAÇÃO");
        btnStopAll.setPreferredSize(new Dimension(GlobalConstants.SCREEN_WIDTH/6, GlobalConstants.GRID_COLUMN_WIDTH));
        btnStopAll.setBackground(new Color(200, 0, 0));
        btnStopAll.setForeground(Color.WHITE);
        btnStopAll.setOpaque(true);
        btnStopAll.setBorderPainted(false);
        btnStopAll.setEnabled(false);

        lblCurrentThreadCount = new JLabel();
        lblCurrentThreadCount.setText("0");
        lblCurrentThreadCount.setPreferredSize(new Dimension(GlobalConstants.SCREEN_WIDTH/6, GlobalConstants.GRID_COLUMN_WIDTH));
        lblCurrentThreadCount.setFont(valueFont);
        lblCurrentThreadCount.setForeground(new Color(0, 100, 0));

        JPanel panButtonLines = new JPanel();
        panButtonLines.setLayout(layout);
        panButtonLines.setOpaque(false);

        panButtonLines.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createTitledBorder(
                                BorderFactory.createLineBorder(new Color(180, 180, 180)),
                                "Controle de simulação",
                                TitledBorder.LEFT,
                                TitledBorder.TOP,
                                new Font("SansSerif", Font.BOLD, 14)
                        ),
                        BorderFactory.createEmptyBorder(10, 10, 10, 10)
                )
        );

        constraints.gridx = 0; constraints.gridy = 0; constraints.insets = new Insets(0, 10, 0, 10);
        panButtonLines.add(lblTitleMaxThreads, constraints);

        constraints.gridx = 1;
        panButtonLines.add(lblTitleInterval, constraints);

        constraints.gridx = 2;
        panButtonLines.add(lblTitleCurrentThread, constraints);

        constraints.gridx = 2; constraints.gridy = 1; constraints.insets = new Insets(10, 10, 0, 10);
        panButtonLines.add(lblCurrentThreadCount, constraints);

        constraints.gridx = 0; constraints.gridy = 2; constraints.insets = new Insets(10, 10, 10, 10);
        panButtonLines.add(btnStart, constraints);

        constraints.gridx = 1;
        panButtonLines.add(btnStopInsertion, constraints);

        constraints.gridx = 2;
        panButtonLines.add(btnStopAll, constraints);

        JPanel jpTraffic = new JPanel();
        jpTraffic.setLayout(layout);
        jpTraffic.setOpaque(false);
        jpTraffic.add(board, constraints);

        JPanel panLayout = new JPanel();
        panLayout.setLayout(layout);
        panLayout.setOpaque(false);

        constraints.gridx = 0; constraints.gridy = 0; constraints.insets = new Insets(10, 0, 0, 0);
        panLayout.add(panButtonLines, constraints);

        constraints.gridy = 1;
        panLayout.add(jpTraffic, constraints);

        scpScroll = new JScrollPane(panLayout);
        scpScroll.setOpaque(false);
        scpScroll.getViewport().setOpaque(false);

        setTitle("Road Mesh");
        setSize(GlobalConstants.SCREEN_WIDTH, GlobalConstants.SCREEN_HEIGHT);

        btnStart.addActionListener(e -> {
            controller.startSimulation(this.maxThreads, this.insertionInterval);
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
        String count = String.valueOf(controller.getCars().size());
        javax.swing.SwingUtilities.invokeLater(() -> {
            lblCurrentThreadCount.setText(count);
            lblCurrentThreadCount.repaint();
        });
    }

    @Override
    public void notifyStartCar(int line, int column) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            TableModel model = board.getModel();
            PieceModel pieceAtual = (PieceModel) model.getValueAt(line, column);
            pieceAtual.setHasCar(true);
            model.setValueAt(pieceAtual, line, column);
            controller.pieces[line][column] = pieceAtual;
            board.repaint();
            updateThreadCount();
        });
    }

    @Override
    public void notifyMoveCar(int pastLine, int pastColumn, int newLine, int newColumn) {
        javax.swing.SwingUtilities.invokeLater(() -> {
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
        });
    }

    @Override
    public void notifyEndCar(int line, int column, Car car) {
        javax.swing.SwingUtilities.invokeLater(() -> {
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
        });
    }
}