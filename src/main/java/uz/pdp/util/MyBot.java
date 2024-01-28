package uz.pdp.util;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import uz.pdp.entity.TelegramUser;
import uz.pdp.enums.Status;
import uz.pdp.service.TelegramUserRepo;

import java.util.List;

public class MyBot {

    private  String token =  "6871863879:AAGFyjh3JK5mJcFgCN0w_4Wa_KgKl7zHcTQ";
    public  TelegramBot telegramBot = new TelegramBot(token);
    public static TelegramUserRepo userRepo = TelegramUserRepo.getInstance();


    public void start() {

        telegramBot.setUpdatesListener((updates)->{
            for (Update update : updates) {
                if(update!=null){
                    handle(update);
                }
            }
            return UpdatesListener.CONFIRMED_UPDATES_ALL;

        });


    }

    private void handle(Update update) {

        String text = update.message().text();
        Long id = update.message().chat().id();

        TelegramUser currentUser = getUser(id);


        if(text.equals("/start")){

            String sendText = """
                    Assalomu Alaykum botimizga xush kelibsiz. Iltimos tilni tanlang
                    ---------------------------------------------------------------
                    Здравствуйте и добро пожаловать в наш бот. Пожалуйста, выберите язык
                    ---------------------------------------------------------------
                    Hello and welcome to our bot. Please select a language
                    
                    """;
            chooseLanguage(id,sendText);
            currentUser.setStatus(Status.LANGUAGE);
        }

        else {

            if (currentUser.getStatus().equals(Status.LANGUAGE)) {

                //update.message()



            }


        }



    }

    private void chooseLanguage(Long id, String sendText) {

        KeyboardButton keyboardButton = new KeyboardButton("Choose languages");
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(new String[][]{

                {"uz","en","ru"}
        });
        SendMessage message = new SendMessage(id,sendText);
        message.replyMarkup(replyKeyboardMarkup);
        telegramBot.execute(message);

    }

    private TelegramUser getUser(Long id) {

        List<TelegramUser> users = userRepo.getAll();
        for (TelegramUser user : users) {
            if(user.getChatId()==id) return user;
        }

        TelegramUser telegramUser = new TelegramUser(id);
        userRepo.save(telegramUser);
        return telegramUser;
    }
}
