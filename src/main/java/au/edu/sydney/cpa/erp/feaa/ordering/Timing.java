package au.edu.sydney.cpa.erp.feaa.ordering;

import au.edu.sydney.cpa.erp.ordering.Order;
import au.edu.sydney.cpa.erp.ordering.Report;

import java.time.LocalDateTime;
import java.util.Map;

public interface Timing {

    String generateInvoiceData(double criticalLoading, Map<Report, Integer> reports, int maxCountedEmployees);

    String shortDesc(int id, double criticalLoading, Map<Report, Integer> reports, int maxCountedEmployees);

    String longDesc(int id, boolean finalised, LocalDateTime date, double criticalLoading, Map<Report, Integer> reports, int maxCountedEmployees);

    double getTotalCommission( double criticalLoading, Map<Report, Integer> reports, int maxCountedEmployees);

    double getRecurringCost(double criticalLoading, Map<Report, Integer> reports, int maxCountedEmployees);

    int getNumberOfQuarters();

    void setNumQuarters(int q);

}
