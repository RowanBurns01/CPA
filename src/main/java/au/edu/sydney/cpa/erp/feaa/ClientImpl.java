package au.edu.sydney.cpa.erp.feaa;

import au.edu.sydney.cpa.erp.auth.AuthToken;
import au.edu.sydney.cpa.erp.database.TestDatabase;
import au.edu.sydney.cpa.erp.ordering.Client;

public class ClientImpl implements Client {

    private final int id;
    private String fName;
    private String lName;
    private String phoneNumber;
    private String emailAddress;
    private String address;
    private String suburb;
    private String state;
    private String postCode;
    private String internalAccounting;
    private String businessName;
    private String pigeonCoopID;

    public ClientImpl(AuthToken token, int id) {

        this.id = id;
        this.fName = TestDatabase.getInstance().getClientField(token, id, "fName");
        this.lName = TestDatabase.getInstance().getClientField(token, id, "lName");
        this.phoneNumber = TestDatabase.getInstance().getClientField(token, id, "phoneNumber");
        this.emailAddress = TestDatabase.getInstance().getClientField(token, id, "emailAddress");
        this.address = TestDatabase.getInstance().getClientField(token, id, "address");
        this.suburb = TestDatabase.getInstance().getClientField(token, id, "suburb");
        this.state = TestDatabase.getInstance().getClientField(token, id, "state");
        this.postCode = TestDatabase.getInstance().getClientField(token, id, "postCode");
        this.internalAccounting = TestDatabase.getInstance().getClientField(token, id, "internal accounting");
        this.businessName = TestDatabase.getInstance().getClientField(token, id, "businessName");
        this.pigeonCoopID = TestDatabase.getInstance().getClientField(token, id, "pigeonCoopID");
    }

    public int getId() {
        return id;
    }

    @Override
    public String getFName() {
        return fName;
    }

    @Override
    public String getLName() {
        return lName;
    }

    @Override
    public String getPhoneNumber() {
        return phoneNumber;
    }

    @Override
    public String getEmailAddress() {
        return emailAddress;
    }

    @Override
    public String getAddress() {
        return address;
    }

    @Override
    public String getSuburb() {
        return suburb;
    }

    @Override
    public String getState() {
        return state;
    }

    @Override
    public String getPostCode() {
        return postCode;
    }

    @Override
    public String getInternalAccounting() {
        return internalAccounting;
    }

    @Override
    public String getBusinessName() {
        return businessName;
    }

    @Override
    public String getPigeonCoopID() {
        return pigeonCoopID;
    }
}

