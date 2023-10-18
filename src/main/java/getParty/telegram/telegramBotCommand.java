import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
public class telegramBotCommand {

    private final String pathTextFile = "src/main/JSONFiles/TextCommands.json";
    private String textToSend;
    private List<InlineKeyboardButton> buttons = new ArrayList<>();
    public telegramBotCommand(String command) {

        try (FileReader reader = new FileReader(pathTextFile)) {
            JsonParser jsonParser = new JsonParser();
            JsonObject jsonObject = jsonParser.parse(reader).getAsJsonObject();

            textToSend = jsonObject.getAsJsonObject(command).get("text").getAsString();

            if(jsonObject.getAsJsonObject(command).get("inlineButtons").getAsBoolean()){
                int buttonCount = jsonObject.getAsJsonObject(command).getAsJsonObject("buttons").get("count").getAsInt();

                for (int i = 1; i <= buttonCount; i++) {
                    String buttonText = jsonObject.getAsJsonObject(command).getAsJsonObject("buttons").getAsJsonObject("b" + i).get("butText").getAsString();
                    String callbackData = jsonObject.getAsJsonObject(command).getAsJsonObject("buttons").getAsJsonObject("b" + i).get("callBackData").getAsString();
                    InlineKeyboardButton but = new InlineKeyboardButton();
                    but.setText(buttonText);
                    but.setCallbackData(callbackData);
                    buttons.add(but);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public String getCommandText() {
        return textToSend;
    }

    public List getButtonsList(){
        return buttons;
    }
}
