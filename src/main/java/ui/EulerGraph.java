package ui;

import com.formdev.flatlaf.FlatIntelliJLaf;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import logic.EulerSolver;

public class EulerGraph extends JFrame {
    private JTextField x0Field, y0Field, hField, stepsField;
    private JTable resultTable;
    private JSplitPane splitPane;

    public EulerGraph() {
        setTitle("Comparación de Métodos Numéricos");
        setSize(950, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        // Panel de entrada
        JPanel inputPanel = new JPanel(new GridLayout(2, 4, 10, 10));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Parámetros"));

        x0Field = createRoundedField("0");
        y0Field = createRoundedField("1");
        hField = createRoundedField("0.1");
        stepsField = createRoundedField("10");

        inputPanel.add(new JLabel("x₀:")); inputPanel.add(x0Field);
        inputPanel.add(new JLabel("y₀:")); inputPanel.add(y0Field);
        inputPanel.add(new JLabel("h:")); inputPanel.add(hField);
        inputPanel.add(new JLabel("Pasos:")); inputPanel.add(stepsField);

        // Botón con diseño moderno
        JButton calculateButton = new JButton("Calcular y Graficar");
        calculateButton.setBackground(new Color(0, 120, 215));
        calculateButton.setForeground(Color.WHITE);
        calculateButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        calculateButton.putClientProperty("JButton.arc", 20);
        calculateButton.addActionListener(e -> updateChart());

        // Tabla
        resultTable = new JTable(new DefaultTableModel(new String[]{"Paso", "Euler", "Euler Mejorado", "Runge-Kutta"}, 0));
        JScrollPane tableScrollPane = new JScrollPane(resultTable);

        // Panel de gráfico
        JPanel chartPanelContainer = new JPanel(new BorderLayout());

        // Split entre tabla y gráfico
        splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, tableScrollPane, chartPanelContainer);
        splitPane.setResizeWeight(0.5);
        splitPane.setOneTouchExpandable(true);
        splitPane.setDividerLocation(300);

        // Agregar todo al main panel
        mainPanel.add(inputPanel, BorderLayout.NORTH);
        mainPanel.add(splitPane, BorderLayout.CENTER);
        mainPanel.add(calculateButton, BorderLayout.SOUTH);

        setContentPane(mainPanel);
    }

    private JTextField createRoundedField(String text) {
        JTextField field = new JTextField(text);
        field.putClientProperty("JComponent.roundRect", true);
        field.putClientProperty("JComponent.outline", "none");
        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        return field;
    }

    private void updateChart() {
        double x0 = Double.parseDouble(x0Field.getText());
        double y0 = Double.parseDouble(y0Field.getText());
        double h = Double.parseDouble(hField.getText());
        int steps = Integer.parseInt(stepsField.getText());

        XYSeries euler = EulerSolver.solveEuler(x0, y0, h, steps);
        XYSeries mejorado = EulerSolver.solveEulerMejorado(x0, y0, h, steps);
        XYSeries runge = EulerSolver.solveRungeKutta(x0, y0, h, steps);

        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(euler);
        dataset.addSeries(mejorado);
        dataset.addSeries(runge);

        JFreeChart chart = ChartFactory.createXYLineChart(
                "Comparación de Métodos: Euler, Euler Mejorado y Runge-Kutta",
                "X", "Y",
                dataset,
                PlotOrientation.VERTICAL,
                true, true, false
        );

        // Estilo de las líneas
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesPaint(0, Color.BLUE);
        renderer.setSeriesPaint(1, new Color(0, 160, 0));
        renderer.setSeriesPaint(2, new Color(200, 0, 0)); // Rojo oscuro para Runge-Kutta
        renderer.setSeriesStroke(0, new BasicStroke(2.0f));
        renderer.setSeriesStroke(1, new BasicStroke(2.0f));
        renderer.setSeriesStroke(2, new BasicStroke(2.5f));
        chart.getXYPlot().setRenderer(renderer);

        // Actualizar panel de gráfico
        JPanel chartPanelContainer = (JPanel) splitPane.getBottomComponent();
        chartPanelContainer.removeAll();
        chartPanelContainer.add(new ChartPanel(chart), BorderLayout.CENTER);
        chartPanelContainer.revalidate();
        chartPanelContainer.repaint();

        // Llenar tabla
        DefaultTableModel model = (DefaultTableModel) resultTable.getModel();
        model.setRowCount(0);
        for (int i = 0; i <= steps; i++) {
            model.addRow(new Object[]{
                    i * h,
                    euler.getY(i),
                    mejorado.getY(i),
                    runge.getY(i)
            });
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatIntelliJLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            EulerGraph frame = new EulerGraph();
            frame.setVisible(true);
        });
    }
}
