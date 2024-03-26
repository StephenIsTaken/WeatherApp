import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.*;

public class WeatherApp extends JFrame {
    private final JLabel tempLabel;
    private final JLabel descLabel;
    private final JLabel countryLabel;
    private final JLabel dateLabel;
    private final JLabel humidityLabel;
    private final JTextField cityField;
    private final JButton searchButton;

    public WeatherApp() {
        setTitle("Weather App");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(7, 1));

        JLabel cityLabel = new JLabel("Enter City:");
        cityField = new JTextField();
        tempLabel = new JLabel();
        descLabel = new JLabel();
        countryLabel = new JLabel();
        dateLabel = new JLabel();
        humidityLabel = new JLabel();
        searchButton = new JButton("Search");
        searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String city = cityField.getText();
                if (!city.isEmpty()) {
                    String apiKey = "YOUR_API_KEY"; // Replace with your own API key from https://www.weatherapi.com/
                    String url = "http://api.weatherapi.com/v1/current.json?key=" + apiKey + "&q=" + city + "&aqi=no";

                    try {
                        JSONObject json = getWeatherData(url);
                        JSONObject location = json.getJSONObject("location");
                        JSONObject current = json.getJSONObject("current");

                        double tempCelsius = current.getDouble("temp_c");
                        String description = current.getJSONObject("condition").getString("text");
                        String country = location.getString("country");
                        String date = current.getString("last_updated");
                        int humidity = current.getInt("humidity");

                        tempLabel.setText("Temperature: " + String.format("%.2f", tempCelsius) + "°C");
                        descLabel.setText("Description: " + description);
                        countryLabel.setText("Country: " + country);
                        dateLabel.setText("Date: " + date);
                        humidityLabel.setText("Humidity: " + humidity + "%");
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(null, "Error fetching weather data: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    } catch (JSONException ex) {
                        JOptionPane.showMessageDialog(null, "Error parsing weather data: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Please enter a city name.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        add(cityLabel);
        add(cityField);
        add(searchButton);
        add(tempLabel);
        add(descLabel);
        add(countryLabel);
        add(dateLabel);
        add(humidityLabel);

        setVisible(true);
    }

    private JSONObject getWeatherData(String url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod("GET");
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();
        connection.disconnect();
        return new JSONObject(response.toString());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new WeatherApp();
            }
        });
    }
}
