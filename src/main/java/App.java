import javax.swing.*;
import org.json.JSONArray;
import org.json.JSONObject;
import java.awt.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
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
    Double latitude;
    Double longitude;

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

    private void getCoordinates(String city) {
        location = city;

        String url = "http://api.openweathermap.org/geo/1.0/direct?q=" + location + "&limit=1&appid=4ac43d201d238cf4749262c4f4ed588f";
        HttpClient http = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        try {
            HttpResponse<String> response = http.send(request, HttpResponse.BodyHandlers.ofString());

            JSONArray json = new JSONArray(response.body());

            if (json.length() > 0) {
                JSONObject cityObject = json.getJSONObject(0);

                latitude = cityObject.getDouble("lat");
                longitude = cityObject.getDouble("lon");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateWeatherInfo() {
        String time = new SimpleDateFormat("HH:mm:ss").format(new Date());

        getCoordinates(location);

        String url = "http://api.openweathermap.org/data/2.5/weather?lat=" + latitude + "&lon=" + longitude + "&appid=4ac43d201d238cf4749262c4f4ed588f&units=Metric";
        HttpClient http = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        try {
            HttpResponse<String> response = http.send(request, HttpResponse.BodyHandlers.ofString());

            JSONObject json = new JSONObject(response.body());

            cityLabel.setText("<html><b>City:</b> " + location + "</html>");
            timeLabel.setText("<html><b>Time:</b> " + time + "</html>");
            temperatureLabel.setText("<html><b>Temperature:</b> " + json.getJSONObject("main").getFloat("temp") + " °C</html>");
            maxTemperatureLabel.setText("<html><b>Max Temperature:</b> " + json.getJSONObject("main").getFloat("temp_max") + " °C</html>");
            minTemperatureLabel.setText("<html><b>Min Temperature:</b> " + json.getJSONObject("main").getFloat("temp_min") + " °C</html>");
            feelsLikeLabel.setText("<html><b>Feels Like:</b> " + json.getJSONObject("main").getFloat("feels_like") + " °C</html>");
            pressureLabel.setText("<html><b>Pressure:</b> " + json.getJSONObject("main").getInt("pressure") + " hPa</html>");
            humidityLabel.setText("<html><b>Humidity:</b> " + json.getJSONObject("main").getInt("humidity") + " %</html>");
        } catch (Exception e) {
            e.printStackTrace();
        }
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
