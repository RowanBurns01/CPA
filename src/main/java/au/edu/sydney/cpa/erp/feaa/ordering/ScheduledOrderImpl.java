package au.edu.sydney.cpa.erp.feaa.ordering;

import au.edu.sydney.cpa.erp.ordering.Order;
import au.edu.sydney.cpa.erp.ordering.Report;
import au.edu.sydney.cpa.erp.ordering.ScheduledOrder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class ScheduledOrderImpl extends SingleOrderImpl implements ScheduledOrder {
    private int numQuarters;

    /**
     * A repeated order implementation, extends the single order implementation. Implementation is type and importance dependent.
     * @param importance The order's importance implementation. May not be null.
     * @param type The order's type implementation. May not be null.
     * @param id The id of the order being created. May not be negative.
     * @param customerID The id of the client. May not be null.
     * @param date The local date of the order being created. May not be null.
     * @param criticalLoading The order's critical loading. May be negative.
     * @param numQuarters The number of quarters. May be negative.
     * @param maxCountedEmployees The order's max counted employees. May be negative.
     */
    public ScheduledOrderImpl(Importance importance, Type type, int id, int customerID, LocalDateTime date, double criticalLoading, int numQuarters, int maxCountedEmployees) {
        super(importance, type, id, customerID, date, criticalLoading, maxCountedEmployees);
        this.numQuarters = numQuarters;
    }

    @Override
    public double getRecurringCost() {
        return super.getTotalCommission();
    }

    @Override
    public int getNumberOfQuarters() {
        return numQuarters;
    }

    @Override
    public double getTotalCommission() {
        return super.getTotalCommission() * numQuarters;
    }

    @Override
    public String generateInvoiceData() {
        return super.getImportance().scheduledOrderGenerateInvoiceData(getTotalCommission(), super.getReports(), super.getType(), getRecurringCost(), getNumberOfQuarters());
    }
    /**
     * Copies a scheduled order, sets the same reports and returns.
     * @return  Returns a new copy of a scheduled order.
     */
    @Override
    public Order copy() {
        Map<Report, Integer> products = super.getReports();

        Order copy = new ScheduledOrderImpl(getImportance(),getType(),getOrderID(), getClient(), getOrderDate(), getCriticalLoading(), getNumberOfQuarters(), getType().getMaxCountedEmployees());
        for (Report report : products.keySet()) {
            copy.setReport(report, products.get(report));
        }

        return copy;
    }

    @Override
    public String shortDesc() {
        return String.format("ID:%s $%,.2f per quarter, $%,.2f total", super.getOrderID(), getRecurringCost(), getTotalCommission());
    }

    @Override
    public String longDesc() {
        return getImportance().scheduledOrderGetLongDesc(getTotalCommission(),getReports(),getType(),getRecurringCost(),numQuarters,super.isFinalised(),super.getOrderID(),super.getOrderDate());
    }
}
