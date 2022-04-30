package id.luckynetwork.lyrams.lyralibs.bukkit.menus.menu;

import id.luckynetwork.lyrams.lyralibs.bukkit.LyraLibsBukkit;
import id.luckynetwork.lyrams.lyralibs.bukkit.menus.slots.Slot;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public abstract class LuckyMenu {

    @Getter
    private List<Slot> slots = new ArrayList<>();
    @Setter
    @Getter
    private boolean updateInTask = false;

    public abstract List<Slot> getSlots(Player player);

    public abstract String getName(Player player);

    public void open(Player player) {
        LuckyMenu previous = LyraLibsBukkit.getMenuManager().getOpenedMenus().get(player.getUniqueId());
        if (previous != null) {
            previous.onClose(player);
            LyraLibsBukkit.getMenuManager().getOpenedMenus().remove(player.getUniqueId());
        }

        this.slots = this.getSlots(player);
        String title = this.getName(player);

        Inventory inventory = Bukkit.createInventory(player, this.getInventorySize(this.slots), title);
        this.slots.forEach(slot -> {
            inventory.setItem(slot.getSlot(), slot.getItem(player));
            if (slot.getSlots() != null) {
                Arrays.stream(slot.getSlots()).forEach(extra -> inventory.setItem(extra, slot.getItem(player)));
            }
        });

        if (player.getOpenInventory() != null) {
            player.closeInventory();
        }

        LyraLibsBukkit.getMenuManager().getOpenedMenus().put(player.getUniqueId(), this);
        player.openInventory(inventory);

        this.onOpen(player);
    }

    public void update(Player player) {
        this.slots = this.getSlots(player);
        String title = this.getName(player);

        boolean passed = false;
        Inventory inventory = null;
        LuckyMenu openedMenu = LyraLibsBukkit.getMenuManager().getOpenedMenus().get(player.getUniqueId());
        Inventory current = player.getOpenInventory().getTopInventory();

        if (openedMenu != null && openedMenu.getName(player).equals(current.getTitle()) && current.getSize() == this.getInventorySize(this.slots)) {
            passed = true;
            inventory = current;
        }

        if (inventory == null) {
            inventory = Bukkit.createInventory(player, this.getInventorySize(this.slots), title);
        }

        /**
         * TemporaryInventory
         * Used to prevent item flickering because 'inventory' is live player inventory
         */
        Inventory temporaryInventory = Bukkit.createInventory(player, inventory.getSize(), title);

        this.slots.forEach(slot -> {
            temporaryInventory.setItem(slot.getSlot(), slot.getItem(player));

            if (slot.getSlots() != null) {
                Arrays.stream(slot.getSlots()).forEach(extra -> {
                    temporaryInventory.setItem(extra, slot.getItem(player));
                });
            }
        });

        LyraLibsBukkit.getMenuManager().getOpenedMenus().remove(player.getUniqueId());
        LyraLibsBukkit.getMenuManager().getOpenedMenus().put(player.getUniqueId(), this);

        inventory.setContents(temporaryInventory.getContents());

        if (passed) {
            player.updateInventory();
        } else {
            player.openInventory(inventory);
        }
    }

    private int getInventorySize(List<Slot> slots) {
        int highest = 0;
        if (!slots.isEmpty()) {
            highest = slots.stream().sorted(Comparator.comparingInt(Slot::getSlot).reversed()).map(Slot::getSlot).collect(Collectors.toList()).get(0);
        }
        for (Slot slot : slots) {
            if (slot.getSlots() != null) {
                for (int i = 0; i < slot.getSlots().length; i++) {
                    if (slot.getSlots()[i] > highest) {
                        highest = slot.getSlots()[i];
                    }
                }
            }
        }
        return (int) (Math.ceil((highest + 1) / 9D) * 9D);
    }

    public boolean hasSlot(int value) {
        return this.slots.stream()
                .filter(slot -> slot.getSlot() == value || slot.getSlots() != null && Arrays.stream(slot.getSlots()).anyMatch(i -> i == value))
                .findFirst().orElse(null) != null;
    }

    public Slot getSlot(int value) {
        return this.slots.stream()
                .filter(slot -> slot.getSlot() == value || slot.getSlots() != null && Arrays.stream(slot.getSlots()).anyMatch(i -> i == value))
                .findFirst().orElse(null);
    }

    public void onOpen(Player player) {
    }

    public void onClose(Player player) {
        LyraLibsBukkit.getMenuManager().getLastOpenedMenus().remove(player.getUniqueId());
        LyraLibsBukkit.getMenuManager().getLastOpenedMenus().put(player.getUniqueId(), this);
    }
}
