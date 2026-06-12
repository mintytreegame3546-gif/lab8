package client.gui;

import data.Organization;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import javax.swing.table.AbstractTableModel;

@SuppressWarnings("serial")
public final class OrganizationTableModel extends AbstractTableModel {
    private final LocaleManager localeManager;
    private List<Organization> source = List.of();
    private List<Organization> visible = List.of();
    private String filter = "";
    private int sortColumn = -1;
    private boolean ascending = true;

    public OrganizationTableModel(LocaleManager localeManager) {
        this.localeManager = localeManager;
    }

    public void setOrganizations(List<Organization> organizations) {
        source = List.copyOf(organizations);
        rebuild();
    }

    public List<Organization> organizations() {
        return Collections.unmodifiableList(visible);
    }

    public Organization organizationAt(int row) {
        return visible.get(row);
    }

    public int rowById(long id) {
        for (int row = 0; row < visible.size(); row++) {
            if (visible.get(row).getId() == id) return row;
        }
        return -1;
    }

    public void setFilter(String filter) {
        this.filter = filter == null ? "" : filter.toLowerCase(localeManager.locale());
        rebuild();
    }

    public void sortBy(int column) {
        if (sortColumn == column) ascending = !ascending;
        else {
            sortColumn = column;
            ascending = true;
        }
        rebuild();
    }

    public double turnoverSum() {
        return visible.stream().mapToDouble(Organization::getAnnualTurnover).sum();
    }

    public int getRowCount() { return visible.size(); }
    public int getColumnCount() { return OrganizationTableColumn.count(); }

    public String getColumnName(int column) {
        return OrganizationTableColumn.isValid(column) ? column(column).localizedName(localeManager) : "";
    }

    public void localeChanged() {
        fireTableStructureChanged();
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        Organization organization = visible.get(rowIndex);
        Locale locale = localeManager.locale();
        NumberFormat numberFormat = NumberFormat.getNumberInstance(locale);
        DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT, locale);
        return switch (columnIndex) {
            case 0 -> organization.getId();
            case 1 -> organization.getName();
            case 2 -> numberFormat.format(organization.getCoordinates().getX());
            case 3 -> numberFormat.format(organization.getCoordinates().getY());
            case 4 -> dateFormat.format(Date.from(organization.getCreationDate().atZone(ZoneId.systemDefault()).toInstant()));
            case 5 -> numberFormat.format(organization.getAnnualTurnover());
            case 6 -> organization.getType();
            case 7 -> organization.getOfficialAddress().getStreet();
            case 8 -> organization.getOfficialAddress().getZipCode();
            case 9 -> organization.getOwnerUsername();
            default -> "";
        };
    }

    private void rebuild() {
        Comparator<Organization> comparator = comparatorFor(sortColumn);
        List<Organization> filtered = source.stream()
                .filter(this::matchesFilter)
                .collect(Collectors.toCollection(ArrayList::new));
        if (comparator != null) {
            if (!ascending) comparator = comparator.reversed();
            filtered = filtered.stream().sorted(comparator).collect(Collectors.toList());
        }
        visible = List.copyOf(filtered);
        fireTableDataChanged();
    }

    private boolean matchesFilter(Organization organization) {
        if (filter.isBlank()) return true;
        for (int i = 0; i < getColumnCount(); i++) {
            Object value = rawValue(organization, i);
            if (String.valueOf(value).toLowerCase(localeManager.locale()).contains(filter)) return true;
        }
        return false;
    }

    private Comparator<Organization> comparatorFor(int column) {
        return OrganizationTableColumn.isValid(column) ? column(column).comparator() : null;
    }

    private Object rawValue(Organization organization, int column) {
        return OrganizationTableColumn.isValid(column) ? column(column).rawValue(organization) : "";
    }

    private OrganizationTableColumn column(int column) {
        return OrganizationTableColumn.at(column);
    }

    private enum OrganizationTableColumn {
        ID("table.id", Comparator.comparingLong(Organization::getId)) {
            Object rawValue(Organization organization) { return organization.getId(); }
        },
        NAME("table.name", Comparator.comparing(Organization::getName, Comparator.nullsFirst(String::compareTo))) {
            Object rawValue(Organization organization) { return organization.getName(); }
        },
        X("table.x", Comparator.comparing(o -> o.getCoordinates().getX(), Comparator.nullsFirst(Long::compareTo))) {
            Object rawValue(Organization organization) { return organization.getCoordinates().getX(); }
        },
        Y("table.y", Comparator.comparing(o -> o.getCoordinates().getY(), Comparator.nullsFirst(Double::compareTo))) {
            Object rawValue(Organization organization) { return organization.getCoordinates().getY(); }
        },
        CREATED("table.created", Comparator.comparing(Organization::getCreationDate)) {
            Object rawValue(Organization organization) { return organization.getCreationDate(); }
        },
        TURNOVER("table.turnover", Comparator.comparing(Organization::getAnnualTurnover)) {
            Object rawValue(Organization organization) { return organization.getAnnualTurnover(); }
        },
        TYPE("table.type", Comparator.comparing(o -> String.valueOf(o.getType()))) {
            Object rawValue(Organization organization) { return organization.getType(); }
        },
        STREET("table.street", Comparator.comparing(o -> String.valueOf(o.getOfficialAddress().getStreet()))) {
            Object rawValue(Organization organization) { return organization.getOfficialAddress().getStreet(); }
        },
        ZIP("table.zip", Comparator.comparing(o -> String.valueOf(o.getOfficialAddress().getZipCode()))) {
            Object rawValue(Organization organization) { return organization.getOfficialAddress().getZipCode(); }
        },
        OWNER("table.owner", Comparator.comparing(o -> String.valueOf(o.getOwnerUsername()))) {
            Object rawValue(Organization organization) { return organization.getOwnerUsername(); }
        };

        private static final OrganizationTableColumn[] COLUMNS = values();

        private final String textKey;
        private final Comparator<Organization> comparator;

        OrganizationTableColumn(String textKey, Comparator<Organization> comparator) {
            this.textKey = textKey;
            this.comparator = comparator;
        }

        static int count() {
            return COLUMNS.length;
        }

        static boolean isValid(int index) {
            return index >= 0 && index < COLUMNS.length;
        }

        static OrganizationTableColumn at(int index) {
            return COLUMNS[index];
        }

        String localizedName(LocaleManager localeManager) {
            return localeManager.text(textKey);
        }

        Comparator<Organization> comparator() {
            return comparator;
        }

        abstract Object rawValue(Organization organization);
    }
}
