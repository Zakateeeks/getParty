public class telegramBotNotification implements Runnable {
    private telegramBotConfigure bot;

    public telegramBotNotification(telegramBotConfigure bot) {
        this.bot = bot;
    }

    @Override
    public void run() {
        while (true) {

            String organizerChatId = "";
            String message = "Hello";
            bot.sendNotification(organizerChatId, message);

            try {
                Thread.sleep(100000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
