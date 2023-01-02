package com.application.foggy.support;

import com.application.foggy.support.websecurityconfig.TokenDetails;
import lombok.Data;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Locale;
import java.util.UUID;

@Service
@Data
public class SupportedFunctions {
    public DateTimeFormatter getDateFormatter(){
        return DateTimeFormatter.ofPattern("dd-MMM-yyyy");
    }

    public String getUUID(){
        return UUID.randomUUID().toString();
    }

    public DateTimeFormatter getYearAndMonthFormatter() {
        return new DateTimeFormatterBuilder()
                .parseCaseInsensitive()
                .append(DateTimeFormatter.ofPattern("MMM-uuuu"))
                .toFormatter(Locale.ENGLISH);
    }

    private TokenDetails tokenDetails;
}
