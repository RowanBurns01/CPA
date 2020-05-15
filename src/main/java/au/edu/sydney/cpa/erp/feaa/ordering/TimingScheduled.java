package au.edu.sydney.cpa.erp.feaa.ordering;

import au.edu.sydney.cpa.erp.ordering.Report;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class TimingScheduled implements Timing {

    private int numQuarters;

    @Override
    public String generateInvoiceData(double critical, Map<Report, Integer> reports, int maxCountedEmployees) {
        if(critical == -1 ){
            StringBuilder sb = new StringBuilder();

            sb.append("Thank you for your Crimson Permanent Assurance accounting order!\n");
            sb.append("The cost to provide these services: $");
            sb.append(String.format("%,.2f", getRecurringCost(critical, reports, maxCountedEmployees)));
            sb.append(" each quarter, with a total overall cost of: $");
            sb.append(String.format("%,.2f", getTotalCommission(critical, reports, maxCountedEmployees)));
            sb.append("\nPlease see below for details:\n");

            List<Report> keyList = new ArrayList<>(reports.keySet());
            keyList.sort(Comparator.comparing(Report::getReportName).thenComparing(Report::getCommission));

            for (Report report : keyList) {
                sb.append("\tReport name: ");
                sb.append(report.getReportName());
                sb.append("\tEmployee Count: ");
                sb.append(reports.get(report));
                sb.append("\tCost per employee: ");
                sb.append(String.format("$%,.2f", report.getCommission()));
                sb.append("\tSubtotal: ");
                sb.append(String.format("$%,.2f\n", report.getCommission() * reports.get(report)));
            }
            return sb.toString();
        } else {
            return String.format("Your priority business account will be charged: $%,.2f each quarter for %d quarters, with a total overall cost of: $%,.2f" +
                    "\nPlease see your internal accounting department for itemised details.", getRecurringCost(critical, reports, maxCountedEmployees), getNumberOfQuarters(), getTotalCommission(critical, reports, maxCountedEmployees));
        }
    }

    @Override
    public String shortDesc(int id, double critical, Map<Report, Integer> reports, int maxCountedEmployees) {
        return String.format("ID:%s $%,.2f per quarter, $%,.2f total", id, getRecurringCost(critical, reports, maxCountedEmployees), getTotalCommission(critical, reports, maxCountedEmployees));
    }

    @Override
    public String longDesc(int id, boolean finalised, LocalDateTime date, double critical, Map<Report, Integer> reports, int maxCountedEmployees) {
        String toReturn;
        if(maxCountedEmployees == -1 && critical == -1){
            double totalLoadedCost = this.getTotalCommission(critical, reports, maxCountedEmployees);
            StringBuilder reportSB = new StringBuilder();

            List<Report> keyList = new ArrayList<>(reports.keySet());
            keyList.sort(Comparator.comparing(Report::getReportName).thenComparing(Report::getCommission));

            for (Report report : keyList) {
                double subtotal = report.getCommission() * reports.get(report);

                reportSB.append(String.format("\tReport name: %s\tEmployee Count: %d\tCommission per employee: $%,.2f\tSubtotal: $%,.2f\n",
                        report.getReportName(),
                        reports.get(report),
                        report.getCommission(),
                        subtotal));
            }

            toReturn = String.format(finalised ? "" : "*NOT FINALISED*\n" +
                            "Order details (id #%d)\n" +
                            "Date: %s\n" +
                            "Number of quarters: %d\n" +
                            "Reports:\n" +
                            "%s" +
                            "Recurring cost: $%,.2f\n" +
                            "Total cost: $%,.2f\n",
                    id,
                    date.format(DateTimeFormatter.ISO_LOCAL_DATE),
                    numQuarters,
                    reportSB.toString(),
                    getRecurringCost(critical, reports, maxCountedEmployees),
                    totalLoadedCost
            );
        } else if (maxCountedEmployees != -1 && critical == -1){
            StringBuilder reportSB = new StringBuilder();

            List<Report> keyList = new ArrayList<>(reports.keySet());
            keyList.sort(Comparator.comparing(Report::getReportName).thenComparing(Report::getCommission));

            for (Report report : keyList) {
                double subtotal = report.getCommission() * Math.min(maxCountedEmployees,reports.get(report));

                reportSB.append(String.format("\tReport name: %s\tEmployee Count: %d\tCommission per employee: $%,.2f\tSubtotal: $%,.2f",
                        report.getReportName(),
                        reports.get(report),
                        report.getCommission(),
                        subtotal));

                if (reports.get(report) > maxCountedEmployees) {
                    reportSB.append(" *CAPPED*\n");
                } else {
                    reportSB.append("\n");
                }
            }

            toReturn = String.format(finalised ? "" : "*NOT FINALISED*\n" +
                            "Order details (id #%d)\n" +
                            "Date: %s\n" +
                            "Number of quarters: %d\n" +
                            "Reports:\n" +
                            "%s" +
                            "Recurring cost: $%,.2f\n" +
                            "Total cost: $%,.2f\n",
                    id,
                    date.format(DateTimeFormatter.ISO_LOCAL_DATE),
                    numQuarters,
                    reportSB.toString(),
                    getRecurringCost(critical, reports, maxCountedEmployees),
                    this.getTotalCommission(critical, reports, maxCountedEmployees)

            );
        } else if (maxCountedEmployees == -1 && critical != -1){
            double totalBaseCost = 0.0;
            double loadedCostPerQuarter = getRecurringCost(critical, reports, maxCountedEmployees);
            double totalLoadedCost = this.getTotalCommission(critical,reports,maxCountedEmployees);
            StringBuilder reportSB = new StringBuilder();

            List<Report> keyList = new ArrayList<>(reports.keySet());
            keyList.sort(Comparator.comparing(Report::getReportName).thenComparing(Report::getCommission));

            for (Report report : keyList) {
                double subtotal = report.getCommission() * reports.get(report);
                totalBaseCost += subtotal;

                reportSB.append(String.format("\tReport name: %s\tEmployee Count: %d\tCommission per employee: $%,.2f\tSubtotal: $%,.2f\n",
                        report.getReportName(),
                        reports.get(report),
                        report.getCommission(),
                        subtotal));
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
                    numQuarters,
                    reportSB.toString(),
                    totalLoadedCost - (totalBaseCost * numQuarters),
                    loadedCostPerQuarter,
                    totalLoadedCost
            );
        } else {
            double totalBaseCost = 0.0;
            double loadedCostPerQuarter = getRecurringCost(critical, reports, maxCountedEmployees);
            double totalLoadedCost = this.getTotalCommission(critical,reports,maxCountedEmployees);
            StringBuilder reportSB = new StringBuilder();

            List<Report> keyList = new ArrayList<>(reports.keySet());
            keyList.sort(Comparator.comparing(Report::getReportName).thenComparing(Report::getCommission));

            for (Report report : keyList) {
                double subtotal = report.getCommission() * Math.min(maxCountedEmployees, reports.get(report));
                totalBaseCost += subtotal;

                reportSB.append(String.format("\tReport name: %s\tEmployee Count: %d\tCommission per employee: $%,.2f\tSubtotal: $%,.2f",
                        report.getReportName(),
                        reports.get(report),
                        report.getCommission(),
                        subtotal));

                if (reports.get(report) >maxCountedEmployees) {
                    reportSB.append(" *CAPPED*\n");
                } else {
                    reportSB.append("\n");
                }
            }

            toReturn = String.format(finalised ? "" : "*NOT FINALISED*\n" +
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
                    numQuarters,
                    reportSB.toString(),
                    totalLoadedCost - (totalBaseCost * numQuarters),
                    loadedCostPerQuarter,
                    totalLoadedCost

            );
        }
        return toReturn;
    }

    @Override
    public double getTotalCommission(double critical, Map<Report, Integer> reports, int maxCountedEmployees) {
        return getRecurringCost(critical, reports, maxCountedEmployees) * getNumberOfQuarters();
    }

    @Override
    public double getRecurringCost(double critical, Map<Report, Integer> reports, int maxCountedEmployees) {
        double cost = 0.0;
        for (Report report : reports.keySet()) {
            if (maxCountedEmployees != -1) {
                cost += report.getCommission() * Math.min(maxCountedEmployees, reports.get(report));
            } else {
                cost += reports.get(report) * report.getCommission();
            }
        }
        if(critical != -1) {
            cost += cost * critical;
        }
        return cost;
    }

    @Override
    public int getNumberOfQuarters() {
        return numQuarters;
    }

    @Override
    public void setNumQuarters(int q){
        this.numQuarters = q;
    }
}
