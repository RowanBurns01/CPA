package au.edu.sydney.cpa.erp.feaa.ordering;

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
}
