package com.github.neapovil.oldpvp;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.MultiLiteralArgument;

public final class OldPvp extends JavaPlugin implements Listener
{
    private static OldPvp instance;
    private boolean enabled = false;

    @Override
    public void onEnable()
    {
        instance = this;

        this.getServer().getPluginManager().registerEvents(this, this);

        new CommandAPICommand("oldpvp")
                .withPermission("oldpvp.command")
                .withArguments(new MultiLiteralArgument("on", "off"))
                .executesPlayer((sender, args) -> {
                    if (((String) args[0]).equals("on"))
                    {
                        enabled = true;
                    }
                    else
                    {
                        enabled = false;
                    }

                    this.getServer().getOnlinePlayers().forEach(p -> this.updateAttackSpeed(p));

                    sender.sendMessage("Old PvP: " + args[0]);
                })
                .register();
    }

    @Override
    public void onDisable()
    {
    }

    public static OldPvp getInstance()
    {
        return instance;
    }

    @EventHandler
    private void playerJoin(PlayerJoinEvent event)
    {
        this.updateAttackSpeed(event.getPlayer());
    }

    @EventHandler
    private void playerQuit(PlayerQuitEvent event)
    {
        this.updateAttackSpeed(event.getPlayer());
    }

    private void updateAttackSpeed(Player player)
    {
        final AttributeInstance attribute = player.getAttribute(Attribute.GENERIC_ATTACK_SPEED);

        if (attribute == null)
        {
            return;
        }

        if (enabled)
        {
            attribute.setBaseValue(30D);
        }
        else
        {
            attribute.setBaseValue(attribute.getDefaultValue());
        }
    }
}
