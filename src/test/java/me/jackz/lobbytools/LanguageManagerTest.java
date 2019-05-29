package me.jackz.lobbytools;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(PlayerJoinEvent.class)
public final class LanguageManagerTest {
    @Test
    public void testPlayerJoin() {
        PlayerJoinEvent mockEvent = mock(PlayerJoinEvent.class);
        Main mockMain = mock(Main.class);
        Player player = mock(Player.class);
        Inventory inv = mock(PlayerInventory.class);
        inv.setItem(0,new ItemStack(Material.STONE));

        when(player.getName()).thenReturn("test");
        when(player.getGameMode()).thenReturn(GameMode.CREATIVE);
        when(player.getInventory()).thenAnswer()

        when(mockEvent.getPlayer()).thenReturn(player);


        JoinEvent joinEvent = new JoinEvent(mockMain);
        joinEvent.onJoin(mockEvent);

        verify(player).sendMessage(anyString());
    }
}
