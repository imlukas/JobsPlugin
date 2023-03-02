package me.imlukas.jobsplugin.utils.menu.base;


import io.papermc.paper.adventure.PaperAdventure;
import me.imlukas.jobsplugin.utils.concurrent.MainThreadExecutor;
import me.imlukas.jobsplugin.utils.item.ItemUtil;
import me.imlukas.jobsplugin.utils.menu.element.MenuElement;
import me.imlukas.jobsplugin.utils.menu.element.Renderable;
import net.kyori.adventure.text.Component;
import net.minecraft.network.protocol.game.ClientboundOpenScreenPacket;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftPlayer;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class BaseMenu implements InventoryHolder {

    private Inventory inventory;
    private final UUID destinationPlayerId;

    private final net.kyori.adventure.text.Component title;

    private final List<Renderable> renderables = new ArrayList<>();
    private final Map<Integer, MenuElement> elements = new HashMap<>();

    private Runnable closeTask;

    private boolean allowRemoveItems = false;


    public BaseMenu(UUID playerId, Component title, int rows) {
        this.title = title;
        this.inventory = Bukkit.createInventory(this, rows * 9, title);
        this.destinationPlayerId = playerId;
    }

    public BaseMenu(UUID playerId, String title, int rows) {
        this(playerId, net.kyori.adventure.text.Component.text(title), rows);
    }

    public Component getTitle() {
        return title;
    }

    public void setTitle(Component title) {
        List<HumanEntity> viewers = new ArrayList<>(inventory.getViewers());

        if (viewers.isEmpty()) {
            inventory = Bukkit.createInventory(this, inventory.getSize(), title);
            forceUpdate();
            return;
        }

        for (HumanEntity viewer : viewers) {
            Player player = (Player) viewer;
            ServerPlayer nmsPlayer = ((CraftPlayer) player).getHandle();

            ClientboundOpenScreenPacket packet = new ClientboundOpenScreenPacket(nmsPlayer.containerMenu.containerId, nmsPlayer.containerMenu.getType(),
                    PaperAdventure.asVanilla(title));

            nmsPlayer.connection.send(packet);
        }
    }

    public void setTitle(String title) {
        setTitle(net.kyori.adventure.text.Component.text(title));
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }

    public void open() {
        if (!Bukkit.isPrimaryThread()) {
            MainThreadExecutor.MAIN_THREAD_EXECUTOR.execute(this::open);
            return;
        }

        Player player = getPlayer();

        if (player == null) {
            return;
        }

        player.openInventory(inventory);
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(destinationPlayerId);
    }

    public void addRenderable(Renderable... renderable) {
        renderables.addAll(List.of(renderable));
    }

    public void forceUpdate() {
        Player player = getPlayer();

        if (player == null) {
            return;
        }

        for (Renderable renderable : renderables) {
            if (renderable.isActive()) {
                renderable.forceUpdate();
            }
        }

        for (Map.Entry<Integer, MenuElement> entry : elements.entrySet()) {
            int slot = entry.getKey();
            MenuElement element = entry.getValue();

            ItemStack item = element.getDisplayItem().clone();

            ItemUtil.replacePlaceholder(item, player, element.getItemPlaceholders());
            inventory.setItem(slot, item);
        }
    }

    public void setElement(int slot, MenuElement element) {
        elements.put(slot, element);
    }

    public void setAllowRemoveItems(boolean allowRemoveItems) {
        this.allowRemoveItems = allowRemoveItems;
    }

    public void onClose(Runnable task) {
        this.closeTask = task;
    }

    public boolean isAllowRemoveItems() {
        return allowRemoveItems;
    }

    public void handleClick(InventoryClickEvent event) {
        int slot = event.getRawSlot();

        if (slot < 0 || slot >= inventory.getSize()) {
            if (event.isShiftClick()) {
                event.setCancelled(true);
            }
            return;
        }

        MenuElement element = elements.get(slot);

        if (element == null) {
            return;
        }

        element.handle(event);

        if (!allowRemoveItems)
            event.setCancelled(true);
    }

    public void handleClose() {
        if (closeTask != null)
            closeTask.run();
    }
}
