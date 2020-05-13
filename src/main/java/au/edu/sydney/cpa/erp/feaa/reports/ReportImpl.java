package au.edu.sydney.cpa.erp.feaa.reports;

import au.edu.sydney.cpa.erp.ordering.Report;

public class ReportImpl implements Report {

    private String name;
    private double commissionPerEmployee;
    private double[] legalData;
    private double[] cashFlowData;
    private double[] mergesData;
    private double[] tallyingData;
    private double[] deductionsData;

    public ReportImpl(String name,
                      double commissionPerEmployee,
                      double[] legalData,
                      double[] cashFlowData,
                      double[] mergesData,
                      double[] tallyingData,
                      double[] deductionsData) {
        this.name = name;
        this.commissionPerEmployee = commissionPerEmployee;
        this.legalData = legalData;
        this.cashFlowData = cashFlowData;
        this.mergesData = mergesData;
        this.tallyingData = tallyingData;
        this.deductionsData = deductionsData;
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
    public String toString() {

        return String.format("%s", name);
    }
}
