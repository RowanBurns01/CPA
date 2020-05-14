package au.edu.sydney.cpa.erp.feaa.ordering;

import au.edu.sydney.cpa.erp.ordering.Order;
import au.edu.sydney.cpa.erp.ordering.Report;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ConcreteOrder implements Order {

    private final Timing schedule;
    private final Importance importance;
    private final Type type;
    private Map<Report, Integer> reports = new HashMap<>();
    private final int id;
    private LocalDateTime date;
    private int client;
    private boolean finalised = false;

    public ConcreteOrder(Timing schedule, Importance importance, Type type, int id, int client, LocalDateTime date, double criticalLoading, int numQuarters, int maxCountedEmployees){
        this.schedule = schedule;
        this.importance = importance;
        this.type = type;

        setNumberOfQuarters(numQuarters);
        setCriticalLoading(criticalLoading);
        setMaxCountedEmployees(maxCountedEmployees);

        this.id = id;
        this.client = client;
        this.date = date;
    }


    @Override
    public int getOrderID() {
        return id;
    }

    @Override
    public LocalDateTime getOrderDate() {
        return date;
    }

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

    @Override
    public Set<Report> getAllReports() {
        return reports.keySet();
    }

    @Override
    public int getReportEmployeeCount(Report report) {
        // We can't rely on equal reports having the same object identity since they get
        // rebuilt over the network, so we have to check for presence and same values

        for (Report contained: reports.keySet()) {
            if(contained.equals(report)){
                report = contained;
                break;
            }
        }
        Integer result = reports.get(report);
        return null == result ? 0 : result;
    }

    @Override
    public int getClient() {
        return client;
    }

    @Override
    public void finalise() { this.finalised = true; }

    @Override
    public Order copy() {

        Order copy = new ConcreteOrder(schedule,  importance,  type,id, client, date, getCriticalLoading(), getNumberOfQuarters(), getMaxCountedEmployees());
        for (Report report : reports.keySet()) {
            copy.setReport(report, reports.get(report));
        }

        return copy;
    }

    protected boolean isFinalised() {
        return finalised;
    }

    protected Map<Report, Integer> getReports() {
        return reports;
    }

    // Schedule Specific
    @Override
    public String generateInvoiceData() {
        return schedule.generateInvoiceData(getCriticalLoading(),reports, getMaxCountedEmployees());
    }

    @Override
    public String shortDesc() { return schedule.shortDesc(id,getCriticalLoading(),reports,getMaxCountedEmployees()); }

    @Override
    public String longDesc() { return schedule.longDesc(id,finalised, date, getCriticalLoading(), reports,getMaxCountedEmployees()); }

    @Override
    public double getTotalCommission() {
        return schedule.getTotalCommission(getCriticalLoading(),reports, getMaxCountedEmployees());
    }

    public double getRecurringCost() { return schedule.getRecurringCost(getCriticalLoading(),reports, getMaxCountedEmployees()); }

    public int getNumberOfQuarters() { return schedule.getNumberOfQuarters(); }

    public void setNumberOfQuarters(int numberOfQuarters) { schedule.setNumQuarters(numberOfQuarters); }

    // Importance Specific
    protected double getCriticalLoading() { return importance.getCriticalLoading(); }

    public void setCriticalLoading(double criticalLoading) { importance.setCriticalLoading(criticalLoading); }

    // Type Specific
    protected int getMaxCountedEmployees() {
        return type.getMaxCountedEmployees();
    }

    public void setMaxCountedEmployees(int maxCountedEmployees) { type.setMaxCounterEmployees(maxCountedEmployees);}
}
