package Config;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class DefaultHandlerPerso extends org.xml.sax.helpers.DefaultHandler {
    private int PORT_CHAMBRE;
    private int PORT_ACTIVITE;
    private int PORT_PAY;
    private int PORT_ADMIN;
    private int PORT_ADMINS;
    private int PORT_URGENCE;
    private int PORT_CARD;
    private int PORT_BANK;
    private int PORT_GROUPE;

    private StringBuilder currentValue = new StringBuilder();


    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException
    {
        currentValue.setLength(0);
    }

    public void endElement(String uri, String localName, String qName) throws SAXException
    {
        if (qName.equalsIgnoreCase("PORT_CHAMBRE")) {
            PORT_CHAMBRE = Integer.parseInt(currentValue.toString());
        }
        if (qName.equalsIgnoreCase("PORT_ACTIVITE")) {
            PORT_ACTIVITE = Integer.parseInt(currentValue.toString());
        }
        if (qName.equalsIgnoreCase("PORT_PAY")) {
            PORT_PAY = Integer.parseInt(currentValue.toString());
        }
        if (qName.equalsIgnoreCase("PORT_ADMIN")) {
            PORT_ADMIN = Integer.parseInt(currentValue.toString());
        }
        if (qName.equalsIgnoreCase("PORT_ADMINS")) {
            PORT_ADMINS = Integer.parseInt(currentValue.toString());
        }
        if (qName.equalsIgnoreCase("PORT_URGENCE")) {
            PORT_URGENCE = Integer.parseInt(currentValue.toString());
        }
        if (qName.equalsIgnoreCase("PORT_CARD")) {
            PORT_CARD = Integer.parseInt(currentValue.toString());
        }
        if (qName.equalsIgnoreCase("PORT_BANK")) {
            PORT_BANK = Integer.parseInt(currentValue.toString());
        }
        if (qName.equalsIgnoreCase("PORT_GROUPE")) {
            PORT_GROUPE = Integer.parseInt(currentValue.toString());
        }
    }
    //reads the text value of the currently parsed element
    public void characters(char ch[], int start, int length) throws SAXException
    {
        currentValue.append(ch, start, length);
    }


    public int getPORT_CHAMBRE() {
        return PORT_CHAMBRE;
    }

    public int getPORT_ACTIVITE() {
        return PORT_ACTIVITE;
    }

    public int getPORT_PAY() {
        return PORT_PAY;
    }

    public int getPORT_ADMIN() {
        return PORT_ADMIN;
    }

    public int getPORT_ADMINS() {
        return PORT_ADMINS;
    }

    public int getPORT_URGENCE() {
        return PORT_URGENCE;
    }

    public int getPORT_CARD() {
        return PORT_CARD;
    }

    public int getPORT_BANK() {
        return PORT_BANK;
    }

    public int getPORT_GROUPE() {
        return PORT_GROUPE;
    }
}
