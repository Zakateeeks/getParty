import org.junit.jupiter.api.Test;
import java.io.IOException;
import org.telegram.telegrambots.meta.api.objects.Update;

import static org.junit.jupiter.api.Assertions.*;

class telegramFSMTest {
    @Test
    void selectInfoRegistration() {
        String callbackData = "registration";
        Long chatId = 123456L;
        String text;
        telegramFSM fsm = new telegramFSM();

        telegramBotHandlers handler = new telegramBotHandlers();
        handler.commandHandlers.get(callbackData).accept(chatId, callbackData);

        text = "NA;ME";
        fsm.selectInfo(chatId, fsm.getState(), text);
        System.out.println(fsm.getState() + "__________________");
        assertEquals("name", fsm.getState());

        text = "NAME";
        fsm.selectInfo(chatId, fsm.getState(), text);
        assertEquals("description", fsm.getState());

        text = "oiuhyjkiu;yhj";
        fsm.selectInfo(chatId, fsm.getState(), text);
        assertEquals("description", fsm.getState());

        text = "oiuhyjkiuyhj";
        fsm.selectInfo(chatId, fsm.getState(), text);
        assertEquals("menu", fsm.getState());
    }

    @Test
    void selectInfoCreateEvent(){
        String callbackData = "createEvent";
        Long chatId = 123456L;
        String text;
        telegramFSM fsm = new telegramFSM();

        telegramBotHandlers handler = new telegramBotHandlers();
        handler.commandHandlers.get(callbackData).accept(chatId, callbackData);

        text = "EVE'NT";
        fsm.selectInfo(chatId, fsm.getState(), text);
        assertEquals("eventname", fsm.getState());

        text = "EVENT";
        fsm.selectInfo(chatId, fsm.getState(), text);
        assertEquals("eventdesc", fsm.getState());

        text = "DESCRIPT\"ION";
        fsm.selectInfo(chatId, fsm.getState(), text);
        assertEquals("eventdesc", fsm.getState());

        text = "DESCRIPTION";
        fsm.selectInfo(chatId, fsm.getState(), text);
        assertEquals("eventdate", fsm.getState());

        text = "DESCRIPTION";
        fsm.selectInfo(chatId, fsm.getState(), text);
        assertEquals("eventdate", fsm.getState());

        text = "11.11.1111 11:11";
        fsm.selectInfo(chatId, fsm.getState(), text);
        assertEquals("eventref", fsm.getState());

        text = "DESCRIPTION";
        fsm.selectInfo(chatId, fsm.getState(), text);
        assertEquals("eventref", fsm.getState());

        text = "t.me/+oiughjk";
        fsm.selectInfo(chatId, fsm.getState(), text);
        assertEquals("complete", fsm.getState());
    }
}