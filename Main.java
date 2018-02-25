import javax.swing.*;
import java.awt.*;

public class Main {

    public static void main(String[] args) {

        JFrame frame = new JFrame();

        frame.setTitle("Gravity project");
        frame.setSize(new Dimension(1000, 1000));
        frame.setLocationRelativeTo(null);
        frame.setLayout(new GridBagLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        GraphicsPanel graphicsPanel = new GraphicsPanel();

        frame.add(graphicsPanel, new GridBagConstraints(
            0, 0, 1, 1, 1, 1,
            GridBagConstraints.NORTH, GridBagConstraints.BOTH,
            new Insets(2, 2,2, 2), 0, 0
        ));

        frame.setVisible(true);
    }
}
