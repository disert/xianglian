package com.alibaba.json.parser.deserializer;

import com.alibaba.json.JSONException;
import com.alibaba.json.parser.DefaultJSONParser;
import com.alibaba.json.parser.JSONToken;

import java.lang.reflect.Type;
import java.util.Locale;

public class LocaleDeserializer implements ObjectDeserializer {
    public final static LocaleDeserializer instance = new LocaleDeserializer();

    @SuppressWarnings("unchecked")
    public <T> T deserialze(DefaultJSONParser parser, Type clazz, Object fieldName) throws JSONException {
        String text = (String) parser.parse();
        
        if (text == null) {
            return null;
        }
        
        String[] items = text.split("_");
        
        if (items.length == 1) {
            return (T) new Locale(items[0]);
        }
        
        if (items.length == 2) {
            return (T) new Locale(items[0], items[1]);
        }
        
        return (T) new Locale(items[0], items[1], items[2]);
    }

    public int getFastMatchToken() {
        return JSONToken.LITERAL_STRING;
    }
}
