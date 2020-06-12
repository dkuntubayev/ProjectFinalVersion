package com.bothome.project_bot.appconfig;

import com.bothome.project_bot.MyProjectTelegramBot;
import com.bothome.project_bot.botapi.TelegramFacade;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.ApiContext;

@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "telegrambot")
public class BotConfig {
    private String webHookPath;
    private String botUserName;
    private String botToken;

    @Bean
    public MyProjectTelegramBot mySuperTelegramBot(TelegramFacade telegramFacade) {
        DefaultBotOptions options = ApiContext.getInstance(DefaultBotOptions.class);

        MyProjectTelegramBot myProjectTelegramBot = new MyProjectTelegramBot(options, telegramFacade);
        myProjectTelegramBot.setWebHookPath(webHookPath);
        myProjectTelegramBot.setBotUserName(botUserName);
        myProjectTelegramBot.setBotToken(botToken);

        return myProjectTelegramBot;
    }

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();

        messageSource.setBasename("classspath:messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }
}
