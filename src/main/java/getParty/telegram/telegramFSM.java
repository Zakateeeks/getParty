import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

public class telegramFSM {

    private telegramBotHandlers handler = new telegramBotHandlers();
    private telegramBotDatabase db = new telegramBotDatabase();
    private Connection conn = db.connectToDatabase("bot_users", "postgres", "1234");
    private static String currentState = "name";
    public Map<String,String> switchState = new HashMap<>();

    public telegramFSM() {
        switchState.put("name","description");
        switchState.put("description","menu");
    }

    public String getState(){
        return currentState;
    }

    public void selectInfo(Long chatID,String column,String text){
        db.update(conn,"users",column,text,chatID.toString());
        currentState = switchState.get(getState());
        System.out.println(currentState);
        handler.textCommand(chatID, getState()+'$');

    }


}
