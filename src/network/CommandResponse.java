package network;

import data.CommandHistoryRecord;
import data.Organization;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings("serial")
public class CommandResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    private final boolean success;
    private final String message;
    private final List<Organization> organizations;
    private final List<CommandHistoryRecord> history;

    public CommandResponse(boolean success, String message) {
        this(success, message, List.of(), List.of());
    }

    public CommandResponse(boolean success, String message, List<Organization> organizations) {
        this(success, message, organizations, List.of());
    }

    public CommandResponse(boolean success, String message, List<Organization> organizations,
                           List<CommandHistoryRecord> history) {
        this.success = success;
        this.message = message;
        this.organizations = new ArrayList<>(organizations);
        this.history = new ArrayList<>(history);
    }

    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public List<Organization> getOrganizations() { return Collections.unmodifiableList(organizations); }
    public List<CommandHistoryRecord> getHistory() { return Collections.unmodifiableList(history); }
}
