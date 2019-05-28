package me.jackz.lobbytools;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(PlayerJoinEvent.class)
public class LanguageManagerTest extends JavaPlugin {
    @Test
    public void testPlayerJoin() {
        PlayerJoinEvent mockEvent = mock(PlayerJoinEvent.class);
        Player player = mock(Player.class);
        when(player.getName()).thenReturn("test");
        when(player.getGameMode()).thenReturn(GameMode.CREATIVE);
        when(mockEvent.getPlayer()).thenReturn(player);

        JoinEvent joinEvent = new JoinEvent();
        joinEvent.onJoin(mockEvent);

        verify(player).sendMessage(anyString());
    }
}
