package net.runelite.client.plugins.slackNotify;

import com.google.inject.Provides;
import com.hubspot.algebra.Result;
import com.hubspot.slack.client.SlackClient;
import com.hubspot.slack.client.SlackClientFactory;
import com.hubspot.slack.client.SlackClientRuntimeConfig;
import com.hubspot.slack.client.SlackWebClient;
import com.hubspot.slack.client.methods.params.chat.ChatPostMessageParams;
import com.hubspot.slack.client.models.response.SlackError;
import com.hubspot.slack.client.models.response.chat.ChatPostMessageResponse;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.NotificationSent;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.smelting.SmeltingConfig;

import javax.inject.Inject;

@PluginDescriptor(
        name = "Slack Notifier",
        description = "Send a slack message when notifications are sent",
        tags = {"slack", "notifier", "notifications"}
)
public class SlackNotifyPlugin extends Plugin
{
    private SlackClient slackClient;
    SlackWebClient.Factory clientFactory;

    @Inject
    private SlackNotifyConfig config;

    @Provides
    SlackNotifyConfig getConfig(ConfigManager configManager)
    {
        return configManager.getConfig(SlackNotifyConfig.class);
    }


    @Override
    protected void startUp() throws Exception {
        super.startUp();
        System.out.println("STARTING");
        SlackClientRuntimeConfig slackConfig = SlackClientRuntimeConfig.builder()
                .setTokenSupplier(() -> config.apiKey())
                .build();

        SlackClient slackClient = SlackClientFactory.defaultFactory().build(slackConfig);
        messageChannel(config.slackChannel(), "Starting up");
    }

    public ChatPostMessageResponse messageChannel(String channelToPostIn, String message) {
        Result<ChatPostMessageResponse, SlackError> postResult = this.slackClient.postMessage(
                ChatPostMessageParams.builder()
                        .setText(message)
                        .setChannelId(channelToPostIn)
                        .build()
        ).join();

        return postResult.unwrapOrElseThrow(); // release failure here as a RTE
    }

    @Subscribe
    public void onNotificationSent(NotificationSent notification) {
        messageChannel(config.slackChannel(), notification.getMessage());
    }
}