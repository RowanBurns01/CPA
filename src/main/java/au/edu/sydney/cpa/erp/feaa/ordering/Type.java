package au.edu.sydney.cpa.erp.feaa.ordering;

import au.edu.sydney.cpa.erp.ordering.Report;

import java.util.Map;

public interface Type {

    /**
     * Simple accessor for the order's maxCountedEmployees.
     * @return The order's maxCountedEmployees, will be -1 if not initialized.
     */
    int getMaxCountedEmployees();

    /**
     * Simple setter for the order's maxCountedEmployees.
     * @param e Sets order's maxCountedEmployees as e.
     */
    void setMaxCountedEmployees(int e);

    /**
     * Modifies the TotalCommission depending on the Type implementation.
     * @param reports A hashmap of the reports in the order.
     * @param report A specific report from the reports collection.
     * @return The calculated total commission.
     */
    double addTotalCommission(Map<Report, Integer> reports, Report report);

    /**
     * Modify the StringBuilder depending on the Type Implementation.
     * @param sb The current StringBuilder for the order. May not be null.
     * @param i An integer value to compare with maxCountedEmployees.
     * @return The altered StringBuilder. May not be null.
     */
    StringBuilder addCapped(StringBuilder sb, Integer i);

    /**
     * Modify the StringBuilder depending on the Type Implementation.
     * @param reportSB The current StringBuilder for the order. May not be null.
     * @param i An integer value to compare with maxCountedEmployees.
     * @return The altered StringBuilder. May not be null.
     */
    StringBuilder addCappedForDesc(StringBuilder reportSB, Integer i);
}
