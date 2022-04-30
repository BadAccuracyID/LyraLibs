package id.luckynetwork.lyrams.lyralibs.bukkit.menus.menu;

import id.luckynetwork.lyrams.lyralibs.bukkit.menus.slots.Slot;
import id.luckynetwork.lyrams.lyralibs.bukkit.menus.slots.pages.NextPageSlot;
import id.luckynetwork.lyrams.lyralibs.bukkit.menus.slots.pages.PageSlot;
import id.luckynetwork.lyrams.lyralibs.bukkit.menus.slots.pages.PreviousPageSlot;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public abstract class SwitchableMenu extends LuckyMenu {

    @Getter
    private int page = 1;

    @Override
    public List<Slot> getSlots(Player player) {
        List<Slot> slots = new ArrayList<>();

        this.onPreOpen();

        int minSlot = (int) ((double) (page - 1) * 21) + 1;
        int maxSlot = (int) ((double) (page) * 21);

        slots.add(new NextPageSlot(this));
        slots.add(new PreviousPageSlot(this));
        slots.add(new PageSlot(this, 4));

        if (this.getEveryMenuSlots(player) != null) {
            this.getEveryMenuSlots(player).forEach(slot -> {
                slots.removeIf(s -> s.hasSlot(slot.getSlot()));
            });
            slots.addAll(this.getEveryMenuSlots(player));
        }

        int index = 1, added = 0, currentSlot = 0;
        for (Slot slot : this.getSwitchableSlots(player)) {
            int current = index++;

            switch (currentSlot) {
                case 16:
                case 25: {
                    index += 2;
                    added += 2;
                    current += 2;
                    break;
                }
            }

            if (current >= minSlot && current <= maxSlot + added) {
                current -= (int) ((double) (21) * (page - 1)) - 9;
                currentSlot = current;

                slots.add(this.getNewSlot(slot, current));
            }
        }

        for (int i = 0; i < 45; i++) {
            if (!Slot.hasSlot(slots, i)) {
                slots.add(Slot.getGlass(i));
            }
        }

        return slots;
    }

    private Slot getNewSlot(Slot slot, int s) {
        return new Slot() {
            @Override
            public ItemStack getItem(Player player) {
                return slot.getItem(player);
            }

            @Override
            public int getSlot() {
                return s;
            }

            @Override
            public void onClick(Player player, int s, ClickType clickType) {
                slot.onClick(player, s, clickType);
            }
        };
    }

    @Override
    public String getName(Player player) {
        return this.getPagesTitle(player);
    }

    public void changePage(Player player, int page) {
        this.page += page;
        this.getSlots().clear();
        this.update(player);
    }

    public int getPages(Player player) {
        if (this.getSwitchableSlots(player).isEmpty()) {
            return 1;
        }
        return (int) Math.ceil(getSwitchableSlots(player).size() / (double) 21);
    }

    public void onPreOpen() {

    }

    public abstract String getPagesTitle(Player player);

    public abstract List<Slot> getSwitchableSlots(Player player);

    public abstract List<Slot> getEveryMenuSlots(Player player);
}
