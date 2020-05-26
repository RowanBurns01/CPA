package au.edu.sydney.cpa.erp.feaa;

import au.edu.sydney.cpa.erp.auth.AuthModule;
import au.edu.sydney.cpa.erp.auth.AuthToken;
import au.edu.sydney.cpa.erp.database.TestDatabase;
import au.edu.sydney.cpa.erp.feaa.handlers.*;
import au.edu.sydney.cpa.erp.feaa.ordering.*;
import au.edu.sydney.cpa.erp.ordering.Client;
import au.edu.sydney.cpa.erp.ordering.Order;
import au.edu.sydney.cpa.erp.ordering.Report;
import au.edu.sydney.cpa.erp.feaa.reports.ReportDatabase;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SuppressWarnings("Duplicates")
public class FEAAFacade {
    private AuthToken token;
    private UnitOfWork transaction;
    private ExecutorService pool = Executors.newFixedThreadPool(1);

    /**
     * Calls the AuthModules login method passing user name and password as strings.
     * @param userName Entered user name string.
     * @param password Entered password string.
     * @return boolean of whether login was successful or not.
     */
    public boolean login(String userName, String password) {
        token = AuthModule.login(userName, password);
        startTransaction();
        return null != token;
    }

    /**
     * Creates a new UnitOfWork transaction.
     */
    private void startTransaction(){
        transaction = new UnitOfWork();
    }

    /**
     * Non-simple accessor. It checks if the order is either in the database or stored locally, and then retrieves it.
     * @param token an authentication token.
     * @param orderID the orderID of the order sought.
     * @return retrieves the order which has the orderID provided. May be null.
     */
    private Order getOrder(AuthToken token, int orderID){
        Order order =TestDatabase.getInstance().getOrder(token, orderID);
        if(order == null){
            order = transaction.getOrder(orderID);
        }
        return order;
    }

    /**
     * Simple accessor which retrieves orders from the database.
     * @return List of order ids. May be empty.
     */
    public List<Integer> getAllOrders() {
        if (null == token) {
            throw new SecurityException();
        }

        TestDatabase database = TestDatabase.getInstance();
        List<Order> orders = database.getOrders(token);
        orders.addAll(transaction.getNewObjects());

        List<Integer> result = new ArrayList<>();

        for (Order order : orders) {
            result.add(order.getOrderID());
        }

        return result;
    }

    /**
     * Creates a new order using the information provided.
     * @param clientID The id of the client for the order.
     * @param date The local date at which it is created.
     * @param isCritical Boolean whether the order is critical or not.
     * @param isScheduled Boolean whether the order is repeated or not.
     * @param orderType Int of whether the order is regular accounting or audit work.
     * @param criticalLoadingRaw The critical loading value to be applied. May be negative.
     * @param maxCountedEmployees The max counted employees for the order. May be negative if not initialized.
     * @param numQuarters The number of quarters a scheduled report. May be negative if not initialized.
     * @return The order id.
     */
    public Integer createOrder(int clientID, LocalDateTime date, boolean isCritical, boolean isScheduled, int orderType, int criticalLoadingRaw, int maxCountedEmployees, int numQuarters) {
        if (null == token) {
            throw new SecurityException();
        }

        double criticalLoading = criticalLoadingRaw / 100.0;

        Order order;

        if (!TestDatabase.getInstance().getClientIDs(token).contains(clientID)) {
            throw new IllegalArgumentException("Invalid client ID");
        }

        int id = TestDatabase.getInstance().getNextOrderID();

        if (isScheduled) { // Scheduled Orders
            if (1 == orderType) { // 1 is RegularType
                if (isCritical) {
                    order = new ScheduledOrderImpl( new ImportanceCritical(), new TypeRegular(), id, clientID, date, criticalLoading, numQuarters, maxCountedEmployees);
                } else {
                    order = new ScheduledOrderImpl(new ImportanceNormal(), new TypeRegular(), id, clientID, date, -1, numQuarters, maxCountedEmployees);
                }
            } else if (2 == orderType) { // 2 is AuditType
                    if (isCritical) {
                        order = new ScheduledOrderImpl(new ImportanceCritical(), new TypeAudit(), id, clientID, date, criticalLoading, numQuarters, -1);
                    } else {
                        order = new ScheduledOrderImpl(new ImportanceNormal(), new TypeAudit(), id, clientID, date, -1, numQuarters, -1);
                    }
            } else {return null;}
        } else { // Single Orders
            if (1 == orderType) { // 1 is RegularType
                if (isCritical) {
                    order = new SingleOrderImpl( new ImportanceCritical(), new TypeRegular(), id, clientID, date, criticalLoading, maxCountedEmployees);
                } else {
                    order = new SingleOrderImpl( new ImportanceNormal(), new TypeRegular(), id, clientID, date, -1, maxCountedEmployees);
                }
            } else if (2 == orderType) {  // 2 is AuditType
                if (isCritical) {
                    order = new SingleOrderImpl( new ImportanceCritical(), new TypeAudit(), id, clientID, date, criticalLoading, -1);
                } else {
                    order = new SingleOrderImpl( new ImportanceNormal(), new TypeAudit(), id, clientID, date, -1, -1);
                }
            } else {return null;}
        }

        transaction.registerNew(order);
        return order.getOrderID();
    }

    /**
     * Simple accessor calling the database's getClientIDs method.
     * @return Returns a list of the client ids. May be empty.
     */
    public List<Integer> getAllClientIDs() {
        if (null == token) {
            throw new SecurityException();
        }

        TestDatabase database = TestDatabase.getInstance();
        return database.getClientIDs(token);
    }

