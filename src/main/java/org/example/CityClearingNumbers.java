package org.example;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class CityClearingNumbers {

    public static final Map<String, String> CITY_CLEARING_MAP;

    static {
        Map<String, String> map = new HashMap<>();
        map.put("Stockholm", "1111-1");
        map.put("Göteborg", "2222-2");
        map.put("Malmö", "3333-3");
        map.put("Uppsala", "4444-4");
        map.put("Västerås", "5555-5");
        map.put("Örebro", "6666-6");
        map.put("Linköping", "7777-7");
        map.put("Helsingborg", "8888-8");
        map.put("Jönköping", "1212-1");
        map.put("Norrköping", "2323-2");
        map.put("Lund", "3434-3");
        map.put("Umeå", "4545-4");
        map.put("Gävle", "5656-5");
        map.put("Borås", "6767-6");
        map.put("Södertälje", "7878-7");
        map.put("Eskilstuna", "8989-8");
        map.put("Halmstad", "1414-1");
        map.put("Växjö", "2525-2");
        map.put("Karlstad", "3636-3");
        map.put("Täby", "4747-4");
        map.put("Luleå", "5858-5");
        map.put("Solna", "6969-6");
        map.put("Trollhättan", "8080-8");
        map.put("Örnsköldsvik", "1515-1");
        map.put("Kalmar", "2626-2");
        map.put("Falun", "3737-3");
        map.put("Skövde", "4848-4");
        map.put("Motala", "7070-7");
        map.put("Kristianstad", "8181-8");
        map.put("Förslöv", "1616-1");
        map.put("Sundsvall", "2727-2");
        map.put("Varberg", "3838-3");
        map.put("Enköping", "4949-4");
        map.put("Kungsbacka", "6060-6");
        map.put("Uddevalla", "7171-7");
        map.put("Landskrona", "8282-8");
        map.put("Nacka", "1717-1");
        map.put("Kungälv", "2828-2");
        map.put("Boden", "3939-3");
        map.put("Vimmerby", "5050-5");
        map.put("Skellefteå", "6161-6");
        map.put("Alingsås", "7272-7");
        map.put("Staffanstorp", "8383-8");
        map.put("Åkersberga", "1818-1");
        map.put("Vårby", "2929-2");
        map.put("Tyresö", "4040-4");
        map.put("Borlänge", "5151-5");
        map.put("Huddinge", "6262-6");
        map.put("Mölndal", "7373-7");
        map.put("Östersund", "8484-8");
        map.put("Sollentuna", "1919-1");
        map.put("Österåker", "3030-3");
        // Lägg till fler vid behov
        CITY_CLEARING_MAP = Collections.unmodifiableMap(map);
    }

    public static String getClearingNumber(String city) {
        if (city == null) return "0000-0";
        city = city.toLowerCase();
        city = Character.toUpperCase(city.charAt(0)) + city.substring(1);
        return CITY_CLEARING_MAP.getOrDefault(city, "0000-0");
    }
}
