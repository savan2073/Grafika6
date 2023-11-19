import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class DrawingPanel extends JPanel {

    private final ArrayList<Point> controlPoints;

    public DrawingPanel(ArrayList<Point> controlPoints) {
        this.controlPoints = controlPoints;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawBezierCurve(g);
        drawControlPoints(g);
    }

    private void drawControlPoints(Graphics g) {
        for (Point p : controlPoints) {
            g.fillOval(p.x - 3, p.y - 3, 6, 6);
        }
    }

    private void drawBezierCurve(Graphics g) {
        if (controlPoints.size() < 2) {
            return;
        }

        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.BLACK);

        Point2D.Float prevPoint = new Point2D.Float(controlPoints.get(0).x, controlPoints.get(0).y);

        for (float t = 0; t <= 1; t+= 0.01) {
            Point2D.Float currentPoint = calculateBezierPoint(t);
            g2d.drawLine((int) prevPoint.x, (int) prevPoint.y, (int) currentPoint.x, (int) currentPoint.y);
            prevPoint = currentPoint;
        }
    }

    private Point2D.Float calculateBezierPoint(float t) {
        int n = controlPoints.size() - 1;
        float x = 0;
        float y = 0;
        for (int i = 0; i <= n; i++) {
            float binomialCoefficient = calculateBinomialCoefficient(n, i);
            float a = (float) Math.pow(1 - t, n - i);
            float b = (float) Math.pow(t, i);
            x += binomialCoefficient * a * b * controlPoints.get(i).x;
            y += binomialCoefficient * a * b * controlPoints.get(i).y;
        }

        return new Point2D.Float(x, y);
    }

    private float calculateBinomialCoefficient(int n, int k) {
        float result = 1;
        for (int i = 1; i <=k; i++) {
            result *= (float) (n - (k - i)) / i;
        }
        return result;
    }
}
