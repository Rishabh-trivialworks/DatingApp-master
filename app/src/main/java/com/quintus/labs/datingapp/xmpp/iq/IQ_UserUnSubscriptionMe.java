package com.quintus.labs.datingapp.xmpp.iq;

import org.jivesoftware.smack.packet.IQ;
import org.jxmpp.jid.Jid;

/**
 * Created by Varun John on 27,June,2019
 */
public class IQ_UserUnSubscriptionMe extends IQ {

    private final static String childElementName = "unsubscribe";
    private final static String childElementNamespace = "urn:xmpp:mucsub:0";

    private int subscriberUserId;

    public IQ_UserUnSubscriptionMe(Jid groupId, int subscriberUserId) {
        super(childElementName, childElementNamespace);

        this.subscriberUserId = subscriberUserId;

        setTo(groupId);
        setType(Type.set);
    }

    @Override
    protected IQChildElementXmlStringBuilder getIQChildElementBuilder(IQChildElementXmlStringBuilder xml) {
        xml.rightAngleBracket();
        return xml;
    }
}
