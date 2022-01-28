package com.example.domer.utilits.engine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public enum Translator {
    INSTANCE;
    private Map<String, String> alphabet;
    private Map<String, String> colors;
    private Map<String, String> size;
    private Map<String, String> article;

    Translator(){
        init();
    }

    private void init() {
        //
        String[] alphabets = {"a:а","b:б","c:ц","d:д","e:е","f:ф","g:г","h:ш","i:и","j:ж",
                "k:к","l:л","m:м","n:н","o:о","p:п","q:у","r:р","s:с","t:т","u:ю","v:в","w:э","x:х","y:й","z:з"};
        alphabet = Arrays.stream(alphabets).map(elem -> elem.split(":"))
                .collect(Collectors.toMap(e -> e[1], e -> e[0]));
        //
        String[] eng_color = {"ANTHRACITE:антрацит", "BEIGE:бежевый", "BLACK:чёрный", "BLUE:голубой",
                "BROWN:коричневый", "BURGUNDY:бордовый",
                "DARK_BLUE:тёмно-голубой", "DARK_BROWN:тёмно-коричневый", "FUCHSIA:фуксия", "GREEN:зелёный", "GREY:серый", "LIGHT_BLUE:светло-голубой",
                "LIGHT_BROWN:светло-коричневый", "LIGHT_GREEN:светло-зелёный", "LIGHT_GREY:светло-серый", "LIGHT_LILAC:светло-лиловый", "LIGHT_PINK:светло-розовый",
                "LILAC:лиловый", "MINT_GREEN:мятно-зелёный", "ORANGE:оранжевый", "PINK:розовый", "SALMON:персиковый", "SAX:синий", "SKY_BLUE:небесный",
                "TURQUOISE:бирюзовый", "YELLOW:жёлтый"};
        colors = Arrays.stream(eng_color).map(elem -> elem.split(":"))
                .collect(Collectors.toMap(e -> e[1], e -> e[0]));
        //
        String[] eng_size = {"50*80/1:м1", "50*80/2:м2", "60*100/1:б1", "60*100/2:б2"};
        size = Arrays.stream(eng_size).map(elem -> elem.split(":"))
                .collect(Collectors.toMap(e -> e[1], e -> e[0]));
        //

        String[] eng_article = {"bp:в", "mp:м", "clc:к", "clt:т", "dec:е", "dn:н", "ds:д", "fy:ф",
                "gp:г", "sj:и", "pal:п", "sy:у", "slv:с", "td:а", "tf:р", "xj:ж", "zg:б"};
        article = Arrays.stream(eng_article).map(elem -> elem.split(":"))
                .collect(Collectors.toMap(e -> e[1], e -> e[0]));

    }

    public String getChar(String value){
        return alphabet.get(value);
    }

    public String getColor(String value){
        return colors.get(value);
    }

    public String getSize(String value){
        return size.get(value);
    }

    public String getArticle(String value) {
        return article.get(value);
    }
}