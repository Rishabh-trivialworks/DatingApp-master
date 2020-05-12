package com.quintus.labs.datingapp.xmpp.iq;


import com.quintus.labs.datingapp.Utils.TempStorage;
import com.quintus.labs.datingapp.xmpp.utils.AppConstants;

import org.jivesoftware.smack.packet.IQ;
import org.jxmpp.jid.impl.JidCreate;

/**
 * Created by Varun John on 21,August,2019
 */
public class IQ_CarbonEnable extends IQ {

    private final static String childElementName = "iq";
    private final static String childElementNamespace = "jabber:client";

    public IQ_CarbonEnable() {
        super(childElementName, childElementNamespace);

        setType(Type.set);

        try {
            setFrom(JidCreate.bareFrom(AppConstants.getJID(String.valueOf(TempStorage.getUser().getId()))));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected IQChildElementXmlStringBuilder getIQChildElementBuilder(IQChildElementXmlStringBuilder xml) {

        xml.rightAngleBracket();

        xml.halfOpenElement("enable");
        xml.attribute("xmlns", "urn:xmpp:carbons:2");
        xml.rightAngleBracket();
        xml.closeElement("enable");

        return xml;
    }
}