package net.runelite.client.plugins.warspammer;

import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.runelite.api.Client;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.input.KeyListener;
import net.runelite.client.ui.overlay.OverlayManager;

@Singleton
class WarspammerListener implements KeyListener
{
	@Inject
	private Client client;

	@Inject
	private ClientThread clientThread;

	@Inject
	private WarspammerConfig config;

	@Inject
	private WarspammerPlugin plugin;

	@Inject
	private OverlayManager overlayManager;

	private final Map<Integer, Integer> modified = new HashMap<>();
	private final Set<Character> blockedChars = new HashSet<>();

	@Override
	public void keyTyped(KeyEvent e)
	{
		char keyChar = e.getKeyChar();
		if (keyChar != KeyEvent.CHAR_UNDEFINED && blockedChars.contains(keyChar))
		{
			e.consume();
		}
	}

	@Override
	public void keyPressed(KeyEvent e)
	{
		if (config.pileSpamKey().matches(e) && config.pileSpam())
		{
			String target = plugin.getTargetName();
			clientThread.invoke(() ->
				plugin.writeString("te" + " " + target)
			);
		}
		else if (config.customSpam1Key().matches(e))
		{
			clientThread.invoke(() ->
				plugin.writeString(config.customSpam1())
			);
		}
		else if (config.customSpam2Key().matches(e))
		{
			clientThread.invoke(() ->
				plugin.writeString(config.customSpam2())
			);
		}
		else if (config.customSpam3Key().matches(e))
		{
			clientThread.invoke(() ->
				plugin.writeString(config.customSpam3())
			);
		}
	}

	@Override
	public void keyReleased(KeyEvent e)
	{
		final int keyCode = e.getKeyCode();
		final char keyChar = e.getKeyChar();

		if (keyChar != KeyEvent.CHAR_UNDEFINED)
		{
			blockedChars.remove(keyChar);
		}

		final Integer mappedKeyCode = modified.remove(keyCode);
		if (mappedKeyCode != null)
		{
			e.setKeyCode(mappedKeyCode);
			e.setKeyChar(KeyEvent.CHAR_UNDEFINED);
		}
	}
}