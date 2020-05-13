package au.edu.sydney.cpa.erp.feaa.ordering;

import au.edu.sydney.cpa.erp.ordering.Order;
import au.edu.sydney.cpa.erp.ordering.Report;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Note from Tim: this is a critical order for audit accounting work.
 * Critical work costs extra.
 * Audits go into detail and so charge for all employees
 */
@SuppressWarnings("Duplicates")
public class CriticalAuditOrder implements Order {
    private Map<Report, Integer> reports = new HashMap<>();
    private final int id;
    private LocalDateTime date;
    private int client;
    private double criticalLoading;
    private boolean finalised = false;

    public CriticalAuditOrder(int id, int client, LocalDateTime date, double criticalLoading) {
        this.id = id;
        this.client = client;
        this.date = date;
        this.criticalLoading = criticalLoading;
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
            if (contained.getCommission() == report.getCommission() &&
                contained.getReportName().equals(report.getReportName()) &&
                    Arrays.equals(contained.getLegalData(), report.getLegalData()) &&
                    Arrays.equals(contained.getCashFlowData(), report.getCashFlowData()) &&
                    Arrays.equals(contained.getMergesData(), report.getMergesData()) &&
                    Arrays.equals(contained.getTallyingData(), report.getTallyingData()) &&
                    Arrays.equals(contained.getDeductionsData(), report.getDeductionsData())) {
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
            if (contained.getCommission() == report.getCommission() &&
                    contained.getReportName().equals(report.getReportName()) &&
                    Arrays.equals(contained.getLegalData(), report.getLegalData()) &&
                    Arrays.equals(contained.getCashFlowData(), report.getCashFlowData()) &&
                    Arrays.equals(contained.getMergesData(), report.getMergesData()) &&
                    Arrays.equals(contained.getTallyingData(), report.getTallyingData()) &&
                    Arrays.equals(contained.getDeductionsData(), report.getDeductionsData())) {
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
    public void finalise() {
        this.finalised = true;
    }

    protected double getCriticalLoading() {
        return this.criticalLoading;
    }

    @Override
    public Order copy() {
        Order copy = new CriticalAuditOrder(id, client, date, criticalLoading);
        for (Report report : reports.keySet()) {
            copy.setReport(report, reports.get(report));
        }

        return copy;
    }

    @Override
    public String shortDesc() {
        return String.format("ID:%s $%,.2f", id, getTotalCommission());
    }

    @Override
    public String longDesc() {
        double baseCommission = 0.0;
        double loadedCommission = getTotalCommission();
        StringBuilder reportSB = new StringBuilder();

        List<Report> keyList = new ArrayList<>(reports.keySet());
        keyList.sort(Comparator.comparing(Report::getReportName).thenComparing(Report::getCommission));

        for (Report report : keyList) {
            double subtotal = report.getCommission() * reports.get(report);
            baseCommission += subtotal;

            reportSB.append(String.format("\tReport name: %s\tEmployee Count: %d\tCommission per employee: $%,.2f\tSubtotal: $%,.2f\n",
                    report.getReportName(),
                    reports.get(report),
                    report.getCommission(),
                    subtotal));
        }

        return String.format(finalised ? "" : "*NOT FINALISED*\n" +
                        "Order details (id #%d)\n" +
                        "Date: %s\n" +
                        "Reports:\n" +
                        "%s" +
                        "Critical Loading: $%,.2f\n" +
                        "Total cost: $%,.2f\n",
                id,
                date.format(DateTimeFormatter.ISO_LOCAL_DATE),
                reportSB.toString(),
                loadedCommission - baseCommission,
                loadedCommission
                );
    }

    @Override
    public String generateInvoiceData() {
        return String.format("Your priority business account has been charged: $%,.2f" +
                "\nPlease see your internal accounting department for itemised details.", getTotalCommission());
    }

    @Override
    public double getTotalCommission() {
        double cost = 0.0;
        for (Report report : reports.keySet()) {
            cost += reports.get(report) * report.getCommission();
        }
        cost += cost * criticalLoading;
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
}
