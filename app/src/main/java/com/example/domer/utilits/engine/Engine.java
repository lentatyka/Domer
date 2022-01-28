package com.example.domer.utilits.engine;

import com.example.domer.database.Product;
import com.example.domer.utilits.Constants;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class Engine implements Consumer<String>, Function<List<Product>, String[]> {
    public static final String ARTICLE_REQ = "ARTICLE_REQ";
    public static final String COLOR_SIZE_REQ = "COLOR_SIZE_REQ";
    public static final String SIZE_REQ = "SIZE_REQ";
    public static final String ERROR_REQ = "ERROR_REQ";
    public static final String CANCEL_REQ = "CANCEL_REQ";

    private MyBinaryOperator<List<String>, String> consumer;
    private final TextParser textParser;
    private final String[] answers;
    private String current_state;

    public Engine() {
        textParser = new TextParser();
        current_state = ARTICLE_REQ;
        answers = new String[2];
    }

    @Override
    public void accept(String result) {
        if(result == null || Constants.KW_CANCEL.equalsIgnoreCase(result)){
            consumer.accept(null, CANCEL_REQ);
        }else{
            result = result.toLowerCase();
            List<String> tokens = textParser.getTokens(result, current_state);
            if (tokens.size() == 0)
                consumer.accept(null, ERROR_REQ);
            else
                consumer.accept(tokens, current_state);
        }
    }

    @Override
    public String[] apply(List<Product> value) {
        if(value == null){
            current_state = ARTICLE_REQ;
            answers[0] = current_state;
            answers[1] = "Ошибка в запросе";
            return answers;
        }
        //Succes request. Stop search!
        else if (value.size() == 1) {
            current_state = ARTICLE_REQ;
            answers[0] = current_state;
            answers[1] = value.get(0).getArticle();
            return answers;
        }
        //Else try more arguments
        switch (current_state) {
            case ARTICLE_REQ: {
                if (value.size() == 0){
                    answers[1] = "Совпадений не найдено";
                }
                else if (value.get(value.size()-1).getColor().length() > 0) {
                    current_state = COLOR_SIZE_REQ;
                    answers[1] = "Уточните цвет и размер";
                } else {
                    current_state = ARTICLE_REQ;
                    answers[1] = "Найдено" + value.size() + "вариантов. Уточните запрос";
                }
                break;
            }
            case COLOR_SIZE_REQ:
            case SIZE_REQ: {
                if (value.size() == 0) {
                    current_state = ARTICLE_REQ;
                    answers[1] = "Совпадений не найдено";
                } else if (current_state.equals(COLOR_SIZE_REQ)) {
                    current_state = SIZE_REQ;
                    answers[1] = "Уточните размер";
                } else {
                    current_state = ARTICLE_REQ;
                    answers[1] = "Найдено" + value.size() + "вариантов. Уточните запрос";
                }
            }
        }
        answers[0] = current_state;
        return answers;
    }

    public void destroy() {
        textParser.destroy();
    }

    public interface MyBinaryOperator<T, V> {
        void accept(T t, V v);
    }

    public void setCaller(MyBinaryOperator<List<String>, String> caller) {
        this.consumer = caller;
    }
}
