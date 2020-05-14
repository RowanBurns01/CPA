package au.edu.sydney.cpa.erp.feaa.handlers;

import au.edu.sydney.cpa.erp.auth.AuthToken;
import au.edu.sydney.cpa.erp.contact.SMS;
import au.edu.sydney.cpa.erp.ordering.Client;

public class SMSHandler implements ContactChain {

    private ContactChain next;

    public SMSHandler(ContactChain next){
        this.next = next;
    }

    @Override
    public boolean sendInvoice(AuthToken token, Client client, String data) {
        String smsPhone = client.getPhoneNumber();
        if (smsPhone != null) {
            SMS.sendInvoice(token, client.getFName(), client.getLName(), data, smsPhone);
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
