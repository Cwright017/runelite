package net.runelite.client.plugins.slackNotify;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("slack")
public interface SlackNotifyConfig extends Config {
    @ConfigItem(
            position = 1,
            keyName = "apiKey",
            name = "Slack API Key",
            description = "API Key needed to talk to Slack API"
    )
    default String apiKey()
    {
        return "";
    }

    @ConfigItem(
            position = 1,
            keyName = "slackChannel",
            name = "Slack Channel ID",
            description = "Channel ID of Slack Channel to notify"
    )
    default String slackChannel()
    {
        return "";
    }
}

