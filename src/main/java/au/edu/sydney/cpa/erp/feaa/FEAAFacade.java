package au.edu.sydney.cpa.erp.feaa;

import au.edu.sydney.cpa.erp.auth.AuthModule;
import au.edu.sydney.cpa.erp.auth.AuthToken;
import au.edu.sydney.cpa.erp.database.TestDatabase;
import au.edu.sydney.cpa.erp.ordering.Client;
import au.edu.sydney.cpa.erp.ordering.Order;
import au.edu.sydney.cpa.erp.ordering.Report;
import au.edu.sydney.cpa.erp.feaa.ordering.*;
import au.edu.sydney.cpa.erp.feaa.reports.ReportDatabase;

import java.time.LocalDateTime;
import java.util.*;

@SuppressWarnings("Duplicates")
public class FEAAFacade {
    private AuthToken token;
    private Map<Integer,Client> clientMap = new HashMap<>();

    public boolean login(String userName, String password) {
        token = AuthModule.login(userName, password);

        return null != token;
    }

    public List<Integer> getAllOrders() {
        if (null == token) {
            throw new SecurityException();
        }

        TestDatabase database = TestDatabase.getInstance();

        List<Order> orders = database.getOrders(token);

        List<Integer> result = new ArrayList<>();

        for (Order order : orders) {
            result.add(order.getOrderID());
        }

        return result;
    }

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

        if (isScheduled) {
            if (1 == orderType) { // 1 is regular accounting
                if (isCritical) {
                    order = new FirstOrderTypeScheduled(id, clientID, date, criticalLoading, maxCountedEmployees, numQuarters);
                } else {
                    order = new Order66Scheduled(id, clientID, date, maxCountedEmployees, numQuarters);
                }
            } else if (2 == orderType) { // 2 is audit
                    if (isCritical) {
                        order = new CriticalAuditOrderScheduled(id, clientID, date, criticalLoading, numQuarters);
                    } else {
                        order = new NewOrderImplScheduled(id, clientID, date, numQuarters);
                    }
            } else {return null;}
        } else {
            if (1 == orderType) {
                if (isCritical) {
                    order = new FirstOrderType(id, clientID, date, criticalLoading, maxCountedEmployees);
                } else {
                    order = new Order66(id, clientID, date, maxCountedEmployees);
                }
            } else if (2 == orderType) {
                if (isCritical) {
                    order = new CriticalAuditOrder(id, clientID, date, criticalLoading);
                } else {
                    order = new NewOrderImpl(id, clientID, date);
                }
            } else {return null;}
        }

        TestDatabase.getInstance().saveOrder(token, order);
        return order.getOrderID();
    }

    public List<Integer> getAllClientIDs() {
        if (null == token) {
            throw new SecurityException();
        }

        TestDatabase database = TestDatabase.getInstance();
        return database.getClientIDs(token);
    }

    public Client getClient(int id) {
        if (null == token) {
            throw new SecurityException();
        }

        Client client;
        if(clientMap.get(id) != null){
            client = clientMap.get(id);
        } else {
            client = new ClientImpl(token, id);
            clientMap.put(id,client);
        }

        return client;
    }

    public boolean removeOrder(int id) {
        if (null == token) {
            throw new SecurityException();
        }

        TestDatabase database = TestDatabase.getInstance();
        return database.removeOrder(token, id);
    }

    public List<Report> getAllReports() {
        if (null == token) {
            throw new SecurityException();
        }

        return new ArrayList<>(ReportDatabase.getTestReports());
    }

    public boolean finaliseOrder(int orderID, List<String> contactPriority) {
        if (null == token) {
            throw new SecurityException();
        }

        List<ContactMethod> contactPriorityAsMethods = new ArrayList<>();

        if (null != contactPriority) {
            for (String method: contactPriority) {
                switch (method.toLowerCase()) {
                    case "internal accounting":
                        contactPriorityAsMethods.add(ContactMethod.INTERNAL_ACCOUNTING);
                        break;
                    case "email":
                        contactPriorityAsMethods.add(ContactMethod.EMAIL);
                        break;
                    case "carrier pigeon":
                        contactPriorityAsMethods.add(ContactMethod.CARRIER_PIGEON);
                        break;
                    case "mail":
                        contactPriorityAsMethods.add(ContactMethod.MAIL);
                        break;
                    case "phone call":
                        contactPriorityAsMethods.add(ContactMethod.PHONECALL);
                        break;
                    case "sms":
                        contactPriorityAsMethods.add(ContactMethod.SMS);
                        break;
                    default:
                        break;
                }
            }
        }

        if (contactPriorityAsMethods.size() == 0) { // needs setting to default
            contactPriorityAsMethods = Arrays.asList(
                    ContactMethod.INTERNAL_ACCOUNTING,
                    ContactMethod.EMAIL,
                    ContactMethod.CARRIER_PIGEON,
                    ContactMethod.MAIL,
                    ContactMethod.PHONECALL
            );
        }

        Order order = TestDatabase.getInstance().getOrder(token, orderID);

        order.finalise();
        TestDatabase.getInstance().saveOrder(token, order);
        return ContactHandler.sendInvoice(token, getClient(order.getClient()), contactPriorityAsMethods, order.generateInvoiceData());
    }

    public void logout() {
        AuthModule.logout(token);
        token = null;
    }

    public double getOrderTotalCommission(int orderID) {
        if (null == token) {
            throw new SecurityException();
        }

        Order order = TestDatabase.getInstance().getOrder(token, orderID);
        if (null == order) {
            return 0.0;
        }

        return order.getTotalCommission();
    }

    public void orderLineSet(int orderID, Report report, int numEmployees) {
        if (null == token) {
            throw new SecurityException();
        }

        Order order = TestDatabase.getInstance().getOrder(token, orderID);

        if (null == order) {
            System.out.println("got here");
            return;
        }

        order.setReport(report, numEmployees);

        TestDatabase.getInstance().saveOrder(token, order);
    }

    public String getOrderLongDesc(int orderID) {
        if (null == token) {
            throw new SecurityException();
        }

        Order order = TestDatabase.getInstance().getOrder(token, orderID);

        if (null == order) {
            return null;
        }

        return order.longDesc();
    }

    public String getOrderShortDesc(int orderID) {
        if (null == token) {
            throw new SecurityException();
        }

        Order order = TestDatabase.getInstance().getOrder(token, orderID);

        if (null == order) {
            return null;
        }

        return order.shortDesc();
    }

    public List<String> getKnownContactMethods() {
        if (null == token) {
            throw new SecurityException();
        }
        return ContactHandler.getKnownMethods();
    }
}
