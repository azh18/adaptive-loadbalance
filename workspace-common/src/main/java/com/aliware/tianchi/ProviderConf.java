package com.aliware.tianchi;

import org.apache.dubbo.common.utils.IOUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Enumeration;

/**
 * @author zyz
 * @version 2019-07-15
 */
public class ProviderConf {
    static {
        System.out.println(ProviderConf.class.getResource("provider-conf.json").getFile());
//        String origin = loadResourceAsString("provider-conf.json");
//        System.out.println(origin);
    }

    public static void init() {

    }


    private static String loadResourceAsString(String fileName) {
        ClassLoader classLoader = getClassLoader();

        Enumeration<URL> resources;
        try {
            resources = classLoader.getResources(fileName);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load provider-conf.json,cause:" + e.getMessage(), e);
        }

        while (resources.hasMoreElements()) {
            URL url = resources.nextElement();
            try {
                return IOUtils.read(new InputStreamReader(url.openStream())).replace("\n", "").trim();
            } catch (IOException e) {
                throw new IllegalStateException("Failed to load provider-conf.json,cause:" + e.getMessage(), e);
            }
        }
        throw new IllegalStateException("Can not found provider-conf.json");
    }

    private static ClassLoader getClassLoader() {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        if (classLoader != null) {
            return classLoader;
        }
        return ProviderConf.class.getClassLoader();
    }
}
