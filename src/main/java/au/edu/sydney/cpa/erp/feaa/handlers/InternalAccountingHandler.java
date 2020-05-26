package au.edu.sydney.cpa.erp.feaa.handlers;

import au.edu.sydney.cpa.erp.auth.AuthToken;
import au.edu.sydney.cpa.erp.contact.InternalAccounting;
import au.edu.sydney.cpa.erp.ordering.Client;

public class InternalAccountingHandler implements ContactChain {

    private ContactChain next;

    /**
     * InternalAccountingHandler handles the sendInvoice request using mode of InternalAccounting.
     * @param next the next ContactChain handler, may be null.
     */
    public InternalAccountingHandler(ContactChain next){
        this.next = next;
    }

    @Override
    public boolean sendInvoice(AuthToken token, Client client, String data) {
        String internalAccounting = client.getInternalAccounting();
        String businessName = client.getBusinessName();
        if (null != internalAccounting && null != businessName) {
            InternalAccounting.sendInvoice(token, client.getFName(), client.getLName(), data, internalAccounting,businessName);
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
