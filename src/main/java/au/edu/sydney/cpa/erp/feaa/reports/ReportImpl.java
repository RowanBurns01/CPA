package au.edu.sydney.cpa.erp.feaa.reports;

import au.edu.sydney.cpa.erp.ordering.Report;

import java.util.Arrays;
import java.util.List;

/**
 * ReportImpl implemented as a value object. ReportImplFlyweightFactory filters new ReportImpl to make sure reports
 * with the same intrinsic state are shared.
 */
public class ReportImpl implements Report {

    private final String name;
    private final double commissionPerEmployee;
    private final double[] legalData;
    private final double[] cashFlowData;
    private final double[] mergesData;
    private final double[] tallyingData;
    private final double[] deductionsData;
    private final int hashCode;

    public ReportImpl(String name,
                      double commissionPerEmployee,
                      double[] legalData,
                      double[] cashFlowData,
                      double[] mergesData,
                      double[] tallyingData,
                      double[] deductionsData) {

        this.name = ReportImplFlyweightFactory.getString(name); // beneficial because 1700 cases of duplicate names
        this.commissionPerEmployee = commissionPerEmployee;
        this.legalData = ReportImplFlyweightFactory.getDoubleArray(legalData);
        this.cashFlowData = ReportImplFlyweightFactory.getDoubleArray(cashFlowData);
        this.mergesData = ReportImplFlyweightFactory.getDoubleArray(mergesData);
        this.tallyingData = ReportImplFlyweightFactory.getDoubleArray(tallyingData);
        this.deductionsData = ReportImplFlyweightFactory.getDoubleArray(deductionsData);
        this.hashCode = calculateHash();
    }

    /**
     * Simple accessor for the report name. May not be null.
     * @return The report name.
     */
    @Override
    public String getReportName() {
        return name;
    }

    /**
     * Simple accessor for the commission per employee. May not be null.
     * @return The commission.
     */
    @Override
    public double getCommission() {
        return commissionPerEmployee;
    }

    /**
     * Simple accessor for the legal data. May not be null.
     * @return The legal data.
     */
    @Override
    public double[] getLegalData() {
        return legalData;
    }

    /**
     * Simple accessor for the cash flow data. May not be null.
     * @return The cash flow data.
     */
    @Override
    public double[] getCashFlowData() {
        return cashFlowData;
    }

    /**
     * Simple accessor for the merges data. May not be null.
     * @return The merges data.
     */
    @Override
    public double[] getMergesData() {
        return mergesData;
    }

    /**
     * Simple accessor for the tallying data. May not be null.
     * @return The tallying data.
     */
    @Override
    public double[] getTallyingData() {
        return tallyingData;
    }

    /**
     * Simple accessor for the deductions data. May not be null.
     * @return The deductions data.
     */
    @Override
    public double[] getDeductionsData() {
        return deductionsData;
    }


    @Override
    public String toString() { return String.format("%s", name); }

    @Override
    public boolean equals(Object o){
        if(!(o instanceof ReportImpl)){
            return false;
        }
        return  Arrays.equals(((ReportImpl) o).cashFlowData, this.cashFlowData) &&
                Arrays.equals(((ReportImpl) o).deductionsData, this.deductionsData) &&
                Arrays.equals(((ReportImpl) o).legalData, this.legalData) &&
                Arrays.equals(((ReportImpl) o).tallyingData, this.tallyingData) &&
                Arrays.equals(((ReportImpl) o).mergesData, this.mergesData) &&
                (((ReportImpl) o).commissionPerEmployee == this.commissionPerEmployee) &&
                (((ReportImpl) o).name.equals(this.name));
    }

    @Override
    public int hashCode() {
        return this.hashCode;
    }

    /**
     * Calculate Hashcode once to save computation
     * @return the calculated hash. May be negative.
     */
    private int calculateHash() {
        int result = 11;
        result = 37 * result + name.hashCode();
        result = 37 * result + ((Double)commissionPerEmployee).hashCode();
        List<double[]> types = Arrays.asList(cashFlowData,deductionsData,legalData,tallyingData,mergesData);
        for(double[] t : types){
            if(t == null){ continue; }
            for (double v : t) {
                result = 37 * result + ((Double) v).hashCode();
            }
        }
        return result;
    }
}
