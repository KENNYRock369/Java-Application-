package utils;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import javax.swing.*;
import java.awt.*;
import logic.EulerSolver;

class EulerGraph extends JFrame {
    private JPanel mainPanel;
    private JButton calculateButton;
    private JTextField x0Field, y0Field, hField, stepsField;

    public EulerGraph() {
        setTitle("Método de Euler y Euler Mejorado");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        mainPanel = new JPanel(new BorderLayout());
        JPanel inputPanel = new JPanel(new GridLayout(2, 4, 10, 10));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Parámetros"));

        x0Field = new JTextField("0");
        y0Field = new JTextField("1");
        hField = new JTextField("0.1");
        stepsField = new JTextField("10");

        inputPanel.add(new JLabel("x0:"));
        inputPanel.add(x0Field);
        inputPanel.add(new JLabel("y0:"));
        inputPanel.add(y0Field);
        inputPanel.add(new JLabel("Paso (h):"));
        inputPanel.add(hField);
        inputPanel.add(new JLabel("Pasos:"));
        inputPanel.add(stepsField);

        calculateButton = new JButton("Calcular y Graficar");
        calculateButton.addActionListener(e -> updateChart());

        mainPanel.add(inputPanel, BorderLayout.NORTH);
        mainPanel.add(calculateButton, BorderLayout.SOUTH);
        getContentPane().add(mainPanel);
    }

    private void updateChart() {
        double x0 = Double.parseDouble(x0Field.getText());
        double y0 = Double.parseDouble(y0Field.getText());
        double h = Double.parseDouble(hField.getText());
        int steps = Integer.parseInt(stepsField.getText());

        XYSeries eulerSeries = EulerSolver.solveEuler(x0, y0, h, steps);
        XYSeries eulerMejoradoSeries = EulerSolver.solveEulerMejorado(x0, y0, h, steps);

        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(eulerSeries);
        dataset.addSeries(eulerMejoradoSeries);

        JFreeChart chart = ChartFactory.createXYLineChart(
                "Comparación de Métodos Euler",
                "X", "Y",
                dataset,
                PlotOrientation.VERTICAL,
                true, true, false);

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(850, 500));

        if (mainPanel.getComponentCount() > 2) {
            mainPanel.remove(2);
        }
        mainPanel.add(chartPanel, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();
    }
}