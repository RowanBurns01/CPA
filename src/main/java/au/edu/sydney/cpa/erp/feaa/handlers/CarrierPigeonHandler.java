package au.edu.sydney.cpa.erp.feaa.handlers;

import au.edu.sydney.cpa.erp.auth.AuthToken;
import au.edu.sydney.cpa.erp.contact.CarrierPigeon;
import au.edu.sydney.cpa.erp.ordering.Client;

public class CarrierPigeonHandler implements ContactChain {

    private ContactChain next;

    /**
     * CarrierPigeonHandler handles the sendInvoice request using mode of CarrierPigeon.
     * @param next the next ContactChain handler, may be null.
     */
    public CarrierPigeonHandler(ContactChain next){
        this.next = next;
    }

    @Override
    public boolean sendInvoice(AuthToken token, Client client, String data) {
        String pigeonCoopID = client.getPigeonCoopID();
        if (null != pigeonCoopID) {
            CarrierPigeon.sendInvoice(token, client.getFName(), client.getLName(), data, pigeonCoopID);
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
