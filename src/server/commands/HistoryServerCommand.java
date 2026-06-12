package server.commands;

import data.CommandHistoryRecord;
import network.CommandRequest;
import network.CommandResponse;
import server.db.DatabaseManager;

import java.util.List;

public class HistoryServerCommand implements ServerCommand {
    private final DatabaseManager databaseManager;

    public HistoryServerCommand(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    public String getName() { return "history"; }
    public String getDescription() { return "Display your command execution history"; }

    public CommandResponse execute(CommandRequest request) throws Exception {
        String usernameError = ServerCommandSupport.requireUsername(request);
        if (usernameError != null) return new CommandResponse(false, usernameError);
        List<CommandHistoryRecord> history = databaseManager.loadCommandHistory(request.getUsername());
        return new CommandResponse(true, "History loaded", List.of(), history);
    }
}
