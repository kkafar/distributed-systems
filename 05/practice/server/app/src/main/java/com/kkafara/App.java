package com.kkafara;

import com.kkafara.rt.Result;
import com.kkafara.server.ChatServer;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;

public class App {

    public static void main(String[] args) {
        Configurator.setRootLevel(Level.ALL);

        final Logger logger = LogManager.getLogger(App.class.getName());

        logger.info("Creating server instance");

        ChatServer server = new ChatServer();

        Result<Void, Void> stubResult = Result.ok();

        stubResult.isErr();
    }
}
