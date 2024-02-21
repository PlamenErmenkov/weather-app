import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class WeatherApp {
    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH':00'";

    public static JSONObject getWeatherData(String locationName) throws IllegalStateException{
        JSONArray locationData = getLocationData(locationName);

        String urlString = getUrlString(locationData);

        try {
            HttpURLConnection connection = fetchApiResponse(urlString);
            assert connection != null;
            if(connection.getResponseCode() != 200) {
                System.out.println("Error: Could not connect to the API");
                return null;
            }

            StringBuilder resultJson = new StringBuilder();
            Scanner reader = new Scanner(connection.getInputStream());

            while(reader.hasNext()) {
                resultJson.append(reader.nextLine());
            }

            reader.close();
            connection.disconnect();

            JSONParser parser = new JSONParser();
            JSONObject resultsJsonObj = (JSONObject) parser.parse(String.valueOf(resultJson));

            JSONObject hourly = (JSONObject) resultsJsonObj.get("hourly");
            JSONArray time = (JSONArray) hourly.get("time");
            int index = findIndexOfCurrentTime(time);

            JSONArray temperatureData = (JSONArray) hourly.get("temperature_2m");
            double temperature = (double) temperatureData.get(index);

            JSONArray weathercode = (JSONArray) hourly.get("weather_code");
            String weatherCondition = convertWeatherCode((long) weathercode.get(index));

            JSONArray relativeHumidity = (JSONArray) hourly.get("relative_humidity_2m");
            long humidity = (long) relativeHumidity.get(index);

            JSONArray windspeedData = (JSONArray) hourly.get("wind_speed_10m");
            double windspeed = (double) windspeedData.get(index);

            JSONObject weatherData = new JSONObject();
            weatherData.put("temperature", temperature);
            weatherData.put("weatherCondition", weatherCondition);
            weatherData.put("humidity", humidity);
            weatherData.put("windspeed", windspeed);

            return weatherData;
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static String getUrlString(JSONArray locationData) throws IllegalStateException{
        if (locationData == null || locationData.isEmpty())
            throw new IllegalStateException("Location data is null or empty");
        JSONObject location = (JSONObject) locationData.getFirst();
        double latitude = (double) location.get("latitude");
        double longitude = (double) location.get("longitude");

        return "https://api.open-meteo.com/v1/forecast?latitude="
                + latitude + "&longitude="
                + longitude
                + "&hourly=temperature_2m,relative_humidity_2m,weather_code,wind_speed_10m&timezone=auto";
    }

    public static JSONArray getLocationData(String locationName) {
        locationName = locationName.replaceAll(" ", "+");
        String urlString =
                "https://geocoding-api.open-meteo.com/v1/search?name=" +
                locationName +
                "&count=10&language=en&format=json";

        try {
            HttpURLConnection connection = fetchApiResponse(urlString);

            assert connection != null;
            if(connection.getResponseCode() != 200) {
                System.out.println("Error: Could not connect to the API");
                return null;
            }

            StringBuilder resultJson = readApiResponse(connection);
            connection.disconnect();

            JSONParser parser = new JSONParser();
            JSONObject resultsJsonObj = (JSONObject) parser.parse(String.valueOf(resultJson));

            return (JSONArray) resultsJsonObj.get("results");
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static StringBuilder readApiResponse(HttpURLConnection connection) throws IOException{
        StringBuilder resultJson = new StringBuilder();
        try (Scanner reader = new Scanner(connection.getInputStream())) {
            while (reader.hasNext()) {
                resultJson.append(reader.nextLine());
            }
        }

        return resultJson;
    }

    private static HttpURLConnection fetchApiResponse(String urlString) {
        try {
            URI url = new URI(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.toURL().openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            return connection;
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static int findIndexOfCurrentTime(JSONArray timeList) {
        String currentTime = getCurrentTime();
        for(int i = 0; i < timeList.size(); i++) {
            String time = (String) timeList.get(i);
            if(time.equalsIgnoreCase(currentTime)) return i;
        }
        return 0;
    }

    public static String getCurrentTime() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);

        return currentDateTime.format(formatter);
    }

    private static String convertWeatherCode(long weathercode) {
        String weatherCondition = "";
        if (weathercode == 0L) {
            weatherCondition = "Clear";
        } else if (weathercode <= 3L && weathercode > 0) {
            weatherCondition = "Cloudy";
        } else if ((weathercode >= 51L && weathercode <= 67L) || (weathercode >= 80L && weathercode <= 99L)) {
            weatherCondition = "Rain";
        } else if (weathercode >= 71L && weathercode <= 77L) {
            weatherCondition = "Snow";
        }

        return weatherCondition;
    }
}
