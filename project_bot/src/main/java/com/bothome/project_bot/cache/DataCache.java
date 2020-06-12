package com.bothome.project_bot.cache;

import com.bothome.project_bot.botapi.BotState;
import com.bothome.project_bot.botapi.fillingprofile.UserProfileData;

public interface DataCache {
    void setUsersCurrentBotState(int userId, BotState botState);
    BotState getUsersCurrentBotState(int userId);
    UserProfileData getUserProfileData(int userId);
    void saveUserProfileData(int userId, UserProfileData userProfileData);
}
