package client.gui;

import data.Organization;
import network.CommandResponse;

import java.nio.file.Path;
import java.util.List;
import java.util.function.Consumer;
import javax.swing.SwingWorker;

public final class MainController {
    private final GuiCommandClient client;
    private final Consumer<List<Organization>> organizations;
    private final Consumer<String> status;

    public MainController(GuiCommandClient client, Consumer<List<Organization>> organizations, Consumer<String> status) {
        this.client = client;
        this.organizations = organizations;
        this.status = status;
    }

    public String username() {
        return client.username();
    }

    public void refresh() {
        run(() -> client.command("show"), response -> organizations.accept(response.getOrganizations()), false);
    }

    public void simple(String command, Runnable after, String... args) {
        run(() -> client.command(command, args), response -> {
            if (after != null) after.run();
            refresh();
        }, true);
    }

    public void withOrganization(String command, Organization organization, Runnable after, String... args) {
        run(() -> client.organizationCommand(command, organization, args), response -> {
            if (after != null) after.run();
            refresh();
        }, true);
    }

    public void commandMessage(String command, Consumer<String> messageConsumer, String... args) {
        run(() -> client.command(command, args), response -> messageConsumer.accept(response.getMessage()), true);
    }

    public void executeScript(Path path) {
        run(() -> client.executeScript(path), response -> refresh(), true);
    }

    public void close() {
        try {
            client.close();
        } catch (Exception ignored) {
        }
    }

    private void run(Task task, Consumer<CommandResponse> success, boolean reportSuccess) {
        new SwingWorker<CommandResponse, Void>() {
            protected CommandResponse doInBackground() throws Exception {
                return task.execute();
            }

            protected void done() {
                try {
                    CommandResponse response = get();
                    if (reportSuccess || !response.isSuccess()) status.accept(response.getMessage());
                    if (response.isSuccess()) success.accept(response);
                } catch (Exception e) {
                    status.accept(e.getMessage());
                }
            }
        }.execute();
    }

    @FunctionalInterface
    private interface Task {
        CommandResponse execute() throws Exception;
    }
}