    /**
     * Simple accessor that returns the client with the corresponding id.
     * @param id The id of the client to retrieve.
     * @return The client. May be null.
     */
    public Client getClient(int id) {
        if (null == token) {
            throw new SecurityException();
        }

        return new ClientImpl(token, id);
    }

    /**
     * Removes an order if it already exists.
     * @param id Order to be removed is identified by its id.
     * @return Returns whether the order was successfully removed. If not found, it will return false.
     */
    public boolean removeOrder(int id) {
        if (null == token) {
            throw new SecurityException();
        }

        TestDatabase database = TestDatabase.getInstance();
        boolean result = database.removeOrder(token, id);
        if(!result){
            // Check if its in our current session
            if(transaction.getOrder(id) != null){
                transaction.registerDeleted(database.getOrder(token, id));
                return true;
            }
        }
       return result;

    }
    /**
     * Simple accessor that retrieves the order's reports.
     * @return A list of the order's reports.
     */
    public List<Report> getAllReports() {
        if (null == token) {
            throw new SecurityException();
        }

        List<Report> reports = new ArrayList<>(ReportDatabase.getTestReports());

        System.out.println("RAM: " + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory())/(1024*1024) +"MB");
        return reports;
    }

    /**
     * Finalises the order, at ths point the changed orders are saved to the database.
     * @param orderID The order id of the order to be finalised.
     * @param contactPriority A priority contact method for sending an invoice. May be null.
     * @return Whether an invoice was successfully sent for the invoice.
     */
    public boolean finaliseOrder(int orderID, List<String> contactPriority) {
        if (null == token) {
            throw new SecurityException();
        }

        ContactChain chain = null;

        // Priority contact is given
        if (null != contactPriority) {
            for (String method: contactPriority) {
                switch (method.toLowerCase()) {
                    case "internal accounting":
                        if(chain == null){
                            chain = new InternalAccountingHandler(null);
                        } else {
                            chain.setNext(new InternalAccountingHandler(null));
                        }
                        break;
                    case "email":
                        if(chain == null){
                            chain = new EmailHandler(null);
                        } else {
                            chain.setNext(new EmailHandler(null));
                        }
                        break;
                    case "carrier pigeon":
                        if(chain == null){
                            chain = new CarrierPigeonHandler(null);
                        } else {
                            chain.setNext(new CarrierPigeonHandler(null));
                        }
                        break;
                    case "mail":
                        if(chain == null){
                            chain = new MailHandler(null);
                        } else {
                            chain.setNext(new MailHandler(null));
                        }
                        break;
                    case "phone call":
                        if(chain == null){
                            chain = new PhoneCallHandler(null);
                        } else {
                            chain.setNext(new PhoneCallHandler(null));
                        }
                        break;
                    case "sms":
                        if(chain == null){
                            chain = new SMSHandler(null);
                        } else {
                            chain.setNext(new SMSHandler(null));
                        }
                        break;
                    default:
                        break;
                }
            }
        }

        // If no priority contact was given
        if(chain == null){
            chain = new InternalAccountingHandler( new EmailHandler(
                    new CarrierPigeonHandler( new MailHandler( new PhoneCallHandler( null)))));
        }

        Order order = getOrder(token, orderID);

        order.finalise();
        transaction.registerDirty(order);
        pool.execute(() -> transaction.commit(token));
        return chain.sendInvoice(token, getClient(order.getClient()), order.generateInvoiceData());
    }

    /**
     * Logs out of the current session, and automatically saves any unsaved changes to the database.
     */
    public void logout() {

        pool.execute(() -> transaction.commit(token));
        try {
            Thread.sleep(20);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        pool.shutdown();
        AuthModule.logout(token);
        token = null;
    }

    /**
     * Simple accessor that retrieves the order's total commission.
     * @param orderID The id of the order to access.
     * @return The order's total commission. May not be negative.
     */
    public double getOrderTotalCommission(int orderID) {
        if (null == token) {
            throw new SecurityException();
        }

        Order order = getOrder(token, orderID);

        if (null == order) {
            return 0.0;
        }

        return order.getTotalCommission();
    }

    /**
     * Sets the reports for the order.
     * @param orderID The order id of the order being created.
     * @param report The report to be set.
     * @param numEmployees The number of employees to work on the report.
     */
    public void orderLineSet(int orderID, Report report, int numEmployees) {
        if (null == token) {
            throw new SecurityException();
        }

        Order order = getOrder(token, orderID);

        if (null == order) {
            System.out.println("got here");
            return;
        }

        order.setReport(report, numEmployees);

        transaction.registerDirty(order);
    }

    /**
     * Simple accessor that retrieves the order's long description.
     * @param orderID The id of the order to access.
     * @return The order's long description. May not be null.
     */
    public String getOrderLongDesc(int orderID) {
        if (null == token) {
            throw new SecurityException();
        }

        Order order = getOrder(token, orderID);
        if (null == order) {
            return null;
        }
        return order.longDesc();
    }

    /**
     * Simple accessor that retrieves the order's short description.
     * @param orderID The id of the order to access.
     * @return The order's short description. May not be null.
     */
    public String getOrderShortDesc(int orderID) {
        if (null == token) {
            throw new SecurityException();
        }

        Order order = getOrder(token, orderID);

        if (null == order) {
            return null;
        }

        return order.shortDesc();
    }

    /**
     * Simple accessor that retrieves the known contact methods set statically in ContactHandler.
     * @return The known contact methods.
     */
    public List<String> getKnownContactMethods() {
        if (null == token) {
            throw new SecurityException();
        }
        return ContactChain.getKnownContactMethods();
    }
}
