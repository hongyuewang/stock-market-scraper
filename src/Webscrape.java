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

        System.out.println("Largest companies by market cap — US Stock Market".toUpperCase());
        ArrayList<String> nameList = new ArrayList<>();
        ArrayList<String> symbolList = new ArrayList<>();
        ArrayList<String> lastList = new ArrayList<>();
        ArrayList<String> changePercentageList = new ArrayList<>();
        ArrayList<String> changeDollarsList = new ArrayList<>();
        ArrayList<String> volumeList = new ArrayList<>();
        ArrayList<String> capitalizationList = new ArrayList<>();
        ArrayList<String> ratioList = new ArrayList<>();
        ArrayList<String> earningsList = new ArrayList<>();

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
                Element changePercentage = row.select("td").get(2);
                changePercentageList.add(changePercentage.ownText());
                Element changeDollars = row.select("td").get(3);
                changeDollarsList.add(changeDollars.ownText());
                Element volume = row.select("td").get(5);
                volumeList.add(volume.ownText());
                Element capitalization = row.select("td").get(6);
                capitalizationList.add(capitalization.ownText());
                Element ratio = row.select("td").get(7);
                ratioList.add(ratio.ownText());
                Element earnings = row.select("td").get(8);
                earningsList.add(earnings.ownText());
            }


            for (int i = 0; i < nameList.size(); i++) {

                System.out.println("NAME: " + nameList.get(i) + " (" + symbolList.get(i) + ")\nLAST: " + lastList.get(i)
                        + "\nCHANGE (%): " + changePercentageList.get(i) + "\nCHANGE ($): " + changeDollarsList.get(i)
                        + "\nVOLUME: " + volumeList.get(i) + "\nMARKET CAPITALIZATION: " + capitalizationList.get(i)
                        + "\nPRICE TO EARNINGS RATIO (TTM): " + ratioList.get(i) + "\nEARNINGS PER SHARE (TTM): "
                        + earningsList.get(i) + "\n\n\n");
            }

            client.close();
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
