package com.quintus.labs.datingapp.xmpp.extensions;

import org.jivesoftware.smack.packet.ExtensionElement;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.StandardExtensionElement;
import org.jivesoftware.smack.util.XmlStringBuilder;

/**
 * Created by Varun John on 29,July,2019
 */
public class DisplayedExtension implements ExtensionElement {

    /**
     * displayed element.
     */
    public static final String ELEMENT = "displayed";
    public static final String NAMESPACE = "urn:xmpp:chat-markers:0";

    private final String id;

    public DisplayedExtension(String id) {
        this.id = id;
    }

    /**
     * Get the id.
     *
     * @return the id
     */
    public String getId() {
        return id;
    }

    @Override
    public String getElementName() {
        return ELEMENT;
    }

    @Override
    public String getNamespace() {
        return NAMESPACE;
    }

    @Override
    public CharSequence toXML(String enclosingNamespace) {
        XmlStringBuilder xml = new XmlStringBuilder(this);
        xml.attribute("id", id);
        xml.closeEmptyElement();
        return xml;
    }

    public static DisplayedExtension from(Message message) {
        ExtensionElement extension = message.getExtension(ELEMENT, NAMESPACE);

        if (extension != null) {
            try {
                return new DisplayedExtension(((StandardExtensionElement) extension).getAttributes().get("id"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }
}
