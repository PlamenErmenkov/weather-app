import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Scanner;

public class WeatherApp {
    public static JSONObject getWeatherData(String locationName) {
        JSONArray locationData = getLocationData(locationName);

        JSONObject location = (JSONObject) locationData.getFirst();
        double latitude = (double) location.get("latitude");
        double longitude = (double) location.get("longitude");

        String urlString =
                "https://api.open-meteo.com/v1/forecast?latitude="
                        + latitude + "&longitude="
                        + longitude
                        + "&hourly=temperature_2m,relative_humidity_2m,weather_code,wind_speed_10m&timezone=auto";

        try {
            HttpURLConnection connection = fetchApiResponse(urlString);
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
            //int index = findIndexOfCurrentTime(time);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static JSONArray getLocationData(String locationName) {
        locationName = locationName.replaceAll(" ", "+");
        String urlString =
                "https://geocoding-api.open-meteo.com/v1/search?name=" +
                locationName +
                "&count=10&language=en&format=json";

        try {
            HttpURLConnection connection = fetchApiResponse(urlString);

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

            JSONArray locationData = (JSONArray) resultsJsonObj.get("results");
            return locationData;
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static HttpURLConnection fetchApiResponse(String urlString) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            return connection;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

//    private static int findIndexOfCurrentTime(JSONArray timeList) {
//        String currentTime = getCurrentTime();
//    }

//    private static String getCurrentTime() {
//        LocalDateTime currentDateTime = LocalDateTime.now();
//    }


}
