package ru.example.ivtserver.configuratuons.convert;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.example.ivtserver.services.couch.DirectionRout;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Component
public class DirectionRoutConverter implements Converter<String, DirectionRout> {
    @Override
    public DirectionRout convert(String source) {
        return DirectionRout.convert(source);
    }

    @Override
    public <U> Converter<String, U> andThen(Converter<? super DirectionRout, ? extends U> after) {
        return Converter.super.andThen(after);
    }
}
