package data;

import java.io.Serializable;
import java.time.LocalDateTime;

@SuppressWarnings("serial")
public final class CommandHistoryRecord implements Serializable {
    private static final long serialVersionUID = 1L;

    private final long id;
    private final String username;
    private final String commandName;
    private final LocalDateTime executedAt;
    private final boolean success;

    public CommandHistoryRecord(long id, String username, String commandName, LocalDateTime executedAt, boolean success) {
        this.id = id;
        this.username = username;
        this.commandName = commandName;
        this.executedAt = executedAt;
        this.success = success;
    }

    public long getId() { return id; }
    public String getUsername() { return username; }
    public String getCommandName() { return commandName; }
    public LocalDateTime getExecutedAt() { return executedAt; }
    public boolean isSuccess() { return success; }
}
