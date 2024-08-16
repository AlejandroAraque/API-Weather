import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class App extends JFrame {
    private JLabel cityLabel;
    private JLabel timeLabel;
    private JLabel temperatureLabel;
    private JLabel maxTemperatureLabel;
    private JLabel minTemperatureLabel;
    private JLabel feelsLikeLabel;
    private JLabel pressureLabel;
    private JLabel humidityLabel;
    private JComboBox<String> locationComboBox;
    private JLabel[] dayLabels;
    private JLabel[] tempLabels;
    private JButton showForecastButton;

    String location = null;

    public App() {
        setTitle("Weather App");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initializeUI();
    }

    private void initializeUI() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(10, 2, 10, 5));
        panel.setBackground(new Color(240, 240, 240));

        Font labelFont = new Font("Arial", Font.PLAIN, 14);

        String[] locations = { "Valencia", "Maribor" };
        locationComboBox = new JComboBox<>(locations);
        locationComboBox.addActionListener(e -> {
            location = locationComboBox.getSelectedItem().toString();
            updateWeatherInfo(); // Actualizar la información del clima al cambiar la ubicación
        });

        cityLabel = createLabel("City: ", labelFont);
        timeLabel = createLabel("Time: ", labelFont);
        temperatureLabel = createLabel("Temperature: ", labelFont);
        maxTemperatureLabel = createLabel("Max Temperature: ", labelFont);
        minTemperatureLabel = createLabel("Min Temperature: ", labelFont);
        feelsLikeLabel = createLabel("Feels Like: ", labelFont);
        pressureLabel = createLabel("Pressure: ", labelFont);
        humidityLabel = createLabel("Humidity: ", labelFont);

        panel.add(new JLabel("Select Location:"));
        panel.add(locationComboBox);
        panel.add(cityLabel);
        panel.add(timeLabel);
        panel.add(temperatureLabel);
        panel.add(maxTemperatureLabel);
        panel.add(minTemperatureLabel);
        panel.add(feelsLikeLabel);
        panel.add(pressureLabel);
        panel.add(humidityLabel);

        showForecastButton = new JButton("Show Forecast");
        panel.add(showForecastButton);

        dayLabels = new JLabel[5];
        tempLabels = new JLabel[5];

        for (int i = 0; i < 5; i++) {
            dayLabels[i] = createLabel("Day " + (i + 1) + ": ", labelFont);
            tempLabels[i] = createLabel("Temp: ", labelFont);
            panel.add(dayLabels[i]);
            panel.add(tempLabels[i]);
            dayLabels[i].setVisible(false);
            tempLabels[i].setVisible(false);
        }

        showForecastButton.addActionListener(e -> toggleForecastVisibility());

        add(panel);
        updateWeatherInfo(); // Actualiza la información (ficticia) del clima
    }

    private JLabel createLabel(String text, Font font) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        label.setForeground(new Color(50, 50, 50));
        return label;
    }

    private void toggleForecastVisibility() {
        boolean currentVisibility = dayLabels[0].isVisible();

        for (int i = 0; i < 5; i++) {
            dayLabels[i].setVisible(!currentVisibility);
            tempLabels[i].setVisible(!currentVisibility);
        }
    }

    private void updateWeatherInfo() {
        // Simular la obtención de datos del clima
        String time = new SimpleDateFormat("HH:mm:ss").format(new Date());
        cityLabel.setText("<html><b>City:</b> " + location + "</html>");
        timeLabel.setText("<html><b>Time:</b> " + time + "</html>");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }

            App weatherAppUI = new App();
            weatherAppUI.setVisible(true);
        });
    }
}
