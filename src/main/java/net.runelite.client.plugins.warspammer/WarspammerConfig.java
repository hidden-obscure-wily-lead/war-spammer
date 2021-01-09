package net.runelite.client.plugins.warspammer;

import java.awt.event.KeyEvent;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ModifierlessKeybind;

@ConfigGroup("warspammer")
public interface WarspammerConfig extends Config
{
	@ConfigItem(
			position = 0,
			keyName = "manualEnter",
			name = "Manually press enter?",
			description = "Manually press enter?"
	)
	default boolean manuallyEnter()
	{ return false; }

	@ConfigItem(
		position = 1,
		keyName = "pileSpam",
		name = "Spam pile name",
		description = "Spam pile name with Tempest prefix"
	)
	default boolean pileSpam()
	{ return true; }

	@ConfigItem(
		position = 2,
		keyName = "pileCharCount",
		name = "Name character count",
		description = "Characters appended to spam"
	)
	default int pileCharCount()
	{ return 3; }

	@ConfigItem(
		position = 3,
		keyName = "pileSpamKey",
		name = "Pile Spam Hotkey",
		description = "The key to spam pile"
	)
	default ModifierlessKeybind pileSpamKey()
	{ return new ModifierlessKeybind(KeyEvent.VK_F6, 0); }

	@ConfigItem(
		position = 4,
		keyName = "customSpam1",
		name = "Custom Spam 1",
		description = "Custom Spam 1"
	)
	default String customSpam1()
	{ return "cyan:Tempest"; }

	@ConfigItem(
		position = 5,
		keyName = "customSpam1Key",
		name = "Custom Spam 1 Hotkey",
		description = "The key to spam custom spam 1"
	)
	default ModifierlessKeybind customSpam1Key()
	{
		return new ModifierlessKeybind(KeyEvent.VK_F7, 0);
	}

	@ConfigItem(
		position = 6,
		keyName = "customSpam2",
		name = "Custom Spam 2",
		description = "Custom Spam 2"
	)
	default String customSpam2()
	{ return "flash2:wave:tempy on my tooth"; }

	@ConfigItem(
		position = 7,
		keyName = "customSpam2Key",
		name = "Custom Spam 2 Hotkey",
		description = "The key to spam custom spam 2"
	)
	default ModifierlessKeybind customSpam2Key()
	{
		return new ModifierlessKeybind(KeyEvent.VK_F8, 0);
	}

	@ConfigItem(
		position = 8,
		keyName = "customSpam3",
		name = "Custom Spam 3",
		description = "Custom Spam 3"
	)
	default String customSpam3()
	{ return "tempest in"; }

	@ConfigItem(
		position = 9,
		keyName = "customSpam3Key",
		name = "Custom Spam 3 Hotkey",
		description = "The key to spam custom spam 3"
	)
	default ModifierlessKeybind customSpam3Key()
	{
		return new ModifierlessKeybind(KeyEvent.VK_F9, 0);
	}

	@ConfigItem(
		position = 10,
		keyName = "spamTimer",
		name = "Spam Timer",
		description = "Prevents new messages from being sent until previous message is gone"
	)
	default boolean spamTimer()
	{ return true; }
}