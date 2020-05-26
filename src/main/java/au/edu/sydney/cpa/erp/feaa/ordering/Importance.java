package au.edu.sydney.cpa.erp.feaa.ordering;

import au.edu.sydney.cpa.erp.ordering.Report;

import java.time.LocalDateTime;
import java.util.Map;

public interface Importance {

    /**
     * Simple accessor for the order's critical loading.
     * @return The order's critical loading, will be -1 if not initialized.
     */
    double getCriticalLoading();

    /**
     * Simple setter for the order's critical loading.
     * @param d Sets order's critical loading as d.
     */
    void setCriticalLoading(double d);

    /**
     * Adds critical loading to the calculation if it is required.
     * @param c The order's current cost.
     * @return The additional cost after applying critical loading. May be zero.
     */
    double addCriticalLoading(double c);

    /**
     * Generates invoice data for a single order. The implementation of type also modifies the invoice generated.
     * @param totalCommission The total commission for the order. May not be zero.
     * @param reports The reports prepared in the order. May include no reports.
     * @param type The order's type implementation.
     * @return The invoice data as a String.
     */
    String singleOrderGenerateInvoiceData(double totalCommission, Map<Report, Integer> reports, Type type);

    /**
     * Generates invoice data for a scheduled order. The implementation of type also modifies the invoice generated.
     * @param totalCommission The total commission for the order, same as the numOfQuarters * recurringCost. May not be zero.
     * @param reports The reports prepared in the order. May include no reports.
     * @param type The order's type implementation.
     * @param recurringCost The quarterly cost for the repeated order.
     * @param numOfQuarters The number of quarters, may not be zero.
     * @return The invoice data as a String.
     */
    String scheduledOrderGenerateInvoiceData(double totalCommission, Map<Report, Integer> reports, Type type, double recurringCost, int numOfQuarters);

    /**
     * Generates long description for a single order. The implementation of type also modifies the description generated.
     * @param totalCommission The total commission for the order. May not be zero.
     * @param reports The reports prepared in the order. May include no reports.
     * @param type The order's type implementation.
     * @param finalised Boolean whether the order has been finalised or not.
     * @param id The id of the order being created. May not be negative.
     * @param date The local date of the order being created. May not be null.
     * @return The long description as a String.
     */
    String singleOrderGetLongDesc(double totalCommission, Map<Report, Integer> reports, Type type, boolean finalised, int id, LocalDateTime date);

    /**
     * Generates long description for a scheduled order. The implementation of type also modifies the description generated.
     * @param totalCommission The total commission for the order. May not be zero.
     * @param reports The reports prepared in the order. May include no reports.
     * @param type The order's type implementation.
     * @param recurringCost The quarterly cost for the repeated order.
     * @param numOfQuarters The number of quarters, may not be zero.
     * @param finalised Boolean whether the order has been finalised or not.
     * @param id The id of the order being created. May not be negative.
     * @param date The local date of the order being created. May not be null.
     * @return The long description as a String.
     */
    String scheduledOrderGetLongDesc(double totalCommission, Map<Report, Integer> reports, Type type, double recurringCost, int numOfQuarters, boolean finalised, int id, LocalDateTime date);

}
