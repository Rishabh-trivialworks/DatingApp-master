package com.quintus.labs.datingapp.xmpp.iq;



import com.quintus.labs.datingapp.Utils.TempStorage;
import com.quintus.labs.datingapp.xmpp.utils.AppConstants;

import org.jivesoftware.smack.packet.IQ;
import org.jxmpp.jid.Jid;
import org.jxmpp.jid.impl.JidCreate;

/**
 * Created by Varun John on 27,June,2019
 */
public class IQ_UserSubscription extends IQ {

    private final static String childElementName = "subscribe";
    private final static String childElementNamespace = "urn:xmpp:mucsub:0";

    private int subscriberUserId;

    public IQ_UserSubscription(Jid groupId, int subscriberUserId) {
        super(childElementName, childElementNamespace);

        this.subscriberUserId = subscriberUserId;

        setTo(groupId);
        setType(Type.set);

        try {
            setFrom(JidCreate.bareFrom(AppConstants.getJID(String.valueOf(TempStorage.getUser().getId()))));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected IQChildElementXmlStringBuilder getIQChildElementBuilder(IQChildElementXmlStringBuilder xml) {

        xml.attribute("jid", AppConstants.getJID(String.valueOf(subscriberUserId)));
        xml.attribute("nick", String.valueOf(subscriberUserId));
        xml.rightAngleBracket();

        xml.halfOpenElement("event");
        xml.attribute("node", "urn:xmpp:mucsub:nodes:messages");
        xml.rightAngleBracket();
        xml.closeElement("event");

        xml.halfOpenElement("event");
        xml.attribute("node", "urn:xmpp:mucsub:nodes:affiliations");
        xml.rightAngleBracket();
        xml.closeElement("event");

        xml.halfOpenElement("event");
        xml.attribute("node", "urn:xmpp:mucsub:nodes:subscribers");
        xml.rightAngleBracket();
        xml.closeElement("event");

        return xml;
    }
}
