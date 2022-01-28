package com.example.domer.utilits.engine;


import java.util.ArrayList;
import java.util.List;

public class TextParser {
    private final List<String> list = new ArrayList<>();
    private Translator translator = Translator.INSTANCE;

    public List<String> getTokens(String query, String state) {
        list.clear();
        String[] tokens = query.toLowerCase()
                .replaceAll("[().:]+", " ")
                .split("\\s");
        switch (state) {
            case Engine.ARTICLE_REQ: {
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < tokens.length; i++) {
                    if (Character.isDigit(tokens[i].charAt(0))) {
                        if (i != 0 && !Character.isDigit(stringBuilder.charAt(stringBuilder.length() - 1)))
                            stringBuilder.append("-");
                        stringBuilder.append(tokens[i]);
                    } else {
                        // First word like article. Other like code
                        String s = i == 0 ?
                                translator.getArticle(
                                        Character.toString(tokens[i].charAt(0))
                                ) :
                                translator.getChar(Character.toString(tokens[i].charAt(0)));
                        // word not found at translator.
                        stringBuilder.append(s == null ? " " : s);
                    }
                }
                String trim = stringBuilder.toString().trim();
                list.add(trim.length() == 0 ? "null" : trim);
                break;
            }
            case Engine.COLOR_SIZE_REQ: {
                String color = translator.getColor(tokens[0]);
                if(color != null)
                    list.add(color);
                //Если сказано, например "большой 2"
                String size = tokens.length > 2 ?
                        translator.getSize(castToSize(tokens[1], tokens[2])) : null;
                if(size != null)
                    list.add(size);

                break;
            }
            case Engine.SIZE_REQ: {
                String size = tokens.length>1 ? translator.getSize(castToSize(tokens)) : null;
                if(size != null)
                    list.add(size);

                break;
            }
        }
        return list;
    }

    private String castToSize(String... tokens) {
        // like m1, b2...
        return tokens[0].substring(0, 1) +
                tokens[1];
    }

    public void destroy() {
        translator = null;
    }
}
