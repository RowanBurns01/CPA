package au.edu.sydney.cpa.erp.feaa.handlers;

import au.edu.sydney.cpa.erp.auth.AuthToken;
import au.edu.sydney.cpa.erp.contact.Mail;
import au.edu.sydney.cpa.erp.ordering.Client;

public class MailHandler implements ContactChain {

    private ContactChain next;

    public MailHandler(ContactChain next){
        this.next = next;
    }

    @Override
    public boolean sendInvoice(AuthToken token, Client client, String data) {
        String address = client.getAddress();
        String suburb = client.getSuburb();
        String state = client.getState();
        String postcode = client.getPostCode();
        if (null != address && null != suburb &&
                null != state && null != postcode) {
            Mail.sendInvoice(token, client.getFName(), client.getLName(), data, address, suburb, state, postcode);
            return true;
        }
        if(next == null){
            return false;
        }
        return next.sendInvoice(token,client,data);
    }

    @Override
    public void setNext(ContactChain handler) {
        this.next = handler;
    }
}
