package ca.nerret.emu;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class LogFormatter8061 extends Formatter {

    @Override
    public String format(LogRecord record) {
        return record.getMessage() + "\n";
    }
}

