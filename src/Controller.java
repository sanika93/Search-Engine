


import java.util.Map;


import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

public class Controller {
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		String crawlStorageFolder = "/data/crawl";
		 int numberOfCrawlers = 7;
		 int maxDepthOfCrawling=2;
		 int maxPagesToFetch=500;
		 int politenessDelay=500;
		 
		 CrawlConfig config = new CrawlConfig();
		 config.setCrawlStorageFolder(crawlStorageFolder);
		 config.setMaxDepthOfCrawling(maxDepthOfCrawling);
		 config.setMaxPagesToFetch(maxPagesToFetch);
		 config.setIncludeBinaryContentInCrawling(true);
		 config.setPolitenessDelay(politenessDelay);
		 config.setIncludeHttpsPages(true);
		
		 /*
		 * Instantiate the controller for this crawl.
		 */
		 PageFetcher pageFetcher = new PageFetcher(config);
		 RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
		 RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
		 CrawlController controller = null;
		try {
			controller = new CrawlController(config, pageFetcher, robotstxtServer);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 /*
		 * For each crawl, you need to add some seed urls. These are the first
		 * URLs that are fetched and then the crawler starts following links
		 * which are found in these pages
		 */
		String url="https://sowkweb.usc.edu/";
		
		
		 controller.addSeed(url);
		 controller.start(MyCrawler.class, numberOfCrawlers);
		 System.out.println("Fetch Attempted"+MyCrawler.fetchesAttempted);
		 System.out.println("Fetch succeeded"+MyCrawler.fetchesSucceeded);
		 System.out.println("Fetch aborted"+MyCrawler.fetchesAborted);
		 System.out.println("Fetch failed"+MyCrawler.fetchesFailed);
		 System.out.println("No urlSchool"+MyCrawler.urlSchool.size());
		 System.out.println("No urlWithinSchool"+MyCrawler.urlWithinSchool.size());
		 System.out.println("No urlOutsideSchool"+MyCrawler.urlOutsideSchool.size());
		 System.out.println("No uniqueurl"+MyCrawler.uniqueURLS.size());
		 
		 for(Map.Entry<Integer, Integer> status : MyCrawler.codes.entrySet()){
			 int code=status.getKey();
			 int val=status.getValue();
			 System.out.println(code + ":" + val);
		}
		 
		System.out.println(visitPages.fs1);
		System.out.println(visitPages.fs2); 
		System.out.println(visitPages.fs3); 
		System.out.println(visitPages.fs4); 
		System.out.println(visitPages.fs5);
		
		for(String key:MyCrawler.contentTypes.keySet())
		{
			System.out.println("Key is: "+key+" Value: "+MyCrawler.contentTypes.get(key));
		}
//		for(Map.Entry<String, Integer> status : MyCrawler.contentTypes.entrySet()){
//			 String code=status.getKey();
//			 int val=status.getValue();
//			 System.out.println(code + ":" + val);
//			 
//		}
		 
	}

}
