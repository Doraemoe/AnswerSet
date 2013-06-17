package netp.component;


import netp.xml.NetpParser;
import netp.xmldocument.NetpDocument;

public interface NetpLogicUnit
{
    public NetpDocument parseRequest(NetpParser parser);
    public void parseRespond(NetpParser parser);
    public void reciveWrongRespond(String str);
    public void heartBeat(int num);
    public String getTT();
}