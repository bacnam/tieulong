package bsh.util;

import bsh.ConsoleInterface;

import java.awt.*;

public interface GUIConsoleInterface extends ConsoleInterface {
    void print(Object paramObject, Color paramColor);

    void setNameCompletion(NameCompletion paramNameCompletion);

    void setWaitFeedback(boolean paramBoolean);
}

