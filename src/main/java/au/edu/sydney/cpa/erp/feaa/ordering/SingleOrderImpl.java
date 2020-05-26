package au.edu.sydney.cpa.erp.feaa.ordering;

import au.edu.sydney.cpa.erp.ordering.Order;
import au.edu.sydney.cpa.erp.ordering.Report;

import java.time.LocalDateTime;
import java.util.*;

public class SingleOrderImpl implements Order {
    private Map<Report, Integer> reports = new HashMap<>();
    private final int id;
    private LocalDateTime date;
    private int client;
    private boolean finalised = false;

    private final Importance importance;
    private final Type type;

    /**
     * The single order implementation. Implementation is type and importance dependent.
     * @param importance The order's importance implementation. May not be null.
     * @param type The order's type implementation. May not be null.
     * @param id The id of the order being created. May not be negative.
     * @param client The id of the client. May not be null.
     * @param date The local date of the order being created. May not be null.
     * @param criticalLoading The order's critical loading. May be negative.
     * @param maxCountedEmployees The order's max counted employees. May be negative.
     */
    public SingleOrderImpl(Importance importance, Type type, int id, int client, LocalDateTime date, double criticalLoading, int maxCountedEmployees ) {
        this.id = id;
        this.client = client;
        this.date = date;

        this.importance = importance;
        this.type = type;
        setCriticalLoading(criticalLoading);
        setMaxCountedEmployees(maxCountedEmployees);
    }

    /**
     * Simple accessor that returns the local date.
     * @return The local date. May not be null.
     */
    @Override
    public LocalDateTime getOrderDate() {
        return date;
    }

    /**
     * Sets the number of employees working on a specific report.
     * @param report The chosen report. May not be null.
     * @param employeeCount The number of employees. May not be zero or negative.
     */
    @Override
    public void setReport(Report report, int employeeCount) {
        if (finalised) throw new IllegalStateException("Order was already finalised.");

        // We can't rely on equal reports having the same object identity since they get
        // rebuilt over the network, so we have to check for presence and same values

        for (Report contained: reports.keySet()) {
            if (contained.equals(report)) {
                report = contained;
                break;
            }
        }

        reports.put(report, employeeCount);
    }

    /**
     * Simple accessor that returns the the set reports.
     * @return The set reports as a set. May not be null.
     */
    @Override
    public Set<Report> getAllReports() {
        return reports.keySet();
    }

    /**
     * Simple accessor, retrieves the number of employees working on a specific report.
     * @param report The chosen report. May not be null.
     */
    @Override
    public int getReportEmployeeCount(Report report) {
        // We can't rely on equal reports having the same object identity since they get
        // rebuilt over the network, so we have to check for presence and same values

        for (Report contained: reports.keySet()) {
            if (contained.equals(report)) {
                report = contained;
                break;
            }
        }
        Integer result = reports.get(report);
        return null == result ? 0 : result;
    }

    /**
     * Simple accessor that returns the client.
     * @return The client. May not be null.
     */
    @Override
    public int getClient() {
        return client;
    }

    /**
     * Sets the finalise boolean to true. The order is now finalised.
     */
    @Override
    public void finalise() {
        this.finalised = true;
    }

    /**
     * Copies an original order, resets the same reports and returns.
     * @return  Returns a new copy of the original order.
     */
    @Override
    public Order copy() {
        Order copy = new SingleOrderImpl(importance, type, id, client, date,  getCriticalLoading(), getMaxCountedEmployees());
        for (Report report : reports.keySet()) {
            copy.setReport(report, reports.get(report));
        }

        return copy;
    }

    /**
     * Simple accessor that returns the short description.
     * @return The short description. May not be null.
     */
    @Override
    public String shortDesc() {
        return String.format("ID:%s $%,.2f", id, getTotalCommission());
    }

    /**
     * Simple accessor that returns the long description depending on importance and type implementation.
     * @return The long description. May not be null.
     */
    @Override
    public String longDesc() {
        return importance.singleOrderGetLongDesc(getTotalCommission(),reports,type,finalised,id,date);
    }

    /**
     * Simple accessor that returns the invoice data depending on importance and type implementation.
     * @return The invoice data. May not be null.
     */
    @Override
    public String generateInvoiceData() {
        return importance.singleOrderGenerateInvoiceData(getTotalCommission(), reports, type);
    }

    /**
     * Simple accessor that returns the total commission depending on importance and type implementation.
     * @return The total commission. May not be negative.
     */
    @Override
    public double getTotalCommission() {
        double cost = 0.0;
        for (Report report : reports.keySet()) {
            cost += type.addTotalCommission(reports, report);
        }
        cost += importance.addCriticalLoading(cost);
        return cost;
    }

    protected Map<Report, Integer> getReports() {
        return reports;
    }

    @Override
    public int getOrderID() {
        return id;
    }

    protected boolean isFinalised() {
        return finalised;
    }

    protected double getCriticalLoading() { return importance.getCriticalLoading(); }

    public void setCriticalLoading(double criticalLoading) { importance.setCriticalLoading(criticalLoading); }

    public Importance getImportance() {
        return importance;
    }

    protected int getMaxCountedEmployees() { return type.getMaxCountedEmployees(); }

    public void setMaxCountedEmployees(int maxCountedEmployees) { type.setMaxCountedEmployees(maxCountedEmployees);}

    public Type getType() {
        return type;
    }
}
