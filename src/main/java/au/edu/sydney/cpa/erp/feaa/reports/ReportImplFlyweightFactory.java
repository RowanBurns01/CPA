package au.edu.sydney.cpa.erp.feaa.reports;

import java.util.*;

/**
 * Creates and manages flyweight objects
 */
public class ReportImplFlyweightFactory {

    private static Map<Integer,double[]> doubleArrayHashMap = new HashMap<>();
    private static Map<Integer,String> stringHashMap = new HashMap<>();

    /**
     * Static retrieval method using hash codes. If the array already exists return that instance of it, otherwise add
     * the instance and return that.
     * @param d The double array being checked.
     * @return The instance of the array stored in the hash map.
     */
    public static double[] getDoubleArray(double[] d){

        int hash = Arrays.hashCode(d);
        if(doubleArrayHashMap.containsKey(hash)){
            return doubleArrayHashMap.get(hash);
        } else {
            doubleArrayHashMap.put(hash,d);
            return d;
        }
    }

    /**
     * Static retrieval method using hash codes. If the String already exists return that instance of it, otherwise add
     * the instance and return that.
     * @param s The String being checked.
     * @return The instance of the String stored in the hash map.
     */
    public static String getString(String s){

        int hash = s.hashCode();
        if(stringHashMap.containsKey(hash)){
            return stringHashMap.get(hash);
        } else {
            stringHashMap.put(hash,s);
            return s;
        }
    }
}
