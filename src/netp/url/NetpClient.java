package netp.url;

import netp.component.NetpLogicUnit;
import netp.xml.NetpParser;
import netp.xmldocument.NetpDocument;


public class NetpClient implements HttpHost
{
    private NetpClientHeart m_heart;

    private String m_host;
    private String m_path;
    private int m_port;

    private int m_maxHttpClient = 10;
	
    private NetpLogicUnit m_logic;
    public NetpClient(String host, int port, String path, NetpLogicUnit logic)
    {
        m_host = host;
        m_port = port;
        m_path = path;
        m_logic = logic;
        m_heart = null;
    }
	
    public void startHeart(int itv) {
        if(m_heart!=null) m_heart.doStop();
        m_heart = new NetpClientHeart(this, itv);
        m_heart.start();
    }


    public void stopHeart()
    {
        if(m_heart != null) m_heart.doStop();
        m_heart = null;
    }


    public void sendRequest(NetpDocument reqDoc )
    {
        try
        {
            while ( HttpClient.getCount() > m_maxHttpClient )
            {
                Thread.sleep(100);
            }
        }
        catch ( InterruptedException e ) {}
        HttpClient m_httpClient;

        m_httpClient = new HttpClient(this, m_host, m_port, m_path);
        m_httpClient.add_par("document", reqDoc.toString());
        m_httpClient.start();
    }

    public void getRespond(String serDoc)
    {

        if(m_logic==null) return;
        String xmlStr;
        netDocument resDoc = new netDocument( serDoc );
        xmlStr = resDoc.getDoc();

        NetpParser parser= new NetpParser();

        try {
            parser.netpParse(xmlStr);
        }
        catch (Exception e) {
            m_logic.reciveWrongRespond(xmlStr);
        }

        m_logic.parseRespond(parser);
    }
    public void heartBeat(int val) {
        if(m_logic==null) return;
        m_logic.heartBeat(val);
    }
}

class NetpClientHeart extends Thread
{
    private NetpClient m_client;
    private boolean m_finish;
    private int m_interval;
    private int m_num;
    private boolean m_has_error;
    public NetpClientHeart( NetpClient client, int interval )
    {
        super();
        m_client = client;
        m_interval = interval*1000;
        m_num=0;
        m_finish = false;
        m_has_error = false;

    }

    public void run()
    {
        do {
            try 
            {
                m_client.heartBeat(m_num++);
                sleep(m_interval);
            }
            catch (InterruptedException e) 
            {
                m_has_error = true;
            }
        } while (!m_finish);
    }

    public void doStop()
    {
       m_finish = true;
    }
    public boolean hasError() {
        return m_has_error;
    }
}
