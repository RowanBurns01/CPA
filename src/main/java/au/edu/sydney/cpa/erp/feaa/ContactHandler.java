package au.edu.sydney.cpa.erp.feaa;

import au.edu.sydney.cpa.erp.auth.AuthToken;
import au.edu.sydney.cpa.erp.contact.*;
import au.edu.sydney.cpa.erp.ordering.Client;

import java.util.Arrays;
import java.util.List;

public class ContactHandler {
    public static boolean sendInvoice(AuthToken token, Client client, List<ContactMethod> priority, String data) {
        for (ContactMethod method : priority) {
            switch (method) {
                case SMS:
                    String smsPhone = client.getPhoneNumber();
                    if (null != smsPhone) {
                        SMS.sendInvoice(token, client.getFName(), client.getLName(), data, smsPhone);
                        return true;
                    }
                    break;
                case MAIL:
                    String address = client.getAddress();
                    String suburb = client.getSuburb();
                    String state = client.getState();
                    String postcode = client.getPostCode();
                    if (null != address && null != suburb &&
                        null != state && null != postcode) {
                        Mail.sendInvoice(token, client.getFName(), client.getLName(), data, address, suburb, state, postcode);
                        return true;
                    }
                    break;
                case EMAIL:
                    String email = client.getEmailAddress();
                    if (null != email) {
                        Email.sendInvoice(token, client.getFName(), client.getLName(), data, email);
                        return true;
                    }
                    break;
                case PHONECALL:
                    String phone = client.getPhoneNumber();
                    if (null != phone) {
                        PhoneCall.sendInvoice(token, client.getFName(), client.getLName(), data, phone);
                        return true;
                    }
                    break;
                case INTERNAL_ACCOUNTING:
                    String internalAccounting = client.getInternalAccounting();
                    String businessName = client.getBusinessName();
                    if (null != internalAccounting && null != businessName) {
                        InternalAccounting.sendInvoice(token, client.getFName(), client.getLName(), data, internalAccounting,businessName);
                        return true;
                    }
                    break;
                case CARRIER_PIGEON:
                    String pigeonCoopID = client.getPigeonCoopID();
                    if (null != pigeonCoopID) {
                        CarrierPigeon.sendInvoice(token, client.getFName(), client.getLName(), data, pigeonCoopID);
                        return true;
                    }
                    break;
                default:
                    return false;
            }
        }
        return false;
    }
    public static List<String> getKnownMethods() {
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
