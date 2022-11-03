package me.zombie_striker.gsl;

import org.bukkit.plugin.java.JavaPlugin;

import me.zombie_striker.gsl.commands.GroupCommand;
import me.zombie_striker.gsl.commands.NamelayerCreateGroupCommand;
import me.zombie_striker.gsl.commands.ReinforceCommand;

public final class GSLCore extends JavaPlugin {

    @Override
    public void onEnable() {
        new GSL(this).init();
        NamelayerCreateGroupCommand nlcg = new NamelayerCreateGroupCommand();
        getCommand("nlcg").setExecutor(nlcg);

        ReinforceCommand rc = new ReinforceCommand();
        getCommand("r").setExecutor(rc);
        getCommand("r").setTabCompleter(rc);

        GroupCommand gc = new GroupCommand();
        getCommand("g").setExecutor(gc);
        getCommand("g").setTabCompleter(gc);
    }

    @Override
    public void onDisable() {
        GSL.getApi().shutdown();
    }
}
