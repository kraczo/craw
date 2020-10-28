package com.baeldung.crawler4j;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

public class HtmlCrawlerController {

    public static void main(String[] args) throws Exception {
        File crawlStorage = new File("src/test/resources/crawler4j");
        File crawlStorage2 = new File("src/test/resources/toadd");
        CrawlConfig config = new CrawlConfig();
        config.setCrawlStorageFolder(crawlStorage.getAbsolutePath());
        config.setMaxDepthOfCrawling(1000);
        //?
        config.setIncludeHttpsPages(true);
//        config.setMaxPagesToFetch(10000);

        int numCrawlers = 1000;

        PageFetcher pageFetcher = new PageFetcher(config);
        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
        robotstxtConfig.setEnabled(false);
        CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);

//        controller.addSeed("https://www.hafele.pl/pl/products/");
//        controller.addSeed("https://www.hafele.pl/pl/product/zestaw-z-terminalem-ciennym-wt-120-dialock-w-wersji-merten-bez-p-ytki-centralnej-i-ramy-do-obszaru-wewn-trznego-tag-it-sup-tm-sup-iso/0000001100021fef000a0023/#SearchParameter=&@QueryTerm=*&Category=HU4KAOsCQrgAAAFtzMJkto8v&@P.FF.followSearch=9900&PageNumber=1&OriginalPageSize=12&PageSize=12&Position=2&OrigPos=94&ProductListSize=454&PDP=true");
//        controller.addSeed("https://www.hafele.pl/pl/");
            controller.addSeed("https://www.hafele.pl/pl/product/-cznik-jednocz-ciowy-haefele-confirmat-eb-sto-kowy-do-otworu-o-5-mm-sw4/000000000000ff4e00010023/#SearchParameter=&@QueryTerm=*&Category=0A8KAOsCwgUAAAFtJMpkto8v&PageNumber=1&OriginalPageSize=12&PageSize=12&Position=12&OrigPos=12&ProductListSize=17.167");

//        controller.addSeed("https://www.hafele.pl/");
//        controller.addSeed("https://www.hafele.pl/pl/product");
//        controller.addSeed("https://www.hafele.pl/pl/products");
//        for(String s :SeedsToScan.seedsToScan ){
//            controller.addSeed(s);
//        }

//        controller.addSeed("https://www.hafele.pl/pl/product/zestaw-z-terminalem-ciennym-wt-120-dialock-w-wersji-merten-bez-p-ytki-centralnej-i-ramy-do-obszaru-wewn-trznego-tag-it-sup-tm-sup-iso/0000001100021fef000a0023/#SearchParameter=&@QueryTerm=*&Category=HU4KAOsCQrgAAAFtzMJkto8v&@P.FF.followSearch=9900&PageNumber=1&OriginalPageSize=12&PageSize=12&Position=2&OrigPos=94&ProductListSize=454");
//        controller.addSeed("https://www.hafele.pl/");
//        controller.addSeed("https://www.hafele.pl/pl/product/");
//        controller.addSeed("https://www.hafele.pl/pl/product/-/");
//        controller.addSeed("https://www.hafele.pl/pl/products/");
//        controller.addSeed("https://www.hafele.pl/pl/product/zestaw-z-terminalem-ciennym-wt-120-dialock-w-wersji-merten-bez-p-ytki-centralnej-i-ramy-do-obszaru-wewn-trznego-tag-it-sup-tm-sup-iso/0000001100021fef000a0023/#SearchParameter=&@QueryTerm=*&Category=HU4KAOsCQrgAAAFtzMJkto8v&@P.FF.followSearch=9900&PageNumber=1&OriginalPageSize=12&PageSize=12&Position=2&OrigPos=94&ProductListSize=454&PDP=true");
//        controller.addSeed("https://www.hafele.pl/pl/products/okucia-drzwiowe-i-budowlane/elektroniczne-systemy-kontroli-dost-pu/904dfdc0074e92e7c80dac09879972a0/");




        CrawlerStatistics stats = new CrawlerStatistics();
        Set<String> urlsToAdd = new HashSet<>();
        CrawlController.WebCrawlerFactory<HtmlCrawler> factory = () -> new HtmlCrawler(stats, urlsToAdd);

        controller.start(factory, numCrawlers);
        System.out.printf("Crawled %d pages %n", stats.getProcessedPageCount());
        System.out.printf("Total Number of outbound links = %d %n", stats.getTotalLinksCount());
        System.out.printf("Urls to add");
        urlsToAdd.forEach(System.out::println);
    }

}
