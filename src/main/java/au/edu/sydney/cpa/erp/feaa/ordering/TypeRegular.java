package au.edu.sydney.cpa.erp.feaa.ordering;

public class TypeRegular implements Type {

    private int maxCountedEmployees;

    @Override
    public int getMaxCountedEmployees() {
        return maxCountedEmployees;
    }

    @Override
    public void setMaxCounterEmployees(int e) {
        this.maxCountedEmployees = e;
    }
}
