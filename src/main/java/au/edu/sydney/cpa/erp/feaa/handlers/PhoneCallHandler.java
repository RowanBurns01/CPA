package au.edu.sydney.cpa.erp.feaa.handlers;

import au.edu.sydney.cpa.erp.auth.AuthToken;
import au.edu.sydney.cpa.erp.contact.PhoneCall;
import au.edu.sydney.cpa.erp.ordering.Client;

public class PhoneCallHandler implements ContactChain {

    private ContactChain next;

    /**
     * PhoneCallHandler handles the sendInvoice request using mode of PhoneCall.
     * @param next the next ContactChain handler, may be null.
     */
    public PhoneCallHandler(ContactChain next){
        this.next = next;
    }

    @Override
    public boolean sendInvoice(AuthToken token, Client client,String data) {
        String phone = client.getPhoneNumber();
        if (null != phone) {
            PhoneCall.sendInvoice(token, client.getFName(), client.getLName(), data, phone);
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
