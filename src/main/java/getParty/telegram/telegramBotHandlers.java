import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * Класс предназначен для обработки команд
 *
 * @author Свечников Дмитрий и Цымбал Александр
 */
public class telegramBotHandlers extends telegramBotConfigure {

    public Map<String, BiConsumer<Long, String>> commandHandlers = new HashMap<>();
    telegramBotDatabase db = new telegramBotDatabase();
    Connection conn = db.connectToDatabase("bot_users", "postgres", "1234");
    private static int currentEmpid = 1;

    public telegramBotHandlers() {
        commandHandlers.put("registration", this::textCommand);
        commandHandlers.put("member", this::registration);
        commandHandlers.put("organizer", this::registration);
        commandHandlers.put("createEvent", this::createEvent);
        commandHandlers.put("eventList", this::eventList);
        commandHandlers.put("empidPlus", this::empidPlus);
        commandHandlers.put("empidMinus", this::empidMinus);
    }


    public void textCommand(Long chatId, String nameCommand) {
        telegramBotCommand newCommand = new telegramBotCommand(nameCommand);
        List<InlineKeyboardButton> inlineButtons = newCommand.getButtonsList();

        SendMessage message = new SendMessage();
                message.enableMarkdown(true);
                message.setChatId(chatId);
                message.setText(newCommand.getCommandText());


        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboardRows = new ArrayList<>();

        if (inlineButtons != null && !inlineButtons.isEmpty()) {
            List<InlineKeyboardButton> row = new ArrayList<>();
            row.addAll(inlineButtons);
            keyboardRows.add(row);
        }

        inlineKeyboardMarkup.setKeyboard(keyboardRows);
        message.setReplyMarkup(inlineKeyboardMarkup);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void registration(Long chatId, String S){
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        telegramFSM fsm = new telegramFSM();
        fsm.changeState("name");
        fsm.currentDB = "users";

        System.out.println(S);
        try {
            if (db.searchByChatID(conn, "users", chatId.toString(), "chatid") == "11") {
                String role;
                message.setText("Введите имя");
                execute(message);
                if(S.equals("member")){
                     role = "Участник";
                }else{
                     role = "Организатор";
                }
                db.insertRow(conn, "users", "role","chatid",role, chatId.toString());
            } else {
                message.setText("Вы уже зарегистрированы");
                execute(message);
                textCommand(chatId, "menu$");
            }
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void createEvent(Long chatId, String S){
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        telegramFSM fsm = new telegramFSM();
        fsm.changeState("eventname");
        fsm.currentDB = "event";
        try {
            String organizer = db.searchByChatID(conn, "users", chatId.toString(), "name");
            message.setText("Как называется Ваше мероприятие?");
            execute(message);
            db.insertRow(conn, "event", "organizer","chatid",organizer, chatId.toString());

        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void eventList(Long chatId, String S) {
        telegramBotCommand newCommand;
        if (currentEmpid==1) {
            newCommand = new telegramBotCommand("first$");
        }else if(currentEmpid == db.countRow(conn,"event")){
            newCommand = new telegramBotCommand("last$");
        }else{
            newCommand = new telegramBotCommand("midle$");
        }
        List<InlineKeyboardButton> inlineButtons = newCommand.getButtonsList();

        SendMessage message = new SendMessage();
        message.setChatId(chatId);

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboardRows = new ArrayList<>();

        if (inlineButtons != null && !inlineButtons.isEmpty()) {
            List<InlineKeyboardButton> row = new ArrayList<>();
            row.addAll(inlineButtons);
            keyboardRows.add(row);
        }

        inlineKeyboardMarkup.setKeyboard(keyboardRows);
        message.setReplyMarkup(inlineKeyboardMarkup);

        try {
            String[] eventRow = db.viewRow(conn, "event", currentEmpid);
            message.setText(eventRow[2] + "\n\n" + "*Организатор:* " + eventRow[1] + "\n\n" + eventRow[3] + "\n\n*Дата и время:* " + eventRow[6]);
            message.enableMarkdown(true);
            execute(message);
        } catch (TelegramApiException e){
            e.printStackTrace();
        }
    }

    public void empidPlus(Long chatId, String S){
        currentEmpid++;
        eventList(chatId,S);
    }
    public void empidMinus(Long chatId, String S){
        currentEmpid--;
        eventList(chatId,S);
    }
}

