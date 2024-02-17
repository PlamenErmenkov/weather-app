import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

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
        addSearchButton();

        // TODO: make the method for choosing the right weather condition image
        JLabel weatherConditionImage = new JLabel(loadImage("src/assets/sunny.png"));
        weatherConditionImage.setBounds(0, 125, 450, 217);
        add(weatherConditionImage);

        // TODO: make the method for choosing the right weather temperature
        JLabel temperatureText = new JLabel("25 C");
        temperatureText.setBounds(0, 350, 450, 54);
        temperatureText.setFont(new Font("Dialog", Font.BOLD, 48));
        temperatureText.setHorizontalAlignment(SwingConstants.CENTER);
        add(temperatureText);

        // TODO: make the method for choosing the right weather condition
        JLabel weatherCondition = new JLabel("Sunny");
        weatherCondition.setBounds(0, 405, 450, 36);
        weatherCondition.setFont(new Font("Dialog", Font.PLAIN, 32));
        weatherCondition.setHorizontalAlignment(SwingConstants.CENTER);
        add(weatherCondition);

        addHumidityImage();

        // TODO: make the method for choosing the right humidity
        JLabel humidityText = new JLabel("<html><b>Humidity</b> 100%</html");
        humidityText.setBounds(90, 500, 85, 55);
        humidityText.setFont(new Font("Dialog", Font.PLAIN, 16));
        add(humidityText);

        addWindSpeedImage();

        // TODO: make the method for choosing the right windspeed
        JLabel windSpeedText = new JLabel("<html><b>Windspeed</b> 7km/h</html");
        windSpeedText.setBounds(310, 500, 85, 55);
        windSpeedText.setFont(new Font("Dialog", Font.PLAIN, 16));
        add(windSpeedText);
    }

    private void addSearchTextField() {
        JTextField searchTextField = new JTextField();
        searchTextField.setBounds(15, 15, 351, 45);
        searchTextField.setFont(new Font("Dialog", Font.PLAIN, 24));
        add(searchTextField);

    }

    private void addSearchButton() {
        JButton searchButton = new JButton(loadImage("src/assets/search.png"));
        searchButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        searchButton.setBounds(375, 13, 47, 45);
        add(searchButton);
    }

    private ImageIcon loadImage(String path) {
        try {
            BufferedImage image = ImageIO.read(new File(path));
            return new ImageIcon(image);
        } catch (IOException e) {
            System.out.println("Could not find image!");
            return null;
        }
    }

    private void addHumidityImage() {
        JLabel humidityImage = new JLabel(loadImage("src/assets/humidity.png"));
        humidityImage.setBounds(15, 500, 74, 66);
        add(humidityImage);
    }

    private void addWindSpeedImage() {
        JLabel windSpeedImage = new JLabel(loadImage("src/assets/windspeed.png"));
        windSpeedImage.setBounds(220, 500, 74, 66);
        add(windSpeedImage);
    }
}
