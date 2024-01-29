package uz.pdp.util;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Contact;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import uz.pdp.entity.TelegramUser;
import uz.pdp.enums.Status;
import uz.pdp.service.TelegramUserRepo;

import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class MyBot {

    private  String token =  "6871863879:AAGFyjh3JK5mJcFgCN0w_4Wa_KgKl7zHcTQ";
    public  TelegramBot telegramBot = new TelegramBot(token);
    //public static TelegramUserRepo userRepo = TelegramUserRepo.getInstance();


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

        if(update.message().contact()!=null){

            Contact contact = update.message().contact();
            String phonedNumber = contact.phoneNumber();
            currentUser.setPhoneNumber(phonedNumber);
            currentUser.setStatus(Status.SHARECONTACT);
        }

        else {

            if (text.equals("/start")) {

                String sendText = """
                        Assalomu Alaykum botimizga xush kelibsiz. Iltimos tilni tanlang
                        ---------------------------------------------------------------------
                        Здравствуйте и добро пожаловать в наш бот. Пожалуйста, выберите язык
                        ---------------------------------------------------------------------
                        Hello and welcome to our bot. Please select a language
                                            
                        """;
                chooseLanguage(id, sendText);
                currentUser.setStatus(Status.LANGUAGE);
            } else {

                if (currentUser.getStatus().equals(Status.LANGUAGE)) {

                    String lang = update.message().text();
                    currentUser.setChatLang(lang);
                    String getText = getLangText(currentUser.getChatLang(), "ENTER_NAME");
                    sendMassageToBot(getText, id);
                    currentUser.setStatus(Status.NAME);


                } else if (currentUser.getStatus().equals(Status.NAME)) {

                    String name = update.message().text();
                    currentUser.setName(name);
                    String shareContact = getLangText(currentUser.getChatLang(), "SHARE_CONTACT");
                    sendMassageToBot(shareContact, id,currentUser);
                    currentUser.setStatus(Status.SHARECONTACT);

                } else if (currentUser.getStatus().equals(Status.SHARECONTACT)) {



                }


            }

        }


    }

    private void sendMassageToBot(String getText, Long id) {



        SendMessage sendMessage = new SendMessage(id,getText);
        telegramBot.execute(sendMessage);
    }

    private void sendMassageToBot(String getText, Long id,TelegramUser user) {

        if(user.getStatus().equals(Status.NAME)){


            KeyboardButton keyboardButton =
                    new KeyboardButton(getLangText(user.getChatLang(),"CONTACT"));
            keyboardButton.requestContact(true);
            ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(keyboardButton);
            SendMessage sendMessage = new SendMessage(id,getText);
            sendMessage.replyMarkup(replyKeyboardMarkup);
            telegramBot.execute(sendMessage);

            return;
        }


        SendMessage sendMessage = new SendMessage(id,getText);
        telegramBot.execute(sendMessage);
    }



    private String getLangText(String chatLang, String textLang) {

        //Locale locale = Locale.of(chatLang);
        Locale locale = new Locale(chatLang);
        ResourceBundle bundle = ResourceBundle.getBundle("message", locale);

        String greeting = bundle.getString(textLang);
        return greeting;

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
        TelegramUserRepo userRepo = TelegramUserRepo.getInstance();
        List<TelegramUser> users = userRepo.getAll();
        for (TelegramUser user : users) {
            if(user.getChatId().equals(id)){
                return user;
            }
        }

        TelegramUser telegramUser = new TelegramUser(id);
        userRepo.save(telegramUser);
        return telegramUser;
    }
}
