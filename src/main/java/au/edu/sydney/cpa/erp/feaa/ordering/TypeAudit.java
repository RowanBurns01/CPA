package au.edu.sydney.cpa.erp.feaa.ordering;

import au.edu.sydney.cpa.erp.ordering.Report;

import java.util.Map;

public class TypeAudit implements Type {
    @Override
    public int getMaxCountedEmployees() {
        return -1;
    }

    @Override
    public void setMaxCountedEmployees(int e) { }

    @Override
    public double addTotalCommission(Map<Report,Integer> reports, Report report) {
        return reports.get(report) * report.getCommission();
    }

    @Override
    public StringBuilder addCapped(StringBuilder sb, Integer i) {
        return sb;
    }

    @Override
    public StringBuilder addCappedForDesc(StringBuilder reportSB, Integer i) { return reportSB; }
}
