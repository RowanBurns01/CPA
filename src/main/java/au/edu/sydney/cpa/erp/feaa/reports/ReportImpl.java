package au.edu.sydney.cpa.erp.feaa.reports;

import au.edu.sydney.cpa.erp.ordering.Report;

import java.util.Arrays;
import java.util.Objects;

public class ReportImpl implements Report {

    private final String name;
    private final double commissionPerEmployee;
    private final double[] legalData;
    private final double[] cashFlowData;
    private final double[] mergesData;
    private final double[] tallyingData;
    private final double[] deductionsData;

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
        return Objects.hash(cashFlowData, commissionPerEmployee, deductionsData, legalData, tallyingData, mergesData, name);
    }
}
