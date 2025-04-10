package logic;

import org.jfree.data.xy.XYSeries;

public class EulerSolver {

    // Definir la ecuación diferencial dy/dx = f(x, y) (Ejemplo: y' = x + y)
    private static double f(double x, double y) {
        return 0.2*x * y;
    }

    // Método de Euler
    /*Esta es la implementación estándar del
    * método de Euler. Utiliza la fórmula: y_n+1 = y_n + h·f(x_n, y_n)
    *
    *
    * */
    public static XYSeries solveEuler(double x0, double y0, double h, int steps) {
        XYSeries series = new XYSeries("Euler");
        double x = x0, y = y0;

        for (int i = 0; i <= steps; i++) {
            series.add(x, y);
            y += h * f(x, y);
            x += h;
        }
        return series;
    }

    // Método de Euler Mejorado

    /*
    * Primero calcula un paso predictor utilizando el método de Euler.
    * Luego utiliza el promedio de las pendientes en el punto actual y el punto predicho
    *
    * */
    public static XYSeries solveEulerMejorado(double x0, double y0, double h, int steps) {
        XYSeries series = new XYSeries("Euler Mejorado");
        double x = x0, y = y0;

        for (int i = 0; i <= steps; i++) {
            series.add(x, y);
            double yPredict = y + h * f(x, y);
            y += (h / 2) * (f(x, y) + f(x + h, yPredict));
            x += h;
        }
        return series;
    }

    // Método de Runge-Kutta de 4to Orden (RK4)
// Este método calcula una estimación más precisa al promediar 4 pendientes (k1 a k4)
    public static XYSeries solveRungeKutta(double x0, double y0, double h, int steps) {
        XYSeries series = new XYSeries("Runge-Kutta");
        double x = x0;
        double y = y0;

        for (int i = 0; i <= steps; i++) {
            series.add(x, y);

            // Cálculo de las pendientes
            double k1 = f(x, y);
            double k2 = f(x + h / 2, y + (h / 2) * k1);
            double k3 = f(x + h / 2, y + (h / 2) * k2);
            double k4 = f(x + h, y + h * k3);

            // Fórmula clásica de Runge-Kutta 4to orden
            y += (h / 6) * (k1 + 2 * k2 + 2 * k3 + k4);
            x += h;
        }
        return series;
    }
}
