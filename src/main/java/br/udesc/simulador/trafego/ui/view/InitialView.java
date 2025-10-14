package br.udesc.simulador.trafego.ui.view;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Font;
import java.awt.Color;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.BorderFactory;
import javax.swing.border.TitledBorder;

import br.udesc.simulador.trafego.controller.StartupController;
import br.udesc.simulador.trafego.model.factory.MonitorNodeFactory;
import br.udesc.simulador.trafego.model.factory.SemaphoreNodeFactory;
import br.udesc.simulador.trafego.ui.observer.ObserverInitialView;
import br.udesc.simulador.trafego.model.singleton.MeshRepository;

public class InitialView extends JFrame implements ObserverInitialView {
    private static final long serialVersionUID = 1L;
    private StartupController controller;
    private JFileChooser fileChooser;
    private JTextField txtFilePath;
    private JButton btnBrowse;
    private JButton btnStartMutex;
    private JButton btnStartMonitor;

    private JTextField txtMaxThreads;
    private JTextField txtInsertionInterval;

    public InitialView() {
        this.controller = new StartupController(this);
        defineProperties();
        initializeActions();
    }

    private void defineProperties() {
        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints constraints = new GridBagConstraints();

        Color monitorColor = new Color(0, 100, 200);
        Color mutexColor = new Color(200, 100, 0);

        JLabel lblTitle = new JLabel("Escolha o arquivo");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 16));
        lblTitle.setForeground(new Color(50, 50, 50));


        txtFilePath = new JTextField();
        txtFilePath.setText("Escolha o arquivo");
        txtFilePath.setPreferredSize(new Dimension(310, 26));
        txtFilePath.setMinimumSize(new Dimension(310, 26));
        txtFilePath.setEnabled(false);

        btnBrowse = new JButton("Buscar");
        btnBrowse.setPreferredSize(new Dimension(120, 26));
        btnBrowse.setMinimumSize(new Dimension(120, 26));
        btnBrowse.setBackground(new Color(220, 220, 220));
        btnBrowse.setOpaque(true);
        btnBrowse.setBorderPainted(false);

        JPanel panSearchLine = new JPanel();
        panSearchLine.setLayout(layout);
        panSearchLine.setOpaque(false);
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.insets = new Insets(0, 0, 0, 0);
        panSearchLine.add(txtFilePath, constraints);
        constraints.gridx = 1;
        constraints.insets = new Insets(0, 10, 0, 0);
        panSearchLine.add(btnBrowse, constraints);

        JLabel lblMaxThreads = new JLabel("Máximo de Threads:");
        lblMaxThreads.setFont(new Font("Arial", Font.PLAIN, 12));
        txtMaxThreads = new JTextField("10");
        txtMaxThreads.setPreferredSize(new Dimension(80, 26));

        JLabel lblInsertionInterval = new JLabel("Intervalo (ms):");
        lblInsertionInterval.setFont(new Font("Arial", Font.PLAIN, 12));
        txtInsertionInterval = new JTextField("1000");
        txtInsertionInterval.setPreferredSize(new Dimension(80, 26));

        JPanel panConfigLine = new JPanel();
        panConfigLine.setLayout(layout);
        panConfigLine.setOpaque(false);

        constraints.gridx = 0; constraints.gridy = 0; constraints.insets = new Insets(5, 0, 5, 10);
        panConfigLine.add(lblMaxThreads, constraints);

        constraints.gridx = 1; constraints.insets = new Insets(5, 0, 5, 20);
        panConfigLine.add(txtMaxThreads, constraints);

        constraints.gridx = 2; constraints.insets = new Insets(5, 0, 5, 10);
        panConfigLine.add(lblInsertionInterval, constraints);

        constraints.gridx = 3; constraints.insets = new Insets(5, 0, 5, 0);
        panConfigLine.add(txtInsertionInterval, constraints);

        panConfigLine.setBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        );

        btnStartMutex = new JButton("Semaforo");
        btnStartMutex.setPreferredSize(new Dimension(120, 30));
        btnStartMutex.setMinimumSize(new Dimension(120, 30));
        btnStartMutex.setEnabled(false);
        btnStartMutex.setBackground(mutexColor);
        btnStartMutex.setForeground(Color.WHITE);
        btnStartMutex.setOpaque(true);
        btnStartMutex.setBorderPainted(false);

        btnStartMonitor = new JButton("Monitor");
        btnStartMonitor.setPreferredSize(new Dimension(120, 30));
        btnStartMonitor.setMinimumSize(new Dimension(120, 30));
        btnStartMonitor.setEnabled(false);
        btnStartMonitor.setBackground(monitorColor);
        btnStartMonitor.setForeground(Color.WHITE);
        btnStartMonitor.setOpaque(true);
        btnStartMonitor.setBorderPainted(false);


        fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        JPanel panLayout = new JPanel();
        panLayout.setLayout(layout);
        panLayout.setBackground(new Color(245, 245, 245));

        constraints.gridx = 0; constraints.gridy = 0; constraints.insets = new Insets(10, 0, 0, 0);
        panLayout.add(lblTitle, constraints);

        constraints.gridy = 1; constraints.insets = new Insets(15, 0, 0, 0);
        panLayout.add(panSearchLine, constraints);

        constraints.gridy = 2; constraints.insets = new Insets(10, 0, 0, 0);
        panLayout.add(panConfigLine, constraints);

        JPanel panButtons = new JPanel();
        panButtons.setLayout(layout);
        panButtons.setOpaque(false);

        constraints.gridx = 0; constraints.gridy = 0; constraints.insets = new Insets(0, 0, 0, 15);
        panButtons.add(btnStartMutex, constraints);

        constraints.gridx = 1; constraints.insets = new Insets(0, 15, 0, 0);
        panButtons.add(btnStartMonitor, constraints);

        constraints.gridx = 0; constraints.gridy = 3; constraints.insets = new Insets(20, 0, 20, 0);
        panLayout.add(panButtons, constraints);

        setTitle("Escolha a malha");
        setSize(600, 300);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setContentPane(panLayout);
    }

    private void initializeActions() {
        btnBrowse.addActionListener(click -> {
            int i= fileChooser.showSaveDialog(null);
            if (i==1){
                txtFilePath.setText("Escolha o arquivo");
                btnStartMutex.setEnabled(false);
                btnStartMonitor.setEnabled(false);
            } else {
                this.controller.updateRoadMesh(fileChooser.getSelectedFile());
            }
        });

        btnStartMutex.addActionListener(click -> {
            MeshRepository.getInstance().setFactory(new SemaphoreNodeFactory());
            this.controller.navigateNextView(txtMaxThreads.getText(), txtInsertionInterval.getText());
        });

        btnStartMonitor.addActionListener(click -> {
            MeshRepository.getInstance().setFactory(new MonitorNodeFactory());
            this.controller.navigateNextView(txtMaxThreads.getText(), txtInsertionInterval.getText());
        });
    }

    @Override
    public void activateInitialButton() {
        btnStartMutex.setEnabled(true);
        btnStartMonitor.setEnabled(true);
    }

    @Override
    public void updatePathText(String path) {
        txtFilePath.setText(path);

    }

    @Override
    public void notifyErrorFile() {
        JOptionPane.showMessageDialog(null, "O arquivo selecionado não é suportado",
                "File Error", JOptionPane.ERROR_MESSAGE);
        btnStartMutex.setEnabled(false);
        btnStartMonitor.setEnabled(false);
    }

    @Override
    public void navigateNextView(String maxThreads, String insertionInterval) {
        TrafficSimulatorView view = new TrafficSimulatorView(maxThreads, insertionInterval);
        view.setVisible(true);
        this.dispose();
    }
}