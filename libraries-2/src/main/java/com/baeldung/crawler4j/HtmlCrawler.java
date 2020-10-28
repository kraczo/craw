package com.baeldung.crawler4j;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

public class HtmlCrawler extends WebCrawler {

//    private final static Pattern EXCLUSIONS = Pattern.compile(".*(\\.(css|js|xml|gif|jpg|png|mp3|mp4|zip|gz|pdf))$");
    private final static Pattern EXCLUSIONS = Pattern.compile(".*(\\.(css|js|xml|gif|png|mp3|mp4|zip|gz|pdf))$");
    private final static Pattern JPG = Pattern.compile(".*(\\.(jpg))$");
    private  Set<String> collectedProducts = new HashSet<>();

//    private final static Pattern EXCLUSIONS = Pattern.compile("");

    private CrawlerStatistics stats;
    Set<String> urlsToAdd;
    
    public HtmlCrawler(CrawlerStatistics stats, Set<String> urlsToAdd) {
        this.stats = stats;
        this.urlsToAdd = urlsToAdd;
    }


    @Override
    public boolean shouldVisit(Page referringPage, WebURL url) {
        String baseUrl = "https://www.hafele.pl/";
        String href = url.getURL();
        String referrer = referringPage.getWebURL().getURL();
        boolean shouldVisit = href.startsWith(baseUrl) || referrer.startsWith(baseUrl) || referringPage.isRedirect();


        String urlString = url.getURL().toLowerCase();
        return
                !EXCLUSIONS.matcher(urlString).matches()
//            && urlString.startsWith("https://www.hafele.pl/pl/");
//            && urlString.startsWith("https://www.hafele.pl/pl/products/okucia-drzwiowe-i-budowlane/elektroniczne-systemy-kontroli-dost-pu/904dfdc0074e92e7c80dac09879972a0/");
//            urlString.startsWith("https://www.hafele.pl/pl/product/zestaw-z-terminalem-ciennym-wt-120-dialock-w-wersji-merten-bez-p-ytki-centralnej-i-ramy-do-obszaru-wewn-trznego-tag-it-sup-tm-sup-iso/0000001100021fef000a0023/#SearchParameter=&@QueryTerm=*&Category=HU4KAOsCQrgAAAFtzMJkto8v&@P.FF.followSearch=9900&PageNumber=1&OriginalPageSize=12&PageSize=12&Position=2&OrigPos=94&ProductListSize=454&PDP=true");
//            && urlString.startsWith("https://www.hafele.pl/");
        && shouldVisit;
//                 && urlString.startsWith("https://www.hafele.pl/pl/products/okucia-meblowe-i-rozwi-zania-do-mieszkania/7a8602e71e00a9d1fe9fa8549fddc606".toLowerCase());
    }


    //prod-live/
    @Override
    public void visit(Page page) {
        String url = page.getWebURL().getURL();
        stats.incrementProcessedPageCount();
        AtomicInteger counter2 = new AtomicInteger();
//        Set<String> productsNumbers = Set.of(
//                "48.31.801",
//                "356.02.512",
//                "341.24.415.1",
//                "311.98.510",
//                "311.71.500",
//                "660.23.524",//found
//                "348.31.801",
//                "917.41.201",//found
//                "237.56.971",//found
//                "23756971"
//        );




        if (page.getParseData() instanceof HtmlParseData) {
            HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
            String title = htmlParseData.getTitle();
            String text = htmlParseData.getText();
            String html = htmlParseData.getHtml();
            Set<WebURL> links = htmlParseData.getOutgoingUrls();
            //shopItemImage
            stats.incrementTotalLinksCount(links.size());
            Document doc = Jsoup.parse(html);
            Element productNumber = doc.getElementsByClass("product-sku-title").first();
            Elements href = doc.select("a.shopItemImage");
            Element productTitle = doc.select("label.product-title").first();

            System.out.println("Still looking " + stats.getProcessedPageCount() +
                    " - total links -"  + stats.getTotalLinksCount() +
                    " - current page url " + url +
                    "Number of outgoing links: " + links.size());

            if(Objects.nonNull(href)) {
                for (Element e : href) {
                    String urlToAdd = StringUtils.substringBefore(e.attr("href"), "#SearchParameter");
                    urlsToAdd.add("https://www.hafele.pl" + urlToAdd);
                }
            }
            if(Objects.nonNull(productNumber)) {
                ProductsNumbers.productsNumbers.forEach(item -> {
                    final int[] counter = {1};
                    if (html.contains(item)) {
                        collectedProducts.add(productNumber.text());
                        System.out.println(" {");
                        System.out.println("        productNumber: " + productNumber.text());
                        System.out.println("        productTitle: " + productTitle.text());
                        System.out.println("    counter    " + counter2.getAndIncrement());
                        links.forEach(item2 -> {
                            String itemUrl = item2.getURL();
                            if(itemUrl.contains(item) &&
                                    JPG.matcher(itemUrl).matches()
                                    && itemUrl.contains("huge") //get only biggest

                            ) {
                                System.out.println("        zdjecie nr  = " + counter[0]++);
                                System.out.println("        " + itemUrl);

                            }
                        });
                        System.out.println(" }");
                    }
                });


            }

        }
        System.out.println("Collected size " + collectedProducts.size());
        System.out.println("Collected \n{");
        collectedProducts.forEach(System.out::println);
        System.out.println("}");
    }

}
