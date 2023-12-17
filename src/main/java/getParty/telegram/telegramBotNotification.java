import java.sql.Connection;
import java.sql.SQLException;

import static java.lang.Integer.parseInt;

public class telegramBotNotification implements Runnable {
    private telegramBotConfigure bot;
    private telegramBotDatabase db = new telegramBotDatabase();
    private Connection conn = db.connectToDatabase("bot_users", "postgres", "1234");

    public telegramBotNotification(telegramBotConfigure bot) {
        this.bot = bot;
    }

    @Override
    public void run() {
        while (true) {
            try {
                startEvent();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void startEvent() throws SQLException {
        String date;
        int countEvents = db.countRow(conn, "event");
        for (int i = 1; i <= countEvents; i++) {
            String[] thisEvent = db.viewRowEvent(conn, "event", i);
            date = thisEvent[5];
            if (thisEvent[8].equals("true")) {
                if (date != null) {

                    java.time.LocalDate nowDate = java.time.LocalDate.now();
                    java.time.LocalTime nowTime = java.time.LocalTime.now();

                    String[] nowDateSpl = nowDate.toString().split("-");
                    String nowHour = nowTime.toString().split(":")[0];

                    String[] evDateTime = date.split(" ");
                    String[] evDate = evDateTime[0].split("\\D");
                    String eventHour = evDateTime[1].split(":")[0];


                    if ((nowDateSpl[0].equals(evDate[2])) && (nowDateSpl[1].equals(evDate[1])) && (nowDateSpl[2].equals(evDate[0]))) {
                        if ((parseInt(eventHour) - parseInt(nowHour) < 5) && (parseInt(eventHour) - parseInt(nowHour) > 0)) {
                            String users = thisEvent[7];
                            if (users != null) {
                                String[] empidUsers = users.split(" ");
                                for (int j = 0; j < empidUsers.length; j++) {
                                    String[] infoUsers = db.viewRowUsers(conn, "users", "empid", empidUsers[j]);
                                    if (infoUsers != null) {
                                        System.out.println(infoUsers[2]);
                                        String chatId = infoUsers[4];
                                        String message = "У тебя сегодня " + thisEvent[1] + " в " + evDateTime[1];
                                        bot.sendNotification(chatId, message);
                                    }
                                }
                                String orgChatId = thisEvent[3];
                                db.changeNotState(conn,i);
                            }
                        }
                    }
                }
            }
        }
    }
}
