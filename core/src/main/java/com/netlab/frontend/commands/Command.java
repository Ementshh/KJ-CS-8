package com.netlab.frontend.commands;

import com.netlab.frontend.objects.Player;
import com.netlab.frontend.systems.BulletManager;

public interface Command {
    void execute(Player player, BulletManager bulletManager);
}
