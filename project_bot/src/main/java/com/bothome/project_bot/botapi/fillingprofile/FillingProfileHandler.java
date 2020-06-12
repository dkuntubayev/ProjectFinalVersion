package com.bothome.project_bot.botapi.fillingprofile;

import com.bothome.project_bot.botapi.BotState;
import com.bothome.project_bot.botapi.InputMessageHandler;
import com.bothome.project_bot.cache.UserDataCache;
import com.bothome.project_bot.service.ReplyMessagesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Slf4j
@Component
public class FillingProfileHandler implements InputMessageHandler {

    private UserDataCache userDataCache;
    private ReplyMessagesService messagesService;

    public FillingProfileHandler(UserDataCache userDataCache, ReplyMessagesService messagesService) {
        this.userDataCache = userDataCache;
        this.messagesService = messagesService;
    }

    @Override
    public SendMessage handle(Message message) {
        if (userDataCache.getUsersCurrentBotState(message.getFrom().getId()).equals(BotState.FILLING_PROFILE)) {
            userDataCache.setUsersCurrentBotState(message.getFrom().getId(), BotState.ASK_NAME);
        }
        return processUsersInput(message);
    }

    @Override
    public BotState getHandlerName() {
        return BotState.FILLING_PROFILE;
    }

    private SendMessage processUsersInput(Message inputMessage) {
        String usersAnswer = inputMessage.getText();
        int userId = inputMessage.getFrom().getId();
        long chatId = inputMessage.getChatId();

        UserProfileData profileData = userDataCache.getUserProfileData(userId);
        BotState botState = userDataCache.getUsersCurrentBotState(userId);

        SendMessage replyToUser = null;

        if (botState.equals(BotState.ASK_NAME)) {
            replyToUser = messagesService.getReplyMessage(chatId, "reply.askName");
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_POSITION);
        }

        if (botState.equals(BotState.ASK_POSITION)) {
            replyToUser = messagesService.getReplyMessage(chatId, "reply.askPosition");
            profileData.setName(usersAnswer);
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_SKILLS);
        }

        if (botState.equals(BotState.ASK_SKILLS)) {
            replyToUser = messagesService.getReplyMessage(chatId, "reply.askSkills");
            profileData.setPosition(usersAnswer);
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_EXPERIENCE);
        }

        if (botState.equals(BotState.ASK_EXPERIENCE)) {
            replyToUser = messagesService.getReplyMessage(chatId, "reply.askExperience");
            profileData.setSkills(usersAnswer);
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_POSITION);
        }

        if (botState.equals(BotState.PROFILE_FILLED)) {
            profileData.setExperience(Integer.parseInt(usersAnswer));
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_WORK);
            replyToUser = new SendMessage(chatId, String.format("%s %s %s", "Your personal info: ", profileData,
                    ". You will be noticed when satisfying job is found!!!"));
        }

        userDataCache.saveUserProfileData(userId, profileData);

        return replyToUser;
    }


}
