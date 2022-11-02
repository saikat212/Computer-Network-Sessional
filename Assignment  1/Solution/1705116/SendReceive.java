public class SendReceive {

    NetworkConnection nc;
    Data data;
    Object obj;
    public SendReceive(NetworkConnection networkConnection)
    {
        nc=networkConnection;
        data = new Data();
    }

    public  void SendValue(long value)
    {
       data.sizeValue=value;
        try
        {
            nc.write(data.clone());
        } catch (Exception ex)
        {
            System.out.println("Sending failed");
        }
    }

    public  void SendMessage(String msg)
    {
        //data.message=msg;
        try
        {
            //nc.write(data.clone());
            nc.write(msg);
        } catch (Exception ex)
        {
            System.out.println("Sending failed");
        }
    }
    public  void SendMessageAndValue(String msg,long value)
    {
        data.message=msg;
        data.sizeValue=value;
        try
        {
            nc.write(data.clone());
        } catch (Exception ex)
        {
            System.out.println("Sending failed");
        }
    }

    public  String ReceiveMessage()
    {
        obj =nc.read();
        //data = (Data)obj;

        return (String) obj;
    }

    public long ReceiveValue()
    {
        obj =nc.read();
        data = (Data)obj;
        System.out.println(obj.toString());
        System.out.println(data.toString());
        if(!(data.equals(null)))
        {
            return  data.sizeValue;
        }
        else
        {
            System.out.println("Value by ReceivedValue is NuLL");
        }

        return 0;
    }

}
