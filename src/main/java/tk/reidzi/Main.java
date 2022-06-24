package tk.reidzi;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

public class Main {
    private static String token;
    private static String username;
    private static String pathtobackup;
    private static long ownerid;
    static final String dbpath = "D:/rstat";
    public static void main(String[] args) throws TelegramApiException {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            Document document = builder.parse(new File("settings.xml"));
            Element element = document.getDocumentElement();
            NodeList nodeList = element.getChildNodes();
            for (int i = 0; i < nodeList.getLength(); i++) {
                switch (nodeList.item(i).getNodeName()) {
                    case("ownerid") -> ownerid = Long.valueOf(nodeList.item(i).getTextContent());
                    case ("token") -> token = nodeList.item(i).getTextContent();
                    case ("username") -> username = nodeList.item(i).getTextContent();
                    case ("pathtobackup") -> pathtobackup = nodeList.item(i).getTextContent();
                    default -> System.out.println("none");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        MySuperBot mySuperBot = new MySuperBot();
        try {
            telegramBotsApi.registerBot(mySuperBot);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static String getToken() {
        return token;
    }
    public static String getUsername() {
        return username;
    }
    public static String getPathtobackup(){
        return pathtobackup;
    }
    public static long getOwnerid() {return ownerid;}
}