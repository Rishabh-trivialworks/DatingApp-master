package com.quintus.labs.datingapp.xmpp.iq;


import com.quintus.labs.datingapp.Utils.TempStorage;
import com.quintus.labs.datingapp.xmpp.utils.AppConstants;

import org.jivesoftware.smack.packet.IQ;
import org.jxmpp.jid.Jid;
import org.jxmpp.jid.impl.JidCreate;

/**
 * Created by Varun John on 27,June,2019
 */
public class IQ_ChatGroupDeleted extends IQ {

    private final static String childElementName = "query";
    private final static String childElementNamespace = "http://jabber.org/protocol/muc#owner";

    private Jid groupId;

    public IQ_ChatGroupDeleted(Jid groupId) {
        super(childElementName, childElementNamespace);

        this.groupId = groupId;

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

        xml.rightAngleBracket();

        xml.halfOpenElement("destroy");
        xml.attribute("jid", groupId.toString());
        xml.rightAngleBracket();

        xml.halfOpenElement("reason");
        xml.rightAngleBracket();
        xml.append("Owner choose to delete the group");
        xml.closeElement("reason");

        xml.closeElement("destroy");

        return xml;
    }
}

