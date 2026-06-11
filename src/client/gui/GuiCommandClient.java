package client.gui;

import data.Organization;
import network.CommandRequest;
import network.CommandResponse;
import network.Credentials;
import network.SerializationUtils;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public final class GuiCommandClient implements Closeable {
    private static final int RETRIES = 3;
    private static final int MAX_SCRIPT_DEPTH = 5;
    private static final Duration RESPONSE_TIMEOUT = Duration.ofSeconds(2);

    private final DatagramChannel channel;
    private final InetSocketAddress server;
    private Credentials credentials = Credentials.empty();

    public GuiCommandClient(String host, int port) throws Exception {
        this.server = new InetSocketAddress(host, port);
        this.channel = DatagramChannel.open();
        this.channel.configureBlocking(false);
    }

    public String username() {
        return credentials.username();
    }

    public CommandResponse login(String commandName, String username, String password) throws Exception {
        Credentials requestCredentials = Credentials.of(username, password);
        CommandResponse response = send(commandName, new String[]{username, password}, null, Map.of(), requestCredentials);
        if (response.isSuccess()) credentials = requestCredentials;
        return response;
    }

    public CommandResponse command(String commandName, String... args) throws Exception {
        return send(commandName, args, null, Map.of(), credentials);
    }

    public CommandResponse organizationCommand(String commandName, Organization organization, String... args) throws Exception {
        return send(commandName, args, organization, Map.of(), credentials);
    }

    public CommandResponse executeScript(Path path) throws Exception {
        Map<String, List<String>> scripts = new HashMap<>();
        loadScript(path.toString(), scripts, 1);
        return send("execute_script", new String[]{path.toString()}, null, scripts, credentials);
    }

    private void loadScript(String fileName, Map<String, List<String>> scripts, int depth) throws Exception {
        if (scripts.containsKey(fileName) || depth > MAX_SCRIPT_DEPTH) return;
        List<String> lines = Files.readAllLines(Path.of(fileName));
        scripts.put(fileName, lines);
        for (String rawLine : lines) {
            String line = rawLine.trim();
            if (line.isEmpty()) continue;
            String[] tokens = line.split("\\s+");
            if ("execute_script".equals(tokens[0]) && tokens.length >= 2) loadScript(tokens[1], scripts, depth + 1);
        }
    }

    private CommandResponse send(String commandName, String[] args, Organization organization,
                                 Map<String, List<String>> scripts, Credentials requestCredentials) throws Exception {
        CommandRequest request = new CommandRequest(commandName, args, organization, scripts, requestCredentials);
        Optional<CommandResponse> response = sendRequest(request);
        return response.orElseGet(() -> new CommandResponse(false, "Server is temporarily unavailable"));
    }

    private Optional<CommandResponse> sendRequest(CommandRequest request) throws Exception {
        byte[] requestBytes = SerializationUtils.serialize(request);
        ByteBuffer requestBuffer = ByteBuffer.wrap(requestBytes);
        for (int attempt = 0; attempt < RETRIES; attempt++) {
            requestBuffer.rewind();
            channel.send(requestBuffer, server);
            Optional<CommandResponse> response = waitForResponse();
            if (response.isPresent()) return response;
        }
        return Optional.empty();
    }

    private Optional<CommandResponse> waitForResponse() throws Exception {
        long deadline = System.currentTimeMillis() + RESPONSE_TIMEOUT.toMillis();
        ByteBuffer responseBuffer = ByteBuffer.allocate(SerializationUtils.BUFFER_SIZE);
        while (System.currentTimeMillis() < deadline) {
            SocketAddress address = channel.receive(responseBuffer);
            if (address != null) return Optional.of(readResponse(responseBuffer));
            Thread.sleep(50);
        }
        return Optional.empty();
    }

    private CommandResponse readResponse(ByteBuffer responseBuffer) throws Exception {
        responseBuffer.flip();
        byte[] responseBytes = new byte[responseBuffer.remaining()];
        responseBuffer.get(responseBytes);
        Object object = SerializationUtils.deserialize(responseBytes, responseBytes.length);
        if (object instanceof CommandResponse response) return response;
        return new CommandResponse(false, "Error: invalid response object");
    }

    public void close() throws IOException {
        channel.close();
    }
}
