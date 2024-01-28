package uz.pdp.util;

import uz.pdp.entity.TelegramUser;

import java.util.List;

public interface Repository<T> {


    void save(T t);
    List<T> getAll();

}
