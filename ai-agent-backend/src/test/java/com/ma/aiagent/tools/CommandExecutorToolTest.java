package com.ma.aiagent.tools;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

class CommandExecutorToolTest {

    @Test
    void executeTerminalCommand() {
        CommandExecutorTool commandExecutorTool=new CommandExecutorTool();
        String command="ls";
        final String s = commandExecutorTool.executeTerminalCommand(command);
        Assertions.assertNotNull(s);
    }
}