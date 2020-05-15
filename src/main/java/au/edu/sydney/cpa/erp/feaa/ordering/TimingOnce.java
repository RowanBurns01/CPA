package au.edu.sydney.cpa.erp.feaa.ordering;

import au.edu.sydney.cpa.erp.ordering.Report;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class TimingOnce implements Timing {

    @Override
    public String generateInvoiceData(double criticalLoading, Map<Report, Integer> reports, int maxCountedEmployees) {
        if (criticalLoading == -1) {
            double baseCommission = 0.0;
            double loadedCommission = getTotalCommission(criticalLoading, reports, maxCountedEmployees);

            StringBuilder sb = new StringBuilder();

            sb.append("Thank you for your Crimson Permanent Assurance accounting order!\n");
            sb.append("The cost to provide these services: $");
            sb.append(String.format("%,.2f", getTotalCommission(criticalLoading, reports, maxCountedEmployees)));
            sb.append("\nPlease see below for details:\n");
            List<Report> keyList = new ArrayList<>(reports.keySet());
            keyList.sort(Comparator.comparing(Report::getReportName).thenComparing(Report::getCommission));

            for (Report report : keyList) {
                double subtotal = report.getCommission() * reports.get(report);
                baseCommission += subtotal;

                sb.append("\tReport name: ");
                sb.append(report.getReportName());
                sb.append("\tEmployee Count: ");
                sb.append(reports.get(report));
                sb.append("\tCost per employee: ");
                sb.append(String.format("$%,.2f", report.getCommission()));
                sb.append("\tSubtotal: ");
                sb.append(String.format("$%,.2f\n", subtotal));
            }
            return sb.toString();
        } else {
            return String.format("Your priority business account has been charged: $%,.2f" +
                    "\nPlease see your internal accounting department for itemised details.", getTotalCommission(criticalLoading, reports, maxCountedEmployees));
        }
    }

    @Override
    public String shortDesc(int id, double criticalLoading, Map<Report, Integer> reports, int maxCountedEmployees) {
        return String.format("ID:%s $%,.2f", id, getTotalCommission(criticalLoading, reports, maxCountedEmployees));
    }

    @Override
    public String longDesc(int id, boolean finalised, LocalDateTime date, double criticalLoading, Map<Report, Integer> reports, int maxCountedEmployees) {
        String toReturn;
        if(maxCountedEmployees == -1 && criticalLoading == -1){
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
                            "Reports:\n" +
                            "%s" +
                            "Total cost: $%,.2f\n",
                    id,
                    date.format(DateTimeFormatter.ISO_LOCAL_DATE),
                    reportSB.toString(),
                    getTotalCommission(criticalLoading, reports, maxCountedEmployees)
            );
        } else if (maxCountedEmployees != -1 && criticalLoading == -1){
            StringBuilder reportSB = new StringBuilder();

            List<Report> keyList = new ArrayList<>(reports.keySet());
            keyList.sort(Comparator.comparing(Report::getReportName).thenComparing(Report::getCommission));

            for (Report report : keyList) {
                double subtotal = report.getCommission() * Math.min(maxCountedEmployees, reports.get(report));

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
                            "Reports:\n" +
                            "%s" +
                            "Total cost: $%,.2f\n",
                    id,
                    date.format(DateTimeFormatter.ISO_LOCAL_DATE),
                    reportSB.toString(),
                    getTotalCommission(criticalLoading, reports, maxCountedEmployees)
            );
        } else if (maxCountedEmployees == -1 && criticalLoading != -1){
            double baseCommission = 0.0;
            double loadedCommission = getTotalCommission(criticalLoading, reports, maxCountedEmployees);
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

            toReturn = String.format(finalised ? "" : "*NOT FINALISED*\n" +
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
        } else {
            double baseCommission = 0.0;
            double loadedCommission = getTotalCommission(criticalLoading, reports, maxCountedEmployees);
            StringBuilder reportSB = new StringBuilder();

            List<Report> keyList = new ArrayList<>(reports.keySet());
            keyList.sort(Comparator.comparing(Report::getReportName).thenComparing(Report::getCommission));

            for (Report report : keyList) {
                double subtotal = report.getCommission() * Math.min(maxCountedEmployees, reports.get(report));
                baseCommission += subtotal;

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
        return toReturn;
    }

    @Override
    public double getTotalCommission(double criticalLoading, Map<Report, Integer> reports, int maxCountedEmployees) {
        double cost = 0.0;
        for (Report report : reports.keySet()) {
            if (maxCountedEmployees != -1) {
                cost += report.getCommission() * Math.min(maxCountedEmployees, reports.get(report));
            } else {
                cost += reports.get(report) * report.getCommission();
            }
        }
        if(criticalLoading != -1) {
            cost += cost * criticalLoading;
        }
        return cost;
    }


    // DONT USE BAD INTERFACE SEGREGATION, could change
    @Override
    public double getRecurringCost(double critical, Map<Report, Integer> reports, int max) {
        return 0;
    }

    @Override
    public int getNumberOfQuarters() {
        return 0;
    }

    @Override
    public void setNumQuarters(int q) {

    }

}
