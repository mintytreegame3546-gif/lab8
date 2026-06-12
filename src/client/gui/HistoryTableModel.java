package client.gui;

import data.CommandHistoryRecord;

import java.text.DateFormat;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import javax.swing.table.AbstractTableModel;

@SuppressWarnings("serial")
public final class HistoryTableModel extends AbstractTableModel {
    private final LocaleManager localeManager;
    private List<CommandHistoryRecord> history = List.of();

    public HistoryTableModel(LocaleManager localeManager) {
        this.localeManager = localeManager;
    }

    public void setHistory(List<CommandHistoryRecord> history) {
        this.history = List.copyOf(history);
        fireTableDataChanged();
    }

    public int getRowCount() { return history.size(); }
    public int getColumnCount() { return HistoryColumn.count(); }

    public String getColumnName(int column) {
        return HistoryColumn.isValid(column) ? column(column).localizedName(localeManager) : "";
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        CommandHistoryRecord record = history.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> record.getId();
            case 1 -> record.getUsername();
            case 2 -> record.getCommandName();
            case 3 -> formatTimestamp(record);
            case 4 -> localeManager.text(record.isSuccess() ? "history.success" : "history.failure");
            default -> "";
        };
    }

    public void localeChanged() {
        fireTableStructureChanged();
    }

    private String formatTimestamp(CommandHistoryRecord record) {
        DateFormat format = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT,
                localeManager.locale());
        Date date = Date.from(record.getExecutedAt().atZone(ZoneId.systemDefault()).toInstant());
        return format.format(date);
    }

    private HistoryColumn column(int column) {
        return HistoryColumn.at(column);
    }

    private enum HistoryColumn {
        ID("history.id"),
        USER("history.user"),
        COMMAND("history.command"),
        TIMESTAMP("history.timestamp"),
        STATUS("history.status");

        private static final HistoryColumn[] COLUMNS = values();
        private final String textKey;

        HistoryColumn(String textKey) {
            this.textKey = textKey;
        }

        static int count() {
            return COLUMNS.length;
        }

        static boolean isValid(int index) {
            return index >= 0 && index < COLUMNS.length;
        }

        static HistoryColumn at(int index) {
            return COLUMNS[index];
        }

        String localizedName(LocaleManager localeManager) {
            return localeManager.text(textKey);
        }
    }
}
