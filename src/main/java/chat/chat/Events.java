package chat.chat;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

import static net.kyori.adventure.text.event.ClickEvent.suggestCommand;

public class Events implements Listener {
    public String message = "test";
    //private main plugin;
    Events(Chat chat) {
        //	this.plugin = plugin;
    }

    @EventHandler
    public void onChat(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        Component text = Component.text(ChatColor.YELLOW + e.getPlayer().getName() + " залетел на сервер");
        text = text.hoverEvent(HoverEvent.showText(Component.text(cmdf.hoverPlayer(p)))).clickEvent(suggestCommand("/w " + p.getName()));
        e.joinMessage(text);
    }
    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        FileConfiguration config = Chat.getInstance().getConfig();
        if (config.getBoolean("options.deaths")) {
            Component death_original = e.deathMessage();
            Player p = e.getPlayer();
            String[] death_message_no = {"Полетел в рай", "Спустился в ад", "Не смог избежать смерти", "Был рассыплен впрах", "Разрушен окончательно", "Сдох", "Принял свою участь", "Рассечён жестокой реальностью"};
            String[] death_message_pl = {"<player> Не увернулся и получил последний удар от <killer>","<killer> свершил последнюю волю <player>","<killer> был единственным свидетелем смерти <player>","<killer> последним ударом отсёк голову <player>"};
            Random rand = new Random();
            int rand_int = rand.nextInt(death_message_no.length);
            int rand_int_pl = rand.nextInt(death_message_pl.length);
            String final_death = "<player> " + death_message_no[rand_int];
            if (p.getKiller() != null) {
                final_death = death_message_pl[rand_int_pl].replace("killer",p.getKiller().getName());
            }
            final_death = final_death.replace("<","").replace(">","").replace("player",p.getName());
            Component text = Component.text(ChatColor.RED + final_death).hoverEvent(death_original);
            e.deathMessage(text);

        }
    }
}