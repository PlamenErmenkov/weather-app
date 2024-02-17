import javax.swing.*;
import java.awt.*;

public class Gui extends JFrame {
    public Gui() {
        super("Weather App");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(450, 650);
        setLocationRelativeTo(null);
        setLayout(null);
        setResizable(false);

        addGuiComponents();
    }

    private void addGuiComponents() {
        addSearchTextField();

        JButton searchButton = new JButton(loadImage("src/assets/search.png"));
    }

    private void addSearchTextField() {
        JTextField searchTextField = new JTextField();
        searchTextField.setBounds(15, 15, 351, 45);
        searchTextField.setFont(new Font("Dialog", Font.PLAIN, 24));
        add(searchTextField);

    }

    private ImageIcon loadImage(String path) {

    }
}
