package com.lij.myqqrobotserver.task.base;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
public abstract class BaseTask {
    protected static Logger logger = LogManager.getLogger();

    public void excute() {
        try {
            task();
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    public abstract void task() throws Exception;
}
