package au.edu.sydney.cpa.erp.feaa.ordering;

import au.edu.sydney.cpa.erp.ordering.Report;

import java.util.Map;

public class TypeRegular implements Type {

    private int maxCountedEmployees;

    @Override
    public int getMaxCountedEmployees() {
        return maxCountedEmployees;
    }

    @Override
    public void setMaxCountedEmployees(int e) {
        this.maxCountedEmployees = e;
    }

    @Override
    public double addTotalCommission(Map<Report,Integer> reports, Report report) {
        return report.getCommission() * Math.min(maxCountedEmployees, reports.get(report));
    }

    @Override
    public StringBuilder addCapped(StringBuilder sb, Integer i) {
        if (i> maxCountedEmployees) {
            sb.append("\tThis report cost has been capped.");
        }
        return sb;
    }

    @Override
    public StringBuilder addCappedForDesc(StringBuilder reportSB, Integer i) {
        if (i > maxCountedEmployees) {
            reportSB.append(" *CAPPED*\n");
        } else {
//            reportSB.append("\n");
        }
        return reportSB;
    }
}
