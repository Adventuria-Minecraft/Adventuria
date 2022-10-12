package de.thedodo24.xenrodsystem.common.utils;

import de.thedodo24.xenrodsystem.common.CommonModule;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

public class Language {


    private Map<String, String> languageMap = new HashMap<>();

    public Language(String resource) {
        init(resource);
    }

    private void init(String resource) throws NoSuchElementException {
        Yaml yaml = new Yaml();
        InputStream inputStream;
        inputStream = Language.class.getClassLoader().getResourceAsStream(resource);
        Map<String, String> values = yaml.load(inputStream);
        if(values != null)
            languageMap = values;
        else
            throw new NoSuchElementException("No file named "+resource+" found in resources");
    }

    public static Language getLanguage() {
        return CommonModule.getInstance().getLanguage();
    }

    public static Language getMaterials() {
        return CommonModule.getInstance().getMaterials();
    }

    public String get(String key) {
        if(languageMap.containsKey(key))
            return languageMap.get(key);
        return "§cError: phrase-not-found §e("+key+")";
    }

    public String get(String key, Object... args) {
        if(languageMap.containsKey(key))
            return MessageFormat.format(languageMap.get(key), args);
        return "§cError: phrase-not-found §e("+key+")";
    }

}
