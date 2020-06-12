package com.bothome.project_bot.botapi.handlers;

import com.bothome.project_bot.botapi.BotState;
import com.bothome.project_bot.botapi.InputMessageHandler;
import com.bothome.project_bot.cache.UserDataCache;
import com.bothome.project_bot.service.ReplyMessagesService;
import lombok.extern.slf4j.Slf4j;
import org.graalvm.compiler.lir.CompositeValue;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Slf4j
@Component
public class AskWorkHandler implements InputMessageHandler {

    private UserDataCache userDataCache;
    private ReplyMessagesService messagesService;

    public AskWorkHandler(UserDataCache userDataCache, ReplyMessagesService messagesService) {
        this.userDataCache = userDataCache;
        this.messagesService = messagesService;
    }

    @Override
    public SendMessage handle(Message message) {
        return processUsersInput(message);
    }

    @Override
    public BotState getHandlerName() {
        return BotState.ASK_WORK;
    }

    private SendMessage processUsersInput(Message inputMessage) {
        int userId = inputMessage.getFrom().getId();
        int chatId = Math.toIntExact(inputMessage.getChatId());

        SendMessage replyToUser = messagesService.getReplyMessage(chatId, "reply.askWork");
        userDataCache.setUsersCurrentBotState(userId,BotState.FILLING_PROFILE);

        return replyToUser;
    }
}
