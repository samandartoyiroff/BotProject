package uz.pdp.service;

import lombok.SneakyThrows;
import uz.pdp.entity.TelegramUser;
import uz.pdp.util.Repository;

import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class TelegramUserRepo implements Repository<TelegramUser> {

    public static List<TelegramUser> USERS;
    public static String PATH="user.txt";
    public static TelegramUserRepo telegramUserRepo;

    public TelegramUserRepo(List<TelegramUser> USERS) {
        this.USERS = USERS;
    }

    public static TelegramUserRepo getInstance(){
        if(telegramUserRepo==null){
            telegramUserRepo = new TelegramUserRepo(getList());
        }
        return telegramUserRepo;
    }

    @SuppressWarnings("unchecked")
    private static List<TelegramUser> getList() {

        try(
        InputStream inputStream = new FileInputStream(PATH);
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
        ){
            List<TelegramUser> users =(List<TelegramUser>) objectInputStream.readObject();
        } catch (ClassNotFoundException | IOException e) {
            return new ArrayList<>();
        }

     return null;
    }

    @Override
    public void save(TelegramUser telegramUser) {
        USERS.add(telegramUser);
        load();
    }

    private void load() {
        try(
                OutputStream outputStream = new FileOutputStream(PATH);
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        ){
            objectOutputStream.writeObject(USERS);
        } catch (IOException e) {
        }
    }

    @Override
    public List<TelegramUser> getAll() {
        return USERS;
    }
}
