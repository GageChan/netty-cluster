package com.gagechan;

import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.lang.Dict;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Properties;

public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        run();
    }

    private static void run() {
        initProp();
        try {
            SocketServer socketServer = new SocketServer();
            socketServer.start(AppConfig.getDict().getInt(Constant.CONFIG_PRE_PORT));
        } catch (Exception e) {
            logger.error("socket server start failed.", e);
        }
    }

    private static void initProp() {
        Properties properties = new Properties();
        try {
            properties.load(ResourceUtil.getStream(Constant.CONFIG_LOCATION));
        } catch (IOException e) {
            logger.error("load config info err.", e);
        }
        properties.forEach((k, v) -> {
            AppConfig.set(String.valueOf(k), v);
        });

        Dict configs = AppConfig.getDict();
        logger.info("load config: {");
        configs.forEach((k, v) -> {
            logger.info("   {} = {}", k, v);
        });
        logger.info("}");
    }

}
