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
import java.util.function.Function;
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
    public int getColumnCount() { return 10; }

    public String getColumnName(int column) {
        return switch (column) {
            case 0 -> localeManager.text("table.id");
            case 1 -> localeManager.text("table.name");
            case 2 -> localeManager.text("table.x");
            case 3 -> localeManager.text("table.y");
            case 4 -> localeManager.text("table.created");
            case 5 -> localeManager.text("table.turnover");
            case 6 -> localeManager.text("table.type");
            case 7 -> localeManager.text("table.street");
            case 8 -> localeManager.text("table.zip");
            case 9 -> localeManager.text("table.owner");
            default -> "";
        };
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
        return switch (column) {
            case 0 -> Comparator.comparingLong(Organization::getId);
            case 1 -> Comparator.comparing(Organization::getName, Comparator.nullsFirst(String::compareTo));
            case 2 -> Comparator.comparing(o -> o.getCoordinates().getX(), Comparator.nullsFirst(Long::compareTo));
            case 3 -> Comparator.comparing(o -> o.getCoordinates().getY(), Comparator.nullsFirst(Double::compareTo));
            case 4 -> Comparator.comparing(Organization::getCreationDate);
            case 5 -> Comparator.comparing(Organization::getAnnualTurnover);
            case 6 -> Comparator.comparing(o -> String.valueOf(o.getType()));
            case 7 -> Comparator.comparing(stringValue(o -> o.getOfficialAddress().getStreet()));
            case 8 -> Comparator.comparing(stringValue(o -> o.getOfficialAddress().getZipCode()));
            case 9 -> Comparator.comparing(stringValue(Organization::getOwnerUsername));
            default -> null;
        };
    }

    private Function<Organization, String> stringValue(Function<Organization, String> getter) {
        return organization -> String.valueOf(getter.apply(organization));
    }

    private Object rawValue(Organization organization, int column) {
        return switch (column) {
            case 0 -> organization.getId();
            case 1 -> organization.getName();
            case 2 -> organization.getCoordinates().getX();
            case 3 -> organization.getCoordinates().getY();
            case 4 -> organization.getCreationDate();
            case 5 -> organization.getAnnualTurnover();
            case 6 -> organization.getType();
            case 7 -> organization.getOfficialAddress().getStreet();
            case 8 -> organization.getOfficialAddress().getZipCode();
            case 9 -> organization.getOwnerUsername();
            default -> "";
        };
    }
}
