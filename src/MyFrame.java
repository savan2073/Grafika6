import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class MyFrame extends JFrame {

    private final ArrayList<Point> controlPoints;
    private Point draggingPoint;
    private final DrawingPanel drawingPanel;
    private JSpinner degreeSpinner;
    private JButton addPointButton;
    private JCheckBox editModeCheckbox;

    public MyFrame() {
        setTitle("Projekt grafika komputerowa 6");
        setSize(1000, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        controlPoints = new ArrayList<>();
        drawingPanel = new DrawingPanel(controlPoints);
        add(drawingPanel, BorderLayout.CENTER);

        setupDegreeControl();
        setupInteractivity();
        addControlPanel();

    }

    private void setupInteractivity() {
        MouseAdapter mouseAdapter = new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (editModeCheckbox.isSelected()) {
                    for (Point point : controlPoints) {
                        if (point.distance(e.getPoint()) < 10) {
                            editPoint(point);
                            break;
                        }
                    }
                }else {
                    controlPoints.add(new Point(e.getX(), e.getY()));
                    drawingPanel.repaint();
                }
            }
            @Override
            public void mousePressed(MouseEvent e) {
                for (Point point : controlPoints) {
                    if (point.distance(e.getPoint()) < 10) {
                        draggingPoint = point;
                        break;
                    }
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                draggingPoint = null;
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (draggingPoint != null) {
                    draggingPoint.setLocation(e.getPoint());
                    repaint();
                }
            }
        };

        drawingPanel.addMouseListener(mouseAdapter);
        drawingPanel.addMouseMotionListener(mouseAdapter);
    }

    private void setupDegreeControl() {
        degreeSpinner = new JSpinner(new SpinnerNumberModel(3, 2, 10, 1));
        degreeSpinner.addChangeListener(e -> updateControlPoints());
        add(degreeSpinner, BorderLayout.NORTH);
    }

    private void updateControlPoints() {
        int newDegree = (int) degreeSpinner.getValue();
        while (controlPoints.size() > newDegree) {
            controlPoints.remove(controlPoints.size() - 1);
        }
        while (controlPoints.size() < newDegree) {
            controlPoints.add(new Point(100, 100));
        }
        drawingPanel.repaint();
    }

    private void addControlPanel() {
        JPanel controlPanel = new JPanel();

        // Przycisk do dodawania punktów
        addPointButton = new JButton("Dodaj punkt");
        addPointButton.addActionListener(e -> addNewPoint());
        controlPanel.add(addPointButton);

        // Checkbox do włączania trybu edycji
        editModeCheckbox = new JCheckBox("Edytuj");
        controlPanel.add(editModeCheckbox);

        add(controlPanel, BorderLayout.SOUTH);
    }

    private void addNewPoint() {
        JTextField xField = new JTextField(5);
        JTextField yField = new JTextField(5);

        JPanel panel = new JPanel();
        panel.add(new JLabel("x:"));
        panel.add(xField);
        panel.add(Box.createHorizontalStrut(15)); // Separator
        panel.add(new JLabel("y:"));
        panel.add(yField);

        int result = JOptionPane.showConfirmDialog(null, panel,
                "Parametry punktu", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                int x = Integer.parseInt(xField.getText());
                int y = Integer.parseInt(yField.getText());
                controlPoints.add(new Point(x, y));
                drawingPanel.repaint();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Invalid input: please enter numeric values.");
            }
        }
    }

    private void editPoint(Point point) {
        JTextField xField = new JTextField(String.valueOf(point.x), 5);
        JTextField yField = new JTextField(String.valueOf(point.y), 5);

        JPanel panel = new JPanel();
        panel.add(new JLabel("x:"));
        panel.add(xField);
        panel.add(Box.createHorizontalStrut(15)); // Separator
        panel.add(new JLabel("y:"));
        panel.add(yField);

        int result = JOptionPane.showConfirmDialog(null, panel,
                "Parametry punktu", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                int x = Integer.parseInt(xField.getText());
                int y = Integer.parseInt(yField.getText());
                point.setLocation(x, y);
                drawingPanel.repaint();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Invalid input");
            }
        }
    }
}
