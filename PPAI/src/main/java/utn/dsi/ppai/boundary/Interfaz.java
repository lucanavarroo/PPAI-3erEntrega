package utn.dsi.ppai.boundary;

import utn.dsi.ppai.control.GestorCierreInspeccion;
import utn.dsi.ppai.mock.Datos;
import utn.dsi.ppai.entity.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class Interfaz extends JFrame implements InterfazCierreInspeccion {
    private JComboBox<String> comboOrdenes;
    private JTextArea txtObservacion;
    private JPanel panelMotivos;
    private JButton btnConfirmar;
    private JButton btnConfirmarOI;
    private JButton btnEnviarObs;
    private JButton btnTomarMotivo;
    private JButton btnConfirmarCierre;
    private JButton btnCancelarCierre;
    private JPanel panelObs;
    private List<JCheckBox> checkBoxesMotivos;
    private List<JTextField> textFieldsObservaciones;
    private GestorCierreInspeccion gestor;
    private JPanel startPanel;
    private JLabel lblUsuario;

    public Interfaz() {
        checkBoxesMotivos = new ArrayList<>();
        textFieldsObservaciones = new ArrayList<>();
        setupGestor();
        initializeComponents();
    }

    private void initializeComponents() {
        setTitle("Cierre de Orden de Inspección");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600, 700);
        setLocationRelativeTo(null);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        setBorder(10);

        // Metodo self, habilitar pantalla
        habilitarPantalla();
        // Resto de paneles

        setupUserNamePanel();
        setupOrdenPanel();
        setupObservacionPanel();
        setupMotivosPanel();
        setupConfirmButtons();


        comboOrdenes.getParent().setVisible(false);
        panelObs.setVisible(false);
        panelMotivos.setVisible(false);
        btnConfirmarCierre.getParent().setVisible(false);

        setVisible(true);
    }


    private void setupUserNamePanel() {
        String userName = (Datos.usuario != null) ? Datos.usuario.getNombreUsuario() : "Usuario Desconocido";
        lblUsuario = new JLabel("Usuario: " + userName);
        lblUsuario.setFont(new Font("Arial", Font.ITALIC, 12));
        lblUsuario.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10)); // Padding


        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        userPanel.add(lblUsuario); // This helps with BoxLayout if it's the only component in its row
        add(userPanel, BorderLayout.NORTH); // Add this panel to the content pane first
    }

    public void habilitarPantalla() {
        startPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton btnIniciarCierre = new JButton("Cerrar Orden de Inspección");
        btnIniciarCierre.setFont(new Font("Arial", Font.BOLD, 18));
        btnIniciarCierre.setPreferredSize(new Dimension(300, 80));

        btnIniciarCierre.addActionListener(e -> {
            startPanel.setVisible(false);
            comboOrdenes.getParent().setVisible(true);
            btnConfirmarCierre.getParent().setVisible(true);
        });

        startPanel.add(btnIniciarCierre);
        add(startPanel);
    }

    private void setupOrdenPanel() {
        JPanel pOrden = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pOrden.add(new JLabel("Orden de Inspección:"));
        comboOrdenes = new JComboBox<>();
        comboOrdenes.addActionListener(e -> {
            if (comboOrdenes.getSelectedIndex() > 0) {
                btnConfirmarOI.setEnabled(true);
            } else {
                btnConfirmarOI.setEnabled(false);
            }
        });
        pOrden.add(comboOrdenes);

        btnConfirmarOI = new JButton("Confirmar Orden");
        btnConfirmarOI.setEnabled(false);
        btnConfirmarOI.addActionListener(e -> {
            gestor.tomarSeleccionOI((String)comboOrdenes.getSelectedItem());
            panelObs.setVisible(true);
        });
        pOrden.add(btnConfirmarOI);

        add(pOrden);
    }

    private void setupObservacionPanel() {
        panelObs = new JPanel(new BorderLayout());
        panelObs.setBorder(BorderFactory.createTitledBorder("Observación General"));
        txtObservacion = new JTextArea(4, 10);
        txtObservacion.setLineWrap(true);
        txtObservacion.setWrapStyleWord(true);

        panelObs.add(new JScrollPane(txtObservacion), BorderLayout.CENTER);

        JButton btnEnviarObs = new JButton("Enviar Observación");
        btnEnviarObs.addActionListener(e -> {
            if (!txtObservacion.getText().trim().isEmpty()) {
                gestor.tomarObservacionOrdenCierre(txtObservacion.getText().trim());
                panelMotivos.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Debe ingresar una observación",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
        panelObs.add(btnEnviarObs, BorderLayout.SOUTH);

        panelObs.setVisible(false);
        add(panelObs);
    }

    private void setupMotivosPanel() {
        panelMotivos = new JPanel();
        panelMotivos.setLayout(new BoxLayout(panelMotivos, BoxLayout.Y_AXIS));
        panelMotivos.setBorder(BorderFactory.createTitledBorder("Motivos"));
        add(panelMotivos);
    }


    private void setupConfirmButtons() {
        JPanel panel = new JPanel();
        btnConfirmarCierre = new JButton("Confirmar Cierre");
        btnCancelarCierre = new JButton("Cancelar");

        btnConfirmarCierre.setVisible(false);
        btnCancelarCierre.setVisible(true);

        btnConfirmarCierre.addActionListener(e -> {
            JOptionPane.showMessageDialog(this,
                    "Orden de Inspeccion cerrada, mails enviados a los empleados y publicados en monitores CCRS exitosamente.",
                    "Confirmado",
                    JOptionPane.INFORMATION_MESSAGE);
            gestor.tomarConfirmacionOI(true);
        });
        btnCancelarCierre.addActionListener(e -> {
            gestor.tomarConfirmacionOI(false);
            gestor.finCU();
        });

        panel.add(btnConfirmarCierre);
        btnCancelarCierre.setBackground(Color.RED); // Fondo rojo
        btnCancelarCierre.setForeground(Color.WHITE); // Texto en blanco para mejor contraste
        panel.add(btnCancelarCierre);

        add(Box.createRigidArea(new Dimension(0,10)));
        add(panel);
    }

    private void setupConfirmButton() {
        btnConfirmar = new JButton("Confirmar Cierre");
        btnConfirmar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnConfirmar.setVisible(false);
        btnConfirmar.addActionListener(e -> confirmar());
        add(Box.createRigidArea(new Dimension(0,10)));
        add(btnConfirmar);
    }

    private void setupGestor() {
        try {
            Datos.inicializarDatos();

            gestor = new GestorCierreInspeccion(
                    Datos.listEmpleados.get(0),
                    null,
                    "",
                    Datos.sesion,
                    this,
                    null,
                    null,
                    Datos.usuario
            );

            gestor.iniciarCierreOI(
                    Datos.listaDeTodosLosEstados,
                    Datos.listMotivosTipo,
                    Datos.listEmpleados,
                    Datos.sesion,
                    Datos.listOrdenesDeInspeccion,
                    Datos.listSismografos
            );
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error al inicializar: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // Implementación de los métodos de InterfazCierreInspeccion
    @Override
    public void setGestor(GestorCierreInspeccion gestor) {
        this.gestor = gestor;
    }

    @Override
    public void solicitarSeleccionOI(List<String> listaOI) {
        SwingUtilities.invokeLater(() -> {
            DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
            model.addElement("-- Seleccionar orden --");
            for (String orden : listaOI) {
                model.addElement(orden);
            }
            comboOrdenes.setModel(model);
            System.out.println("Órdenes cargadas: " + listaOI.size()); // Para debug
        });
    }

    @Override
    public void seleccionarOI(String seleccionada) {
        gestor.tomarSeleccionOI(seleccionada);
    }

    @Override
    public void pedirObservacionOrdenCierre() {
        panelObs.setVisible(true);
    }

    @Override
    public void ingresarObservacionOrdenCierre(String observacion) {
        gestor.tomarObservacionOrdenCierre(observacion);
    }

    @Override
    public void solicitarSeleccionMotivo(List<String> listaMotivos) {
        panelMotivos.removeAll();
        checkBoxesMotivos.clear();
        textFieldsObservaciones.clear();

        for (String motivo : listaMotivos) {
            JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JCheckBox checkBox = new JCheckBox(motivo);
            checkBoxesMotivos.add(checkBox);
            panel.add(checkBox);
            panelMotivos.add(panel);
        }

        if (btnTomarMotivo == null) {
            btnTomarMotivo = new JButton("Tomar Motivo");
        } else {
            for (ActionListener al : btnTomarMotivo.getActionListeners()) {
                btnTomarMotivo.removeActionListener(al);
            }
        }

        btnTomarMotivo.addActionListener(e -> {
            List<String> motivosSeleccionados = new ArrayList<>();
            for (JCheckBox checkBox : checkBoxesMotivos) {
                if (checkBox.isSelected()) {
                    motivosSeleccionados.add(checkBox.getText());
                }
            }
            if (motivosSeleccionados.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Debe seleccionar al menos un motivo", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            gestor.tomarSeleccionMotivo(motivosSeleccionados);
        });

        panelMotivos.add(btnTomarMotivo);
        panelMotivos.setVisible(true);
        panelMotivos.revalidate();
        panelMotivos.repaint();
    }

    @Override
    public void solicitarComentario(String motivo) {
        while (true) {
            String comentario = JOptionPane.showInputDialog(this, "Ingrese comentario para: " + motivo, "");
            if (comentario == null) {
                JOptionPane.showMessageDialog(this, "Debe ingresar un comentario.", "Error", JOptionPane.ERROR_MESSAGE);
                continue;
            }
            if (!comentario.trim().isEmpty()) {
                gestor.tomarComentario(comentario.trim());
                break;
            } else {
                JOptionPane.showMessageDialog(this, "Debe ingresar un comentario.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    @Override
    public void tomarSeleccionMotivo(List<String> motivos) {
        gestor.tomarSeleccionMotivo(motivos);
    }

    @Override
    public void tomarComentario(String comentarioMotivo) {
        gestor.tomarComentario(comentarioMotivo);
    }

    @Override
    public void solicitarConfirmacionCierre() {
        btnConfirmarCierre.setVisible(true);
        btnCancelarCierre.setVisible(true);
    }

    @Override
    public void confirmarCierreOI() {
        gestor.tomarConfirmacionOI(true);
    }

    @Override
    public void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void confirmar() {
        if (comboOrdenes.getSelectedIndex() == 0) {
            showError("Debe seleccionar una orden de inspección");
            return;
        }
        if (txtObservacion.getText().trim().isEmpty()) {
            showError("Debe ingresar una observación");
            return;
        }

        List<String> motivosSeleccionados = new ArrayList<>();
        List<String> observaciones = new ArrayList<>();

        for (int i = 0; i < checkBoxesMotivos.size(); i++) {
            if (checkBoxesMotivos.get(i).isSelected()) {
                motivosSeleccionados.add(checkBoxesMotivos.get(i).getText());
                observaciones.add(textFieldsObservaciones.get(i).getText().trim());
            }
        }

        if (motivosSeleccionados.isEmpty()) {
            showError("Debe seleccionar al menos un motivo");
            return;
        }

        tomarSeleccionMotivo(motivosSeleccionados);
        for (String obs : observaciones) {
            tomarComentario(obs);
        }
        confirmarCierreOI();
    }

    private void showError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void setBorder(int pad) {
        ((JComponent)getContentPane()).setBorder(
                BorderFactory.createEmptyBorder(pad,pad,pad,pad)
        );
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Interfaz::new);
    }
}