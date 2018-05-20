import java.net.*;
import java.util.*;
import java.io.OutputStreamWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import org.json.JSONObject;


public class request {

    public static void main(String args[]) {
        try {
            List<String> cookie = new request().sendJSONDataGet();
            new request().sendJSONData(cookie);
        } catch (Exception E) {
            System.out.println("Exception Occured. " + E.getMessage());
        }
    }


    public String sendJSONData(List<String> cookies) throws Exception {

        URL url = new URL("https://lb.api-sandbox.ea.openprocurement.org/api/2.5/auctions");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        Keys key = new Keys();
        conn.setRequestProperty ("Authorization", "Basic " + key.key(1));
        conn.setRequestProperty( "Content-type", "application/json");
        conn.setRequestProperty( "Accept", "*/*" );

//        System.out.println(cookies);
        for (String cookie : cookies) {
            conn.addRequestProperty("Cookie", cookie.split(",", 10)[0]);
        }

        OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());

        DataForAuctionCDB1 jsons = new DataForAuctionCDB1();
        JSONObject json = new JSONObject(jsons.generate_auction_json("dgfInsider", 2, 1440, 10, "(mode:no-auction)"));
        String myjson = json.toString();
        writer.write(myjson);
        writer.flush();

        String a = "";
        String line;
        BufferedReader reader = new BufferedReader(new
                InputStreamReader(conn.getInputStream()));
        while ((line = reader.readLine()) != null) {
//            System.out.println(line);
            a =line;
        }
        JSONObject r_json = new JSONObject(a);
        System.out.println(a);
        System.out.println(r_json);
//        System.out.println(r_json.getJSONObject("access"));
        writer.close();
        reader.close();
        return "";
    }
    public List<String> sendJSONDataGet() throws Exception {

        URL url = new URL("https://lb.api-sandbox.ea.openprocurement.org/api/2.5/auctions");
        URLConnection conn = url.openConnection();

        List<String> cookies = conn.getHeaderFields().get("Set-Cookie");

        return cookies;
    }
}
