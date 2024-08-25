package part2.simulationframework.simtrafficlauncher;

import part2.simulationframework.common.AbstractEnvironment;
import part2.simulationframework.common.AbstractSimulation;
import part2.simulationframework.contracts.InputListener;
import part2.simulationframework.contracts.SimulationListener;
import part2.simulationframework.simtrafficbase.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class RoadSimView extends JFrame {

    private RoadSimViewPanel panel;
    private static final int CAR_DRAW_SIZE = 10;

    private JTextField numStep;

    private JButton startButton;
    private JButton stopButton;

    private int steps;

    private ArrayList<InputListener> listeners;

    public RoadSimView(int steps) {
        super("RoadSim View");
        listeners = new ArrayList<>();
        this.steps = steps;

        setSize(1500, 600);

        panel = new RoadSimViewPanel(1500, 600);
        panel.setSize(1500, 600);

        numStep = new JTextField("" + this.steps);
        startButton = new JButton("Start");
        stopButton = new JButton("Stop");

        startButton.addActionListener(e -> {
            for (InputListener i : listeners) {
                try {
                    this.steps = Integer.parseInt(this.numStep.getText());
                    i.start(this.steps);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        stopButton.addActionListener(e -> {
            for (InputListener i : listeners) {
                i.stop();
            }
        });

        JPanel inputPanel = new JPanel();
        inputPanel.add(numStep);
        inputPanel.add(startButton);
        inputPanel.add(stopButton);
        inputPanel.add(Box.createRigidArea(new Dimension(50, 50)));

        JPanel cp = new JPanel();
        LayoutManager layout = new BorderLayout();
        cp.setLayout(layout);
        cp.add(BorderLayout.NORTH, inputPanel);
        cp.add(BorderLayout.CENTER, panel);
        setContentPane(cp);

        setDefaultCloseOperation(EXIT_ON_CLOSE);

    }

    public void display() {
        SwingUtilities.invokeLater(() -> {
            this.setVisible(true);
        });
    }

    public void notifyInit(int t, List<CarInfo> agents, AbstractEnvironment env) {
        //nop
    }


    public void notifyStepDone(AbstractEnvironment env) {
        var e = ((RoadsEnv) env);
        panel.update(e.getRoads(), e.getAgentInfo(), e.getTrafficLights());
    }

    public void addListener(Controller controller) {
        listeners.add(controller);
    }

    public void done(AbstractEnvironment env) {
        notifyStepDone(env);
        JOptionPane.showMessageDialog(this, "Simulation Complete!");
        for (InputListener i : listeners) {
            i.stop();
        }
    }


    class RoadSimViewPanel extends JPanel {

        List<CarInfo> cars;
        List<Road> roads;
        List<TrafficLight> sems;

        public RoadSimViewPanel(int w, int h) {
        }

        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g2.clearRect(0, 0, this.getWidth(), this.getHeight());

            if (roads != null) {
                for (var r : roads) {
                    g2.drawLine((int) r.getFrom().x(), (int) r.getFrom().y(), (int) r.getTo().x(), (int) r.getTo().y());
                }
            }

            if (sems != null) {
                for (var s : sems) {
                    if (s.isGreen()) {
                        g.setColor(new Color(0, 255, 0, 255));
                    } else if (s.isRed()) {
                        g.setColor(new Color(255, 0, 0, 255));
                    } else {
                        g.setColor(new Color(255, 255, 0, 255));
                    }
                    g2.fillRect((int) (s.getPos().x() - 5), (int) (s.getPos().y() - 5), 10, 10);
                }
            }

            g.setColor(new Color(0, 0, 0, 255));

            if (cars != null) {
                for (var c : cars) {
                    double pos = c.getPos();
                    Road r = c.getRoad();
                    //c.updateCurrentPos();
                    V2d dir = V2d.makeV2d(r.getFrom(), r.getTo()).getNormalized().mul(pos);
                    g2.drawOval((int) (r.getFrom().x() + dir.x() - CAR_DRAW_SIZE / 2), (int) (r.getFrom().y() + dir.y() - CAR_DRAW_SIZE / 2), CAR_DRAW_SIZE, CAR_DRAW_SIZE);
                }
            }
        }

        public void update(List<Road> roads,
                           List<CarInfo> cars,
                           List<TrafficLight> sems) {
            SwingUtilities.invokeLater(() -> {
                this.roads = roads;
                this.cars = cars;
                this.sems = sems;
                repaint();
            });

        }
    }
}

