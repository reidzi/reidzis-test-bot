package tk.reidzi;

import org.junit.Before;
import org.junit.Test;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import static org.junit.Assert.*;

public class MySuperBotTest {
    private Update update = new Update();
    private MySuperBot mySuperBot;
    @Before
    public void newBot(){
        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            mySuperBot = new MySuperBot();
            telegramBotsApi.registerBot(mySuperBot);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    public void getBotUsername() {
        assertEquals (mySuperBot.getBotUsername(), "reidzis_test_bot");
    }

    //111


    @Test
    public void onUpdateReceived() {

    }
}