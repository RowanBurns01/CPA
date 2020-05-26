package au.edu.sydney.cpa.erp.feaa.handlers;

import au.edu.sydney.cpa.erp.auth.AuthToken;
import au.edu.sydney.cpa.erp.ordering.Client;

import java.util.Arrays;
import java.util.List;

public interface ContactChain {

    /**
     * Attempts to send an invoice through a ContactChain handler, if it is unable to be completed, it tries the next handler provided.
     * @param token An authorization token, may be authenticated or null.
     * @param client The client who is receiving the invoice, may not be null.
     * @param data The String contents of the invoice.
     * @return
     */
    boolean sendInvoice(AuthToken token, Client client, String data);

    /**
     * Sets the next handler to be used if the current cannot be carried out.
     * @param handler the next ContactChain handler, may be null.
     */
    void setNext(ContactChain handler);

    /**
     * Static accessor for known contact methods.
     * @return List of Strings of known contact methods.
     */
    static List<String> getKnownContactMethods(){
        return Arrays.asList(
                "Carrier Pigeon",
                "Email",
                "Mail",
                "Internal Accounting",
                "Phone call",
                "SMS"
        );
    }
}
