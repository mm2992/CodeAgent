package com.ma.aiagent.agent;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public abstract class ReActAgent extends BaseAgent{

    public abstract boolean think();

    public abstract String act();

    @Override
    public String step(String userPrompt) {
        boolean shouldAct = think();
        if (!shouldAct){
            return "no need to act";
        }
        return act();
    }
}
