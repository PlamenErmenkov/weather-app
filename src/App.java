import javax.swing.*;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Gui().setVisible(true);

                //System.out.println(WeatherApp.getLocationData("Warsaw"));
                //System.out.println(WeatherApp.getCurrentTime());
            }
        });
    }
}