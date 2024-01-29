package uz.pdp.entity;

import uz.pdp.enums.Status;

import java.io.Serializable;

public class TelegramUser implements Serializable {

    private String name;
    private Long chatId;
    private String chatLang;
    private String phoneNumber;
    private Status status = Status.START;

    public TelegramUser() {
    }

    public TelegramUser(Long chatId) {
        this.chatId = chatId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public String getChatLang() {
        return chatLang;
    }

    public void setChatLang(String chatLang) {
        this.chatLang = chatLang;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
