package au.edu.sydney.cpa.erp.feaa;

import au.edu.sydney.cpa.erp.auth.AuthToken;
import au.edu.sydney.cpa.erp.database.TestDatabase;
import au.edu.sydney.cpa.erp.ordering.Client;

/**
 * A Client and their details. A Client may make orders.
 */
public class ClientImpl implements Client {

    private final int id;
    private AuthToken token;

    public ClientImpl(AuthToken token, int id) {
        this.id = id;
        this.token = token;
    }

    public int getId() {
        return id;
    }

    /**
     * Simple accessor that retrieves the client's first name from database using a data base call.
     * @return The client's first name.
     */
    @Override
    public String getFName() {
        return TestDatabase.getInstance().getClientField(token, id, "fName");
    }

    /**
     * Simple accessor that retrieves last name from database using a data base call.
     * @return The client's last name.
     */
    @Override
    public String getLName() {
       return TestDatabase.getInstance().getClientField(token, id, "lName");
    }

    /**
     * Simple accessor that retrieves the client's phone number from database using a data base call.
     * @return The client's phone number.
     */
    @Override
    public String getPhoneNumber() {
        return TestDatabase.getInstance().getClientField(token, id, "phoneNumber");
    }

    /**
     * Simple accessor that retrieves the client's email address from database using a data base call.
     * @return The client's email address.
     */
    @Override
    public String getEmailAddress() {
        return TestDatabase.getInstance().getClientField(token, id, "emailAddress");
    }

    /**
     * Simple accessor that retrieves the client's address from database using a data base call.
     * @return The client's address .
     */
    @Override
    public String getAddress() {
        return TestDatabase.getInstance().getClientField(token, id, "address");
    }

    /**
     * Simple accessor that retrieves the client's suburb from database using a data base call.
     * @return The client's suburb.
     */
    @Override
    public String getSuburb() {
        return TestDatabase.getInstance().getClientField(token, id, "suburb");
    }

    /**
     * Simple accessor that retrieves the client's state from database using a data base call.
     * @return The client's state.
     */
    @Override
    public String getState() {
        return  TestDatabase.getInstance().getClientField(token, id, "state");
    }

    /**
     * Simple accessor that retrieves the client's postcode from database using a data base call.
     * @return The client's postcode .
     */
    @Override
    public String getPostCode() {
        return TestDatabase.getInstance().getClientField(token, id, "postCode");
    }

    /**
     * Simple accessor that retrieves the client's internal accounting from database using a data base call.
     * @return The client's internal accounting.
     */
    @Override
    public String getInternalAccounting() {
        return TestDatabase.getInstance().getClientField(token, id, "internal accounting");
    }

    /**
     * Simple accessor that retrieves the client's business name from database using a data base call.
     * @return The client's business name.
     */
    @Override
    public String getBusinessName() {
        return TestDatabase.getInstance().getClientField(token, id, "businessName");
    }

    /**
     * Simple accessor that retrieves the client's pigeon coop id from database using a data base call.
     * Y'know, for storing the carrier pigeons.
     * @return The client's pigeon coop id.
     */
    @Override
    public String getPigeonCoopID() {
        return TestDatabase.getInstance().getClientField(token, id, "pigeonCoopID");
    }
}

