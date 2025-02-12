package bot;



import models.City;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import db.CityDatabase;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import repositories.WeatherRepository;


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RayWeatherBot extends TelegramLongPollingBot {
    private final String bot_username = "RayWeather_bot";
    private final String bot_token = "7746132279:AAFKgNAtLNxeruqEu9IynrmH7B8YHa9UZWw";
    private final CityDatabase db = new CityDatabase();
    private final Map<Long, Boolean> waitingForCityId = new HashMap<>();



    public RayWeatherBot() {
        db.connect("jdbc:postgresql://localhost:5432/postgres", "postgres", "0000");

    }

    @Override
    public String getBotUsername() {
        return bot_username;
    }

    @Override
    public String getBotToken() {
        return bot_token;
    }


    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String text = update.getMessage().getText();
            Long chatId = update.getMessage().getChatId();

            if (waitingForCityId.getOrDefault(chatId, false)) {
                try {
                    int cityId = Integer.parseInt(text);
                    String placeId = db.getCityById(cityId);
                    String city_name = db.getCityNameById(cityId);

                    if (placeId != null) {
                        String token = "wj41qlc7ukds7tj5n2iisohhfxu843nhepoxmpz8";
                        sendWeatherOptions(chatId, token, placeId, this, city_name);
                    } else {
                        sendMessage(chatId, "\uD83D\uDE45\uD83C\uDFFB\u200D♀\uFE0FCity with this ID not found");
                    }
                } catch (NumberFormatException e) {
                    sendMessage(chatId, "\uD83D\uDE45\uD83C\uDFFB\u200D♀\uFE0FPlease enter the number");
                }

                waitingForCityId.put(chatId, false);
            }

            switch (text) {
                case "/start":
                    sendWelcomeMessage(chatId);
                    break;
                case "\uD83C\uDF03cities":
                    sendCitiesList(chatId);
                    break;
                case "\uD83C\uDF25\uFE0Fweather":
                    sendMessage(chatId, "Select ID for the city: ");
                    waitingForCityId.put(chatId, true);
                    break;
            }
        }
    }


    private void sendCitiesList(Long chatId) {
        try {
            ArrayList<City> cities = db.getAllCities();
            StringBuilder response = new StringBuilder("\uD83C\uDF03List of the cities:\n");
            for (City city : cities) {
                String text = city.getId() + ". " + city.getCity_name();
                response.append(text).append("\n");
            }
            sendMessage(chatId, response.toString());
        } catch (SQLException e) {
            sendMessage(chatId, "\uD83D\uDE45\uD83C\uDFFB\u200D♀\uFE0FError giving the list of the cities");
        }
    }

    public void sendMessage(Long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText(text);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void sendWelcomeMessage(Long chatId) {
        ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup();
        keyboard.setResizeKeyboard(true);

        KeyboardRow row = new KeyboardRow();
        row.add("\uD83C\uDF03cities");
        row.add("\uD83C\uDF25\uFE0Fweather");

        keyboard.setKeyboard(List.of(row));

        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText("Hi I am RayWeather. Select command");
        message.setReplyMarkup(keyboard);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }


    }

    private void sendWeatherOptions(Long chatId, String token, String placeId, RayWeatherBot bot, String city_name) {
        WeatherRepository.getWeather(chatId, token, placeId, bot, city_name);
    }

}



