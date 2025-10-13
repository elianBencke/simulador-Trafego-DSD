package br.udesc.simulador.trafego.ui.view;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

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

    public InitialView() {
        this.controller = new StartupController(this);
        defineProperties();
        initializeActions();
    }

    private void defineProperties() {
        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints constraints = new GridBagConstraints();
        JLabel lblTitle = new JLabel("Escolha o arquivo de malha");

        txtFilePath = new JTextField();
        txtFilePath.setText("Selecione o arquivo");
        txtFilePath.setPreferredSize(new Dimension(310, 26));
        txtFilePath.setMinimumSize(new Dimension(310, 26));
        txtFilePath.setEnabled(false);

        btnBrowse = new JButton("Buscar");
        btnBrowse.setPreferredSize(new Dimension(120, 26));
        btnBrowse.setMinimumSize(new Dimension(120, 26));

        JPanel panSearchLine = new JPanel();
        panSearchLine.setLayout(layout);
        panSearchLine.setSize(400, 50);
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.insets = new Insets(0, 0, 0, 0);
        panSearchLine.add(txtFilePath, constraints);
        constraints.gridx = 1;
        constraints.insets = new Insets(0, 10, 0, 0);
        panSearchLine.add(btnBrowse, constraints);


        btnStartMutex = new JButton("Semaforo");
        btnStartMutex.setPreferredSize(new Dimension(120, 26));
        btnStartMutex.setMinimumSize(new Dimension(120, 26));
        btnStartMutex.setEnabled(false);

        btnStartMonitor = new JButton("Monitor");
        btnStartMonitor.setPreferredSize(new Dimension(120, 26));
        btnStartMonitor.setMinimumSize(new Dimension(120, 26));
        btnStartMonitor.setEnabled(false);


        fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        JPanel panLayout = new JPanel();
        panLayout.setLayout(layout);
        panLayout.setSize(600, 250);

        constraints.gridx = 0;
        panLayout.add(lblTitle, constraints);

        constraints.gridy = 1;
        constraints.insets = new Insets(10, 0, 0, 0);
        panLayout.add(panSearchLine, constraints);

        constraints.gridy = 2;
        constraints.insets = new Insets(10, 320, 0, 0);
        panLayout.add(btnStartMutex, constraints);

        constraints.gridy = 4;
        constraints.insets = new Insets(10, 320, 0, 0);
        panLayout.add(btnStartMonitor, constraints);

        setTitle("Escolha a malha");
        setVisible(true);
        setSize(600, 250);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setContentPane(panLayout);
    }

    private void initializeActions() {
        btnBrowse.addActionListener(click -> {
            int i= fileChooser.showSaveDialog(null);
            if (i==1){
                txtFilePath.setText("Select a road mesh file");
                btnStartMutex.setEnabled(false);
                btnStartMonitor.setEnabled(false);
            } else {
                this.controller.updateRoadMesh(fileChooser.getSelectedFile());
            }
        });

        btnStartMutex.addActionListener(click -> {
            MeshRepository.getInstance().setFactory(new SemaphoreNodeFactory());
            this.controller.navigateNextView();
        });

        btnStartMonitor.addActionListener(click -> {
            MeshRepository.getInstance().setFactory(new MonitorNodeFactory());
            this.controller.navigateNextView();
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
        JOptionPane.showMessageDialog(null, "O arquivo selecionado está inválido",
                "File Error", JOptionPane.ERROR_MESSAGE);
        btnStartMutex.setEnabled(false);
        btnStartMonitor.setEnabled(false);
    }

    @Override
    public void navigateNextView() {
        TrafficSimulatorView view = new TrafficSimulatorView();
        view.setVisible(true);
        this.dispose();
    }
}