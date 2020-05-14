package au.edu.sydney.cpa.erp.feaa.handlers;

import au.edu.sydney.cpa.erp.auth.AuthToken;
import au.edu.sydney.cpa.erp.contact.Email;
import au.edu.sydney.cpa.erp.ordering.Client;

public class EmailHandler implements ContactChain {

    private ContactChain next;

    public EmailHandler(ContactChain next){
        this.next = next;
    }

    @Override
    public boolean sendInvoice(AuthToken token, Client client, String data) {
        String email = client.getEmailAddress();
        if (null != email) {
            Email.sendInvoice(token, client.getFName(), client.getLName(), data, email);
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
