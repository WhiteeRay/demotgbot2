package repositories;

import bot.RayWeatherBot;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import db.CityDatabase;
import models.City;


import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;


public class WeatherRepository {
    public static final HashMap<String, JsonNode> weatherCache = new HashMap<>();


    public static void getWeather(Long chatId, String token, String placeId, RayWeatherBot bot, String city_name) {

        StringBuilder weatherMessage = new StringBuilder("\uD83C\uDF24Here is " +city_name  +"'s forecast for the next 7 days :\n");


        String url = "https://www.meteosource.com/api/v1/free/point?" +
                "key=" + token + "&place_id=" + placeId + "&sections=daily";

        try {
            HttpRequest request = HttpRequest.newBuilder().uri(new URI(url)).GET().build();
            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(response.body());
            JsonNode daily = jsonNode.get("daily");

            int i = 1;
            for (JsonNode data : daily.get("data")) {
                weatherMessage.append(data.get("day").asText()).append(": ").append(data.get("summary").asText()).append("\n");
            }

            bot.sendMessage(chatId, weatherMessage.toString());
            weatherCache.put(chatId.toString(), daily);
        } catch (Exception e) {
            e.printStackTrace();
            bot.sendMessage(chatId, "Error accessing the weather");
        }
    }

}


