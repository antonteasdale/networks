import java.net.*;
//this class
public class Coursework1 {

    public static void main(String[] args) {
        Coursework1 instance = new Coursework1();
        if (args.length == 1) {
            instance.oneArg(args[0]);
        } else if (args.length == 2) {
            instance.twoArg(args[0],args[1]);
        }
    }


    public void oneArg(String name) {
        try {
            InetAddress inet = InetAddress.getByName(name);
            //prints host name
            System.out.println("Hostname = " + inet.getHostName());
            //prints IP address
            System.out.println("IP Address = " + inet.getHostAddress());

            //prints IP version
            if (inet instanceof Inet4Address) {
                System.out.println("IP Type = IPv4");
            } else {
                System.out.println("IP Type = IPv6");
            }

        } catch (UnknownHostException e) {
            System.out.println("Unknown host name.");
        }
    }

    public void twoArg(String name1, String name2) {
        try{
            InetAddress inet1 = InetAddress.getByName(name1);
            InetAddress inet2 = InetAddress.getByName(name2);

            if (inet1 instanceof Inet4Address && inet2 instanceof Inet4Address) {
                String address1 = inet1.getHostAddress();
                String address2 = inet2.getHostAddress();

                String[] tokens1 = address1.split("\\.");
                String[] tokens2 = address2.split("\\.");


                //this boolean will stay true until two tokens are different and then it'll switch
                boolean stillEqual = true;
                for (int i = 0; i < 4; i++) {
                    //if we've already found a difference between IP addresses previously
                    //or we just discover one we print asterisks
                    if (stillEqual == false || tokens1[i].compareTo(tokens2[i]) != 0){
                        stillEqual=false;
                        System.out.print("***");
                    } else {
                        System.out.print(tokens1[i]);
                    }
                    if (i<3)
                        System.out.print(".");
                    else
                        System.out.println("\n");
                }
            }
            else{
                System.out.println("One or more addresses are invalid.");
            }
        } catch(UnknownHostException e) {
            System.out.println("Unknown host name.");
        }

    }
}
