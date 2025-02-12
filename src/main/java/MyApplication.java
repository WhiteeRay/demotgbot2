import bot.RayWeatherBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class MyApplication {
    public static void main(String[] args) {
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(new RayWeatherBot());
            System.out.println("Bot started successfully!");
        } catch (TelegramApiException e) {
            System.err.println("Error starting bot: " + e.getMessage());
            e.printStackTrace();
        }
    }
}



