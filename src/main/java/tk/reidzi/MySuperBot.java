package tk.reidzi;

import com.vdurmont.emoji.EmojiParser;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
//import org.apache.log4j.Logger;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;



import java.io.File;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;

public class MySuperBot extends TelegramLongPollingBot {
    // мой айди 648370032



//    final int RECONNECT_PAUSE = 10000;

    private static final String token = Main.getToken();
    private static final String username = Main.getUsername();
    private static final String pathtobackup = Main.getPathtobackup();
    private static final long ownerid = Main.getOwnerid();

    @Override
    public String getBotUsername() {
        return username;
    }

    @Override
    public String getBotToken() {
        return token;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.getMessage() != null && update.getMessage().hasText()) {

            try {
                if (update.getMessage().getChatId() != ownerid) {
                    execute(new SendMessage(String.valueOf(update.getMessage().getChatId()), "Вам тут не рады"));
                    execute(new SendMessage(String.valueOf(ownerid), "Кто-то ломится"));
                }

                String ansver = "";

                switch (update.getMessage().getText().toUpperCase()) {
                    case ("ФАЙЛЫ") -> {
                        ArrayList<String> list = new ArrayList<>();
                        for (File file : getFiles(pathtobackup)) {
                            list.add(file.getName());
                        }
                        execute(new SendMessage(String.valueOf(ownerid), list.toString()));
                    }
                    case ("ЧТО") -> execute(new SendMessage(String.valueOf(ownerid), "ничего"));
                    case ("БЭКАПЫ") -> {
                        execute(new SendMessage(String.valueOf(ownerid), "Файлы, созданные сегодня:"));
                        ArrayList<File> files = getTodayModified();
                        for (File file : files) {
                            execute(new SendMessage(String.valueOf(ownerid), EmojiParser.parseToUnicode(":floppy_disk:")
                                    + " Имя файла " + file.getName()
                                    + " размер " + file.length() / 1024 / 1024 + " мегабайт"));
                        }
                    }
                    case ("RSTAT") -> {
                        ArrayList<String> data = RstatConnect.getSensorsToday();
                        execute(new SendMessage(String.valueOf(ownerid), "Данные по счетчикам" + String.join(", ", data)));
                    }
                    default -> execute(new SendMessage(String.valueOf(ownerid), "непонял ничего"));
                }
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }



    {TimerTask timerTask = new TimerTask() {

        @Override
        public void run() {
            LocalDateTime now = LocalDateTime.now();

            if(now.getHour() == 10){
                int countOfFiles = 0;
                ArrayList<String> filenames = new ArrayList<>();
                ArrayList<File> files = getTodayModified();
                for (File file : files) {
                    if (file.length() / 1024 / 1024 >100) {
                        countOfFiles++;
                        filenames.add("Имя файла " + file.getName() + ", размер файла " + file.length() / 1024 / 1024 + " мегабайт\n");
                    }
                }


                try {
                    if (countOfFiles == 4) {
                        execute(new SendMessage(String.valueOf(ownerid), filenames.toString()));
                        execute(new SendMessage(String.valueOf(ownerid), "Все бэкапы на сегодня сделаны " + EmojiParser.parseToUnicode(":ok:")));
                    } else {
                        execute(new SendMessage(String.valueOf(ownerid), "Проблема с бэкапами " + EmojiParser.parseToUnicode(":error:")));
                    }

                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }

            }

        }
    };

    Timer timer = new Timer("MyTimer");//create a new Timer
        timer.scheduleAtFixedRate(timerTask, 60*60*1000, 60*60*1000);
    }


    private ArrayList<File> getFiles(String path) {
        ArrayList<File> list = new ArrayList<>();
        try {

            list = (ArrayList<File>) Files.walk(Paths.get(path))
                    .filter(Files::isRegularFile)
                    .map(Path::toFile)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    private ArrayList<File> getTodayModified() {
        ArrayList<File> result = new ArrayList<>();
        try {
            ArrayList<File> files = getFiles(pathtobackup);
            for (File file : files) {
                BasicFileAttributes bfatr = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
                LocalDateTime lastModified = LocalDateTime.ofInstant(bfatr.lastModifiedTime().toInstant(), ZoneId.systemDefault());
                if (lastModified.isAfter(LocalDateTime.now().minusDays(1))) {
                    System.out.println(file.getName() + " " + lastModified + " " + LocalDateTime.now());
                    result.add(file);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


}
