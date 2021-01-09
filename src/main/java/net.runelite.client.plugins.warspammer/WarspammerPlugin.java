package net.runelite.client.plugins.warspammer;

import com.google.inject.Provides;
import java.awt.AWTException;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.Robot;
import javax.inject.Inject;
import java.util.concurrent.TimeUnit;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.VarClientStr;
import net.runelite.api.Varbits;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.input.KeyManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.JagexColors;
import net.runelite.client.util.ColorUtil;

@PluginDescriptor(
		name = "War Spammer",
		description = "War spamming utils"
)

public class WarspammerPlugin extends Plugin
{
	private static final String PRESS_ENTER_TO_CHAT = "Press Enter to Chat...";
	private static String OS = System.getProperty("os.name").toLowerCase();

	@Inject
	private Client client;

	@Inject
	private ClientThread clientThread;

	@Inject
	private WarspammerConfig config;

	@Inject
	private WarspammerListener inputListener;

	@Inject
	private KeyManager keyManager;

	@Getter(AccessLevel.PACKAGE)
	@Setter(AccessLevel.PACKAGE)
	private boolean initial = true;
	private int lastSendTick;

	@Provides
	WarspammerConfig getConfig(ConfigManager configManager)
	{
		return configManager.getConfig(WarspammerConfig.class);
	}

	@Override
	protected void startUp()
	{
		keyManager.registerKeyListener(inputListener);

		clientThread.invoke(() ->
		{
			if (client.getGameState() == GameState.LOGGED_IN)
			{
				lockChat();
				// Clear any typed text
				client.setVar(VarClientStr.CHATBOX_TYPED_TEXT, "");
			}
		});
	}

	@Override
	protected void shutDown()
	{
		clientThread.invoke(() ->
		{
			if (client.getGameState() == GameState.LOGGED_IN)
			{
				unlockChat();
			}
		});

		keyManager.unregisterKeyListener(inputListener);
	}

	void lockChat()
	{
		Widget chatboxInput = client.getWidget(WidgetInfo.CHATBOX_INPUT);
		if (chatboxInput != null)
		{
			setChatboxWidgetInput(chatboxInput, PRESS_ENTER_TO_CHAT);
		}
	}

	void unlockChat()
	{
		Widget chatboxInput = client.getWidget(WidgetInfo.CHATBOX_INPUT);
		if (chatboxInput != null && client.getGameState() == GameState.LOGGED_IN)
		{
			final boolean isChatboxTransparent = client.isResized() && client.getVar(Varbits.TRANSPARENT_CHATBOX) == 1;
			final Color textColor = isChatboxTransparent ? JagexColors.CHAT_TYPED_TEXT_TRANSPARENT_BACKGROUND : JagexColors.CHAT_TYPED_TEXT_OPAQUE_BACKGROUND;
			setChatboxWidgetInput(chatboxInput, ColorUtil.wrapWithColorTag(client.getVar(VarClientStr.CHATBOX_TYPED_TEXT) + "*", textColor));
		}
	}

	void setChatboxWidgetInput(Widget widget, String input)
	{
		String text = widget.getText();
		int idx = text.indexOf(':');
		if (idx != -1)
		{
			String newText = text.substring(0, idx) + ": " + input;
			widget.setText(newText);
		}
	}

	public void writeString(String s)
	{
		int currentTick = client.getTickCount();
		if (currentTick - lastSendTick >= 5 || initial || !config.spamTimer())
		{
			try
			{
				Robot r = new Robot();
				for (char c : s.toCharArray())
				{
					int code = KeyEvent.getExtendedKeyCodeForChar(c);
					if (Character.isUpperCase(c) || isShiftChar(c))
						r.keyPress(KeyEvent.VK_SHIFT);
					if(code == 513 && OS.indexOf("win") >= 0){
						r.keyPress(KeyEvent.VK_SEMICOLON);
						r.keyRelease(KeyEvent.VK_SEMICOLON);
					}else if(code == 517 && OS.indexOf("win") >= 0){
						r.keyPress(KeyEvent.VK_1);
						r.keyRelease(KeyEvent.VK_1);
					}else if(code == 512 && OS.indexOf("win") >= 0){
						r.keyPress(KeyEvent.VK_QUOTE);
						r.keyRelease(KeyEvent.VK_QUOTE);
					}else{
						r.keyPress(code);
						r.keyRelease(code);
					}
					if (Character.isUpperCase(c) || isShiftChar(c))
						r.keyRelease(KeyEvent.VK_SHIFT);
					try
					{
						TimeUnit.MILLISECONDS.sleep(10);
					}
					catch (InterruptedException ie)
					{
						Thread.currentThread().interrupt();
					}
				}
				if(!config.manuallyEnter()) {
					r.keyPress(KeyEvent.VK_ENTER);
					r.keyRelease(KeyEvent.VK_ENTER);
				}
				initial = false;
				lastSendTick = currentTick;
			}
			catch (AWTException ex)
			{
				ex.printStackTrace();
			}
		}
	}

	public String getTargetName()
	{
		try
		{
			String name = client.getLocalPlayer().getInteracting().getName();
			int charCount = config.pileCharCount();
			String targetName = name.substring(0, 1).toLowerCase() + name.substring(1, Math.min(name.length(), charCount));
			return targetName;
		}
		catch
		(Exception e)
		{
			e.printStackTrace();
		}
		return "";
	}

	private boolean isShiftChar(char c)
	{
		switch (c)
		{
			case '~':
			case '!':
			case '@':
			case '#':
			case '$':
			case '&':
			case '*':
			case '(':
			case ')':
			case '_':
			case '+':
			case '{':
			case '}':
			case ':':
			case '"':
			case '<':
			case '>':
				return true;
			default:
				return false;
		}
	}
}

