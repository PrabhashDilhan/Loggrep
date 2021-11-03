import java.io.*;
import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Test {

    public static void main(String[] args){

        File myObj = new File("/Users/apple/Documents/logs/greplog.log");
        //File fileout = new File("/Users/apple/Documents/support/143_TIETOOEMSUB-231/logd/logout.log");

        String[] cid = new String[17000];

        int cunt = 0;
        String file;

        try {
            //file = readFromInputStream(new FileInputStream(myObj));
            try (BufferedReader br
                         = new BufferedReader(new InputStreamReader(new FileInputStream(myObj)))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String correlationid = line.substring((line.indexOf("correlation_id")) + 16, (line.indexOf("correlation_id")) + 53);
                    //System.out.println(correlationid);
                    cid[cunt] = correlationid;
                    cunt++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Set<String> temp = new HashSet<String>(Arrays.asList(cid));
        String[] uq = temp.toArray(new String[temp.size()]);
        /*PrintStream stream = null;
        try {
            stream = new PrintStream(fileout);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        System.setOut(stream);*/

        for (int i=1; i<uq.length; i++){
            String cd = uq[i];
            try (BufferedReader br
                         = new BufferedReader(new InputStreamReader(new FileInputStream(myObj)))) {
                String line;
                String first="";
                String sec="";
                while ((line = br.readLine()) != null) {
                    if(line.contains(cd)){
                        if(line.contains("Before = Second call")){
                            first = line.substring(0, 23);
                        }
                        if(line.contains("After = Second call")){
                            sec = line.substring(0, 23);
                        }
                    }
                }
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS", Locale.ENGLISH);
                if(first!="" && sec!="") {
                    Date f = sdf.parse(first);
                    Date s = sdf.parse(sec);

                    if ((s.getTime() - f.getTime()) > 5000) {
                        System.out.println("correlation_id : " + cd);
                        System.out.println("Time difference between Before = Second call log and the After = Second call log in milli seconds");
                        System.out.println((s.getTime() - f.getTime()));
                    }
                }else {
                    System.out.println("correlation_id" + cd);
                    System.out.println("Unable to find the all the log entries");
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

    }

}
