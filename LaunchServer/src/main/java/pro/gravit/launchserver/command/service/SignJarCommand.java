package pro.gravit.launchserver.command.service;

import pro.gravit.launchserver.LaunchServer;
import pro.gravit.launchserver.binary.tasks.SignJarTask;
import pro.gravit.launchserver.binary.tasks.TaskUtil;
import pro.gravit.launchserver.command.Command;
import pro.gravit.utils.helper.LogHelper;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SignJarCommand extends Command {
    public SignJarCommand(LaunchServer server) {
        super(server);
    }

    @Override
    public String getArgsDescription() {
        return "[path to file] (path to signed file)";
    }

    @Override
    public String getUsageDescription() {
        return "sign custom jar";
    }

    @Override
    public void invoke(String... args) throws Exception {
        verifyArgs(args, 1);
        Path target = Paths.get(args[0]);
        Path tmpSign;
        if(args.length > 1)
            tmpSign = Paths.get(args[1]);
        else
            tmpSign = server.dir.resolve("build").resolve(target.toFile().getName());
        LogHelper.info("Signing jar %s to %s", target.toString(), tmpSign.toString());
        SignJarTask task = (SignJarTask) TaskUtil.getTaskByClass(server.launcherBinary.tasks, SignJarTask.class);
        task.sign(server.config.sign, target, tmpSign);
        if(args.length <= 1)
        {
            LogHelper.info("Move temp jar %s to %s", tmpSign.toString(), target.toString());
            Files.deleteIfExists(target);
            Files.move(tmpSign, target);
        }
        LogHelper.info("Success signed");
    }
}
