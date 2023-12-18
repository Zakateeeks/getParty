import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
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
    private static int currentEmpidCreated = 0;
    private static int counterMember = 0;

    public telegramBotHandlers() {
        commandHandlers.put("registration", this::textCommand);
        commandHandlers.put("member", this::registration);
        commandHandlers.put("organizer", this::registration);
        commandHandlers.put("createEvent", this::createEvent);
        commandHandlers.put("eventList", this::eventList);
        commandHandlers.put("empidPlus", this::empidPlus);
        commandHandlers.put("nextElem", this::nextElem);
        commandHandlers.put("empidMinus", this::empidMinus);
        commandHandlers.put("prevElem", this::prevElem);
        commandHandlers.put("menu", this::toMenu);
        commandHandlers.put("viewProfile", this::viewProfile);
        commandHandlers.put("createdEventList", this::viewCreatedList);
        commandHandlers.put("delEvent", this::deleteEvent);
        commandHandlers.put("signUp", this::signUpAnEvent);
        commandHandlers.put("viewMember", this::viewEventMembers);
        commandHandlers.put("plusCounter", this::plusCounter);
        commandHandlers.put("minusCounter", this::minusCounter);
        commandHandlers.put("deleteMember", this::deleteMember);
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

    public void registration(Long chatId, String S) {
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
                if (S.equals("member")) {
                    role = "Участник";
                } else {
                    role = "Организатор";
                }
                db.insertRow(conn, "users", "role", "chatid", role, chatId.toString());
            } else {
                message.setText("Вы уже зарегистрированы");
                execute(message);
                textCommand(chatId, "menu$");
            }
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void createEvent(Long chatId, String S) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        telegramFSM fsm = new telegramFSM();
        fsm.changeState("eventname");
        fsm.currentDB = "event";
        try {
            String organizer = db.searchByChatID(conn, "users", chatId.toString(), "name");
            message.setText("Как называется Ваше мероприятие?");
            execute(message);
            db.insertRow(conn, "event", "organizer", "chatid", organizer, chatId.toString());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void eventList(Long chatId, String S) {
        telegramBotCommand newCommand;
        if (currentEmpid == 1 | currentEmpid > db.countRow(conn, "event")) {
            currentEmpid = 1;
            newCommand = new telegramBotCommand("first$");
        } else if (currentEmpid == db.countRow(conn, "event")) {
            newCommand = new telegramBotCommand("last$");
        } else {
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
            String[] eventRow = db.viewRowEvent(conn, "event", currentEmpid);
            message.setText(eventRow[1] + "\n\n" + "*Организатор:* " + eventRow[0] + "\n\n" + eventRow[2] + "\n\n*Дата и время:* " + eventRow[5]);
            message.enableMarkdown(true);
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void viewProfile(Long chatId, String S) {
        telegramBotCommand newCommand = new telegramBotCommand("profile$");
        ;
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
            String[] eventRow = db.viewRowUsers(conn, "users", "chatid", chatId.toString());
            message.setText(eventRow[1] + "\n\n" + "*Ваша роль:* " + eventRow[2] + "\n\n" + eventRow[3]);
            message.enableMarkdown(true);
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }


    public void viewCreatedList(Long chatId, String S) {
        ArrayList<Integer> arrList = db.searchEvents(conn, "event", chatId.toString());
        telegramBotCommand newCommand;

        if (currentEmpidCreated == arrList.get(0)) {
            newCommand = new telegramBotCommand("firstMyEvent$");
        } else if (currentEmpidCreated == arrList.get(arrList.size() - 1)) {
            newCommand = new telegramBotCommand("lastMyEvent$");
        } else if (arrList.size() == 1) {
            currentEmpidCreated = arrList.get(0);
            newCommand = new telegramBotCommand("oneMyEvent$");
        } else if (arrList.size() != 0 && currentEmpidCreated == 0) {
            currentEmpidCreated = arrList.get(0);
            newCommand = new telegramBotCommand("firstMyEvent$");

        } else {
            newCommand = new telegramBotCommand("midleMyEvent$");
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
            String[] eventRow = db.viewRowEvent(conn, "event", currentEmpidCreated);
            message.setText(eventRow[1] + "\n\n" + "*Организатор:* " + eventRow[0] + "\n\n" + eventRow[2] + "\n\n*Дата и время:* " + eventRow[5]);
            message.enableMarkdown(true);
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void viewEventMembers(Long chatId, String s) {
        telegramBotCommand newCommand;
        String[] thisEvent = db.viewRowEvent(conn, "event", currentEmpidCreated);
        String members = thisEvent[7];
        if (members != null && !members.equals("")) {
            String[] memArr = members.split(" ");

            if (memArr.length == 1){
                newCommand = new telegramBotCommand("oneMember$");
            }
            else{
                if (counterMember == 0){
                    newCommand = new telegramBotCommand("firstMember$");
                }
                else if (counterMember == memArr.length-1){
                    newCommand = new telegramBotCommand("lastMember$");
                }
                else {
                    newCommand = new telegramBotCommand("midleMember$");
                }
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
                String[] userRow = db.viewRowUsers(conn, "users", "empid",String.valueOf(memArr[counterMember]));
                message.setText(userRow[1] + "\n\n"+ userRow[3]);
                message.enableMarkdown(true);
                execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
        else{
            SendMessage message = new SendMessage();
            message.setChatId(chatId);
            try {
                message.setText("Никто не пришел на сходку папищеков(((((😭😭😭");
                message.enableMarkdown(true);
                execute(message);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void deleteMember(Long chatId, String s) {
        String[] thisEvent = db.viewRowEvent(conn, "event", currentEmpidCreated);
        String[] members = thisEvent[7].split(" ");
        String[] result = new String[members.length - 1];
        int index = counterMember;

        System.arraycopy(members, 0, result, 0, index);
        System.arraycopy(members, index + 1, result, index, members.length - index - 1);

        String newMember = Arrays.toString(result).replace(',',' ').replace("[","").replace("]","");
        try {
            db.editMembers(conn,currentEmpidCreated,newMember);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        try {
            message.setText("Участник исключен, так его🤬🤬🤬");
            message.enableMarkdown(true);
            execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteEvent(Long chatId, String S) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);

        db.deleteRow(conn, "event", currentEmpidCreated);
        try {
            message.setText("Мероприятие успешно удалено!");
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }

    public void empidPlus(Long chatId, String S) {
        currentEmpid++;
        eventList(chatId, S);
    }

    public void plusCounter(Long chatId, String S) {
        counterMember++;
        viewEventMembers(chatId, S);
    }

    public void minusCounter(Long chatId, String S) {
        counterMember--;
        viewEventMembers(chatId, S);
    }
    public void nextElem(Long chatId, String S) {
        ArrayList<Integer> arrList = db.searchEvents(conn, "event", chatId.toString());
        currentEmpidCreated++;

        while (!arrList.contains(currentEmpidCreated)) {
            currentEmpidCreated++;
        }
        viewCreatedList(chatId, S);
    }

    public void empidMinus(Long chatId, String S) {
        currentEmpid--;
        eventList(chatId, S);
    }

    public void prevElem(Long chatId, String S) {
        ArrayList<Integer> arrList = db.searchEvents(conn, "event", chatId.toString());
        currentEmpidCreated--;

        while (!arrList.contains(currentEmpidCreated)) {
            currentEmpidCreated--;
        }

        viewCreatedList(chatId, S);
    }

    public void toMenu(Long chatId, String S) {
        textCommand(chatId, "menu$");
    }

    public void signUpAnEvent(Long chatId, String S) {
        db.singUpUser(conn, "event", currentEmpid, Long.toString(chatId));
        textCommand(chatId, "singUp$");
    }

}

