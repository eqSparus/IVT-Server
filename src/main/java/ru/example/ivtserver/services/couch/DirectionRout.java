package ru.example.ivtserver.services.couch;

import java.util.Locale;

public enum DirectionRout {
    UP,
    DOWN;

    public static DirectionRout convert(String rout){
        return DirectionRout.valueOf(rout.toUpperCase(Locale.ROOT));
    }
}
