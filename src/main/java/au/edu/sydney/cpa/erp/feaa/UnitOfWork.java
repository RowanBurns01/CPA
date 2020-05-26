package au.edu.sydney.cpa.erp.feaa;

import au.edu.sydney.cpa.erp.auth.AuthToken;
import au.edu.sydney.cpa.erp.database.TestDatabase;
import au.edu.sydney.cpa.erp.ordering.Order;

import java.util.ArrayList;
import java.util.List;

/**
 * Ensures that orders saved to the database occur at the end of one transaction and not throughout.
 */
public class UnitOfWork {

    private List<Order> newObjects = new ArrayList<>();
    private List<Order> dirtyObjects = new ArrayList<>();
    private List<Order> removedObjects = new ArrayList<>();

    /**
     * Adds an order to the list of new orders created in this transaction.
     * @param entity The new order created. May not be null.
     */
    public void registerNew(Order entity) {
        newObjects.add(entity);
    }

    /**
     * Adds an order to the list of dirty orders in this transaction.
     * @param entity The dirty order added. May not be null.
     */
    public void registerDirty(Order entity) {
        if(!dirtyObjects.contains(entity) && !newObjects.contains(entity)) {
            dirtyObjects.add(entity);
        }
    }

    /**
     * Adds an order to the list of deleted orders in this transaction.
     * @param entity The deleted order added. May not be null.
     */
    public void registerDeleted(Order entity) {
        if (newObjects.remove(entity)) {
            return;
        }
        dirtyObjects.remove(entity);
        if (!removedObjects.contains(entity)) {
            removedObjects.add(entity);
        }
    }

    /**
     * Simple accessor of an order using its order id.
     * @param id The id of the order required.
     * @return The order found or null if no order is returned.
     */
    public Order getOrder(int id){
        ArrayList<Order> list = new ArrayList<>(getDirtyObjects());
        list.addAll(getNewObjects());
        for(Order o: list){
            if(o.getOrderID() == id){
                return o;
            }
        }
        return null;
    }

    public List<Order> getNewObjects(){
        return newObjects;
    }

    public List<Order> getDirtyObjects(){
        return dirtyObjects;
    }

    /**
     * Ends the transaction and saves all the new, dirty and deleted orders to the database.
     * @param token Authenticated token providing security access.
     */
    public void commit(AuthToken token) {
        for(Order order : newObjects){
            TestDatabase.getInstance().saveOrder(token, order);
        }
        for(Order order : dirtyObjects){
            TestDatabase.getInstance().saveOrder(token, order);
        }
        for(Order order: removedObjects){
            TestDatabase.getInstance().removeOrder(token, order.getOrderID());
        }
        newObjects.clear();
        dirtyObjects.clear();
        removedObjects.clear();
    }

}
