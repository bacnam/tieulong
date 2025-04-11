package ch.qos.logback.classic.pattern;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.LoggerContextVO;

import java.util.Map;

public final class PropertyConverter
        extends ClassicConverter {
    String key;

    public void start() {
        String optStr = getFirstOption();
        if (optStr != null) {
            this.key = optStr;
            super.start();
        }
    }

    public String convert(ILoggingEvent event) {
        if (this.key == null) {
            return "Property_HAS_NO_KEY";
        }
        LoggerContextVO lcvo = event.getLoggerContextVO();
        Map<String, String> map = lcvo.getPropertyMap();
        String val = map.get(this.key);
        if (val != null) {
            return val;
        }
        return System.getProperty(this.key);
    }
}

