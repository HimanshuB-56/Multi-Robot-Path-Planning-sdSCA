import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class SimulationPanel extends JPanel {

    Robot[] robots;
    Obstacle[] obstacles;

    int scale = 6;

    // =====================================
    // ROBOT TRAILS
    // =====================================
    ArrayList<Point>[] trails;

    public SimulationPanel(
            Robot[] robots,
            Obstacle[] obstacles
    ) {

        this.robots = robots;
        this.obstacles = obstacles;

        setPreferredSize(
                new Dimension(700, 700)
        );

        setBackground(new Color(245, 245, 245));

        // =====================================
        // INITIALIZE TRAILS
        // =====================================
        trails = new ArrayList[robots.length];

        for (int i = 0; i < robots.length; i++) {

            trails[i] = new ArrayList<>();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        // =====================================
        // ANTIALIASING
        // =====================================
        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

        // =====================================
        // DRAW GRID
        // =====================================
        // =====================================
// DRAW GRID + COORDINATES
// =====================================
        g2.setColor(new Color(220, 220, 220));

        for (int i = 0; i <= 700; i += 60) {

            // Vertical grid lines
            g2.drawLine(i, 0, i, 700);

            // Horizontal grid lines
            g2.drawLine(0, i, 700, i);

            // =====================================
            // DRAW COORDINATE LABELS
            // =====================================
            g2.setColor(Color.GRAY);

            // X-axis labels
            g2.drawString(
                    String.valueOf(i / scale),
                    i + 2,
                    12
            );

            // Y-axis labels
            g2.drawString(
                    String.valueOf(i / scale),
                    2,
                    i + 12
            );

            // Reset grid color
            g2.setColor(new Color(220, 220, 220));
        }

        // =====================================
        // DRAW OBSTACLES
        // =====================================
        g2.setColor(new Color(200, 50, 50));

        for (Obstacle obstacle : obstacles) {

            int diameter =
                    (int)(obstacle.radius * 2 * scale);

            g2.fillOval(
                    (int)((obstacle.x - obstacle.radius) * scale),
                    (int)((obstacle.y - obstacle.radius) * scale),
                    diameter,
                    diameter
            );
        }

        // =====================================
// DRAW START POSITIONS
// =====================================
        for (int i = 0; i < robots.length; i++) {

            if (i == 0) {
                g2.setColor(new Color(0, 120, 255));
            }
            else {
                g2.setColor(new Color(255, 140, 0));
            }

            int sx = (int)(robots[i].startX * scale);
            int sy = (int)(robots[i].startY * scale);

            // Small start marker
            g2.fillOval(
                    sx - 5,
                    sy - 5,
                    10,
                    10
            );

            // Label
            g2.setColor(Color.BLACK);

            g2.drawString(
                    "S" + (i + 1),
                    sx + 8,
                    sy
            );
        }
        // =====================================
        // DRAW GOALS
        // =====================================
        for (int i = 0; i < robots.length; i++) {

            if (i == 0) {
                g2.setColor(new Color(0, 180, 0));
            }
            else {
                g2.setColor(new Color(180, 0, 180));
            }

            int goalSize = 14;

            int gx = (int)(robots[i].goalX * scale);
            int gy = (int)(robots[i].goalY * scale);

            g2.fillOval(
                    gx,
                    gy,
                    goalSize,
                    goalSize
            );

            // Goal label
            g2.setColor(Color.BLACK);

            g2.drawString(
                    "G" + (i + 1),
                    gx + 15,
                    gy + 10
            );
        }

        // =====================================
        // STORE TRAIL POINTS
        // =====================================
        for (int i = 0; i < robots.length; i++) {

            trails[i].add(
                    new Point(
                            (int)(robots[i].x * scale),
                            (int)(robots[i].y * scale)
                    )
            );
        }

        // =====================================
        // DRAW TRAILS
        // =====================================
        for (int i = 0; i < robots.length; i++) {

            if (i == 0) {
                g2.setColor(new Color(100, 149, 237));
            }
            else {
                g2.setColor(new Color(255, 140, 0));
            }

            for (Point p : trails[i]) {

                g2.fillOval(
                        p.x,
                        p.y,
                        4,
                        4
                );
            }
        }

        // =====================================
        // DRAW ROBOTS
        // =====================================
        for (int i = 0; i < robots.length; i++) {

            if (i == 0) {
                g2.setColor(new Color(30, 144, 255));
            }
            else {
                g2.setColor(new Color(255, 140, 0));
            }

            int diameter =
                    (int)(robots[i].radius * 2 * scale);

            int rx =
                    (int)((robots[i].x - robots[i].radius) * scale);

            int ry =
                    (int)((robots[i].y - robots[i].radius) * scale);

            g2.fillOval(
                    rx,
                    ry,
                    diameter,
                    diameter
            );

            // =====================================
            // ROBOT LABEL
            // =====================================
            g2.setColor(Color.BLACK);

            g2.drawString(
                    "R" + (i + 1),
                    rx + 5,
                    ry - 5
            );
        }
    }
}