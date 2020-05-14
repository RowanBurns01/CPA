package au.edu.sydney.cpa.erp.feaa;

import au.edu.sydney.cpa.erp.auth.AuthToken;
import au.edu.sydney.cpa.erp.database.TestDatabase;
import au.edu.sydney.cpa.erp.ordering.Client;

public class ClientImpl implements Client {

    private final int id;
    private String fName = null, lName = null, phoneNumber = null, emailAddress = null, address = null, suburb = null,
            state = null, postCode = null, internalAccounting = null, businessName = null, pigeonCoopID = null;
    private Boolean fNameRetrieved = false, lNameRetrieved = false, phoneNumberRetrieved = false, emailAddressRetrieved = false,
            addressRetrieved = false, suburbRetrieved = false, stateRetrieved = false, postCodeRetrieved = false,
            internalAccountingRetrieved = false, businessNameRetrieved = false, pigeonCoopIDRetrieved = false;
    private AuthToken token;

    public ClientImpl(AuthToken token, int id) {

        this.id = id;
        this.token = token;
    }

    public int getId() {
        return id;
    }

    @Override
    public String getFName() {
        if(fName == null || !fNameRetrieved)
            fName = TestDatabase.getInstance().getClientField(token, id, "fName");
        fNameRetrieved = true;
        return fName;
    }

    @Override
    public String getLName() {
        if(lName == null || !lNameRetrieved)
            lName = TestDatabase.getInstance().getClientField(token, id, "lName");
        lNameRetrieved = true;
        return lName;
    }

    @Override
    public String getPhoneNumber() {
        if(phoneNumber == null || !phoneNumberRetrieved)
            phoneNumber = TestDatabase.getInstance().getClientField(token, id, "phoneNumber");
        phoneNumberRetrieved = true;
        return phoneNumber;
    }

    @Override
    public String getEmailAddress() {
        if(emailAddress == null || !emailAddressRetrieved)
            emailAddress = TestDatabase.getInstance().getClientField(token, id, "emailAddress");
        emailAddressRetrieved = true;
        return emailAddress;
    }

    @Override
    public String getAddress() {
        if(address == null || !addressRetrieved)
            address = TestDatabase.getInstance().getClientField(token, id, "address");
        addressRetrieved = true;
        return address;
    }

    @Override
    public String getSuburb() {
        if(suburb == null || !suburbRetrieved)
            suburb = TestDatabase.getInstance().getClientField(token, id, "suburb");
        suburbRetrieved = true;
        return suburb;
    }

    @Override
    public String getState() {
        if(state == null || !stateRetrieved)
            state = TestDatabase.getInstance().getClientField(token, id, "state");
        stateRetrieved = true;
        return state;
    }

    @Override
    public String getPostCode() {
        if(postCode == null || !postCodeRetrieved)
            postCode = TestDatabase.getInstance().getClientField(token, id, "postCode");
        postCodeRetrieved = true;
        return postCode;
    }

    @Override
    public String getInternalAccounting() {
        if(internalAccounting == null || !internalAccountingRetrieved)
            internalAccounting = TestDatabase.getInstance().getClientField(token, id, "internal accounting");
        internalAccountingRetrieved = true;
        return internalAccounting;
    }

    @Override
    public String getBusinessName() {
        if(businessName == null || !businessNameRetrieved)
            businessName = TestDatabase.getInstance().getClientField(token, id, "businessName");
        businessNameRetrieved = true;
        return businessName;
    }

    @Override
    public String getPigeonCoopID() {
        if(pigeonCoopID == null || !pigeonCoopIDRetrieved)
            pigeonCoopID = TestDatabase.getInstance().getClientField(token, id, "pigeonCoopID");
        pigeonCoopIDRetrieved = true;
        return pigeonCoopID;
    }
}

