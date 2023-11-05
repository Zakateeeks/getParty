import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

public class telegramFSM {

    private telegramBotHandlers handler = new telegramBotHandlers();
    private telegramBotDatabase db = new telegramBotDatabase();
    private Connection conn = db.connectToDatabase("bot_users", "postgres", "1234");
    private static String currentState = "name";
    public static String currentDB = "user";
    public Map<String,String> switchState = new HashMap<>();

    public telegramFSM() {
        switchState.put("name","description");
        switchState.put("description","menu");
        switchState.put("eventname","eventdesc");
        switchState.put("eventdesc","eventdate");
        switchState.put("eventdate","eventref");
        switchState.put("eventref","complete");
    }

    public String getState(){
        return currentState;
    }

    public void selectInfo(Long chatID,String column,String text){
        db.update(conn,currentDB,column,text,chatID.toString());
        currentState = switchState.get(getState());
        System.out.println(currentState);
        handler.textCommand(chatID, getState()+'$');
    }

    public void changeState(String state){
        currentState = state;
    }


}