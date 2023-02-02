package me.zombie_striker.gsl;

import me.zombie_striker.gsl.commands.*;
import org.bukkit.plugin.java.JavaPlugin;

public final class GSLCore extends JavaPlugin {

    @Override
    public void onEnable() {
        new GSL(this).init();
        NamelayerCreateGroupCommand nlcg = new NamelayerCreateGroupCommand();
        getCommand("nl").setExecutor(new NamelayerCommand());

        getCommand("nlcg").setExecutor(nlcg);

        TradeButtonCommand tbc = new TradeButtonCommand();
        getCommand("tb").setExecutor(tbc);
        getCommand("tb").setTabCompleter(tbc);

        NamelayerMergeCommand nlmergec = new NamelayerMergeCommand();
        getCommand("nlmerge").setExecutor(nlmergec);
        getCommand("nlmerge").setTabCompleter(nlmergec);

        NamelayerDisbandCommand nldiband = new NamelayerDisbandCommand();
        getCommand("nldisband").setExecutor(nldiband);
        getCommand("nldisband").setTabCompleter(nldiband);

        NamelayerInviteCommand nlinvite = new NamelayerInviteCommand();
        getCommand("nlinvite").setExecutor(nlinvite);
        getCommand("nlinvite").setTabCompleter(nlinvite);

        NamelayerAcceptCommand nlaccept = new NamelayerAcceptCommand();
        getCommand("nlaccept").setExecutor(nlaccept);
        getCommand("nlaccept").setTabCompleter(nlaccept);

        OTPCommand otp = new OTPCommand();
        getCommand("otp").setExecutor(otp);

        GammaCommand gamma = new GammaCommand();
        getCommand("gamma").setExecutor(gamma);

        ReinforceCommand rc = new ReinforceCommand();
        getCommand("r").setExecutor(rc);
        getCommand("r").setTabCompleter(rc);

        GroupCommand gc = new GroupCommand();
        getCommand("g").setExecutor(gc);
        getCommand("g").setTabCompleter(gc);

        NamelayerCreateAd nlad = new NamelayerCreateAd();
        getCommand("nlad").setExecutor(nlad);
        getCommand("nlad").setTabCompleter(nlad);

        NationsCommand nations = new NationsCommand();
        getCommand("nations").setExecutor(nations);
    }

    @Override
    public void onDisable() {
        GSL.getApi().shutdown();
    }
}
