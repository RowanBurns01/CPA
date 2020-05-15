package au.edu.sydney.cpa.erp.feaa.reports;

import au.edu.sydney.cpa.erp.ordering.Report;

import java.util.Arrays;
import java.util.List;

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

        this.name = name;
        this.commissionPerEmployee = commissionPerEmployee;
        this.legalData = DoubleArrayFactory.getDoubleArray(legalData);
        this.cashFlowData = DoubleArrayFactory.getDoubleArray(cashFlowData);
        this.mergesData = DoubleArrayFactory.getDoubleArray(mergesData);
        this.tallyingData = DoubleArrayFactory.getDoubleArray(tallyingData);
        this.deductionsData = DoubleArrayFactory.getDoubleArray(deductionsData);
        this.hashCode = calculateHash();
    }

    @Override
    public String getReportName() {
        return name;
    }

    @Override
    public double getCommission() {
        return commissionPerEmployee;
    }

    @Override
    public double[] getLegalData() {
        return legalData;
    }

    @Override
    public double[] getCashFlowData() {
        return cashFlowData;
    }

    @Override
    public double[] getMergesData() {
        return mergesData;
    }

    @Override
    public double[] getTallyingData() {
        return tallyingData;
    }

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

    // Calculate Hashcode once to save computation
    public int calculateHash() {
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
