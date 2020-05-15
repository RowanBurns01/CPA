package au.edu.sydney.cpa.erp.feaa.reports;

import java.util.*;

public class DoubleArrayFactory {

    private static Map<Integer,double[]> hm = new HashMap<>();

    public static double[] getDoubleArray(double[] d){

        int hash = Arrays.hashCode(d);
        if(hm.containsKey(hash)){
//            System.out.println("reuses");
            return hm.get(hash);
        } else {
            hm.put(hash,d);
            return d;
        }

    }
}
