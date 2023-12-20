import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.sql.Connection;

public class telegramBotNewsletter extends telegramBotConfigure {

    telegramBotDatabase db = new telegramBotDatabase();
    Connection conn = db.connectToDatabase("bot_users", "postgres", "1234");

    public void notofication(int eventEmpid, String text) {
        String[] eventInfo = db.viewRowEvent(conn, "event", eventEmpid);
        String[] member = eventInfo[7].split(" ");

        if (member != null) {
            for (int i = 0; i < member.length; i++) {
                String[] userInfo = db.viewRowUsers(conn,"users","empid",member[i]);
                String chatId = userInfo[5];

                SendMessage message = new SendMessage();
                message.setChatId(chatId);
                message.setText(eventInfo[1]+text);
                try {
                    execute(message);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
