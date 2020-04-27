package com.quintus.labs.datingapp.xmpp.iq;

import org.jivesoftware.smack.packet.IQ;

/**
 * Created by Varun John on 27,June,2019
 */
public class IQ_QueryMAM extends IQ {

    private final static String childElementName = "query";
    private final static String childElementNamespace = "urn:xmpp:mam:2";

    private int subscriberUserId;
    private String timeStamp;

    public IQ_QueryMAM(String timeStamp) {
        super(childElementName, childElementNamespace);

        this.timeStamp = timeStamp;

        setType(Type.set);
    }

    @Override
    protected IQChildElementXmlStringBuilder getIQChildElementBuilder(IQChildElementXmlStringBuilder xml) {

        xml.rightAngleBracket();

        xml.halfOpenElement("x");
        xml.attribute("xmlns", "jabber:x:data");
        xml.attribute("type", "submit");
        xml.rightAngleBracket();


        xml.halfOpenElement("field");
        xml.attribute("var", "start");
        xml.rightAngleBracket();


        xml.halfOpenElement("value");
        xml.rightAngleBracket();
        xml.append(timeStamp);
        xml.closeElement("value");

        xml.closeElement("field");

        xml.closeElement("x");

        return xml;
    }
}
