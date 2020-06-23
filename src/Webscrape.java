package webscrape;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.logging.Level;

public class Webscrape {
    public static void main(String[] args) {
        LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog");

        java.util.logging.Logger.getLogger("com.gargoylesoftware").setLevel(Level.OFF);
        java.util.logging.Logger.getLogger("org.apache.commons.httpclient").setLevel(Level.OFF);

        WebClient client = new WebClient(BrowserVersion.CHROME);
        client.getOptions().setJavaScriptEnabled(true);
        client.getOptions().setThrowExceptionOnScriptError(false);
        client.getOptions().setThrowExceptionOnFailingStatusCode(false);
        client.getOptions().setThrowExceptionOnScriptError(false);

        final String url = "https://www.wsj.com/market-data/stocks/us/indexes";
        System.out.println("Largest companies by market cap — US Stock Market".toUpperCase());
        ArrayList<String> nameList = new ArrayList<>();
        ArrayList<String> symbolList = new ArrayList<>();
        ArrayList<String> lastList = new ArrayList<>();

        try {
            HtmlPage myPage = (HtmlPage) client.getPage("https://www.tradingview.com/markets/stocks-usa/market-movers-large-cap");
            final Document pageXml = Jsoup.parse(myPage.asXml());
            Elements rows = pageXml.select("tr");

            for (Element name : pageXml.select(".tv-screener__description")) {
                nameList.add(name.ownText());
            }

            int symbolCount = 0;
            for (Element symbol : pageXml.select(".tv-screener__symbol")) {
                if (symbolCount % 2 == 0) {
                    symbolList.add(symbol.ownText());
                }
                symbolCount++;
            }

            for (Element row : rows.subList( 1, rows.size() ) ) {
                Element last = row.select("td").get(1);
                lastList.add(last.ownText());
            }

            for (int i = 0; i < nameList.size(); i++) {

                System.out.println("NAME: " + nameList.get(i) + " (" + symbolList.get(i) + ")\nLAST: " + lastList.get(i) + "\n\n\n");
            }

            client.close();
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
