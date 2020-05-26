package au.edu.sydney.cpa.erp.feaa.ordering;

import au.edu.sydney.cpa.erp.ordering.Report;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * Critical order has critical loading applied to some methods.
 */
public class ImportanceCritical implements Importance {

    private double criticalLoading;

    @Override
    public double getCriticalLoading() {
        return criticalLoading;
    }

    @Override
    public void setCriticalLoading(double d) {
        this.criticalLoading = d;
    }

    @Override
    public double addCriticalLoading(double c) {
        return c *  getCriticalLoading();
    }

    @Override
    public String singleOrderGenerateInvoiceData(double totalCommission, Map<Report, Integer> reports, Type type) {
        return String.format("Your priority business account has been charged: $%,.2f" +
                "\nPlease see your internal accounting department for itemised details.", totalCommission);
    }

    @Override
    public String scheduledOrderGenerateInvoiceData(double totalCommission, Map<Report, Integer> reports, Type type, double recurringCost, int numOfQuarters) {
        return String.format("Your priority business account will be charged: $%,.2f each quarter for %d quarters, with a total overall cost of: $%,.2f" +
                "\nPlease see your internal accounting department for itemised details.", recurringCost, numOfQuarters, totalCommission);
    }

    @Override
    public String singleOrderGetLongDesc(double totalCommission, Map<Report, Integer> reports, Type type, boolean finalised, int id, LocalDateTime date) {

        double baseCommission = 0.0;
        double loadedCommission = totalCommission;
        StringBuilder reportSB = new StringBuilder();

        List<Report> keyList = new ArrayList<>(reports.keySet());
        keyList.sort(Comparator.comparing(Report::getReportName).thenComparing(Report::getCommission));

        for (Report report : keyList) {
            double subtotal =  type.addTotalCommission(reports,report);
            baseCommission += subtotal;

            reportSB.append(String.format("\tReport name: %s\tEmployee Count: %d\tCommission per employee: $%,.2f\tSubtotal: $%,.2f\n",
                    report.getReportName(),
                    reports.get(report),
                    report.getCommission(),
                    subtotal));

            reportSB = type.addCappedForDesc(reportSB, reports.get(report));

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
    public String scheduledOrderGetLongDesc(double totalCommission, Map<Report, Integer> reports, Type type, double recurringCost, int numOfQuarters, boolean finalised, int id, LocalDateTime date) {

        double totalBaseCost = 0.0;
        double loadedCostPerQuarter = recurringCost;
        double totalLoadedCost = totalCommission;
        StringBuilder reportSB = new StringBuilder();

        List<Report> keyList = new ArrayList<>(reports.keySet());
        keyList.sort(Comparator.comparing(Report::getReportName).thenComparing(Report::getCommission));

        for (Report report : keyList) {
            double subtotal =  type.addTotalCommission(reports,report);
            totalBaseCost += subtotal;

            reportSB.append(String.format("\tReport name: %s\tEmployee Count: %d\tCommission per employee: $%,.2f\tSubtotal: $%,.2f\n",
                    report.getReportName(),
                    reports.get(report),
                    report.getCommission(),
                    subtotal));

            reportSB = type.addCappedForDesc(reportSB, reports.get(report));
        }

        return String.format(finalised ? "" : "*NOT FINALISED*\n" +
                        "Order details (id #%d)\n" +
                        "Date: %s\n" +
                        "Number of quarters: %d\n" +
                        "Reports:\n" +
                        "%s" +
                        "Critical Loading: $%,.2f\n" +
                        "Recurring cost: $%,.2f\n" +
                        "Total cost: $%,.2f\n",
                id,
                date.format(DateTimeFormatter.ISO_LOCAL_DATE),
                numOfQuarters,
                reportSB.toString(),
                totalLoadedCost - (totalBaseCost * numOfQuarters),
                loadedCostPerQuarter,
                totalLoadedCost
        );
    }




}
