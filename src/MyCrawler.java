
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

public class MyCrawler extends WebCrawler {

			 /**
			 * This method receives two parameters. The first parameter is the page
			 * in which we have discovered this new url and the second parameter is
			 * the new url. You should implement this function to specify whether
			 * the given url should be crawled or not (based on your crawling logic).
			 * In this example, we are instructing the crawler to ignore urls that
			 * have css, js, git, ... extensions and to only accept urls that start
			 * with "http://www.viterbi.usc.edu/". In this case, we didn't need the
			 * referringPage parameter to make the decision.
			 */
	
			public static int fetchesAttempted=0;
			public static int fetchesSucceeded=0;
			public static int fetchesFailed=0;
			public static int fetchesAborted=0;
			private final Lock _mutex = new ReentrantLock(true);
			public static HashSet<String> uniqueURLS=new HashSet<String>();
			public static Map<Integer,Integer> codes=new HashMap<Integer,Integer>();
			public static Map<String,Integer> contentTypes=new HashMap<String,Integer>();
			public static HashSet<String> urlSchool=new HashSet<String>();
			public static HashSet<String> urlWithinSchool=new HashSet<String>();
			public static HashSet<String> urlOutsideSchool=new HashSet<String>();
	
			 @Override
			 public boolean shouldVisit(WebURL url) {
				 String href = url.getURL().toLowerCase();
				 
				 try {
					FileWriter fw=new FileWriter("C:/Users/sanika/workspace/MyCrawlerProject/urls.csv",true);
					fw.append(href);
					fw.append(",");
						
						try{
							_mutex.lock();
							String subDomain=url.getSubDomain();
							String domain=url.getDomain();
							if(!uniqueURLS.contains(href)){
								uniqueURLS.add(href);
							}
							if(subDomain.contains("sowkweb")){
								fw.append("OK");
								fw.append('\n');
								urlSchool.add(href);
							}else if(!subDomain.contains("sowkweb") && domain.contains("usc.edu")){
								fw.append("USC");
								fw.append('\n');
								urlWithinSchool.add(href);
							}else{
								fw.append("outUSC");
								fw.append('\n');
								urlOutsideSchool.add(href);
								
							}
						
						_mutex.unlock();
					}catch(Exception e){
						e.printStackTrace();
					}
					
					
					fw.close();
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			 
				 return (href.startsWith("https://sowkweb.usc.edu/") || href.startsWith("http://sowkweb.usc.edu/"));
		
			 }
			 
			 @Override
			 public void visit(Page page) {
				 String url = page.getWebURL().getURL();
				 System.out.println("URL: " + url);
				 byte[] getContent;
				 String contentTypeNew=page.getContentType();
				 System.out.println(contentTypeNew);
				 try
				 {
					 _mutex.lock();
					  if(contentTypes.get(contentTypeNew)!=null){					 
						 contentTypes.put(contentTypeNew, contentTypes.get(contentTypeNew)+1);
					 }else{
						 contentTypes.put(contentTypeNew, 1);
					 }
					 _mutex.unlock();
				 }
				 catch(Exception e)
				 {
					 e.printStackTrace();
				 }
				 List<WebURL> links=null;
				 if(page.getParseData() instanceof HtmlParseData){
					 HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
				     links = htmlParseData.getOutgoingUrls();
				 }
				 if(links!=null){System.out.println(links.size());}
				 
			    
			     try{
			    	 _mutex.lock();
			    	 FileWriter fw=new FileWriter("C:/Users/sanika/workspace/MyCrawlerProject/urls.csv",true);
			    	 fw.append(url);
			    	 fw.append(',');
			    	 String sd=page.getWebURL().getSubDomain();
			    	 String d=page.getWebURL().getDomain();
			    	 if(sd.contains("sowkweb")){
							fw.append("OK");
							fw.append('\n');
							
						}else if(!sd.contains("sowkweb") && d.contains("usc.edu")){
							fw.append("USC");
							fw.append('\n');
							
						}else{
							fw.append("outUSC");
							fw.append('\n');
						}
			    	 if(links!=null){
			    	 for(WebURL weburl: links){
			    		 String getURL=weburl.getURL();
			    		
			    		 fw.append(getURL);
			    		 fw.append(',');
			    		 String subDomain=weburl.getSubDomain();
					     String domain=weburl.getSubDomain();
					     
					     if(subDomain.contains("sowkweb")){
								fw.append("OK");
								fw.append('\n');
								
							}else if(!subDomain.contains("sowkweb") && domain.contains("usc.edu")){
								fw.append("USC");
								fw.append('\n');
								
							}else{
								fw.append("outUSC");
								fw.append('\n');
								
								
							}
			    		 
			    	 }
			    	 }
			    	 fw.close();
			     }catch(Exception e){
			    	 e.printStackTrace();
			     }
				
				
			     String contentType=page.getContentType();
			     
				 if(contentType.contains("html") || contentType.contains("pdf")|| contentType.contains("htm") || contentType.contains("doc") || contentType.contains("docx") || contentType.contains("msword")){
					 getContent=page.getContentData();
					 visitPages visitNew=new visitPages();
					 visitNew.visitPage(getContent,contentType,url,links);
				 }
				 
				 
				 }
				 
			 protected void handlePageStatusCode(WebURL webUrl, int statusCode, String statusDescription){
				 
				 //Calculate fetches Attempted;
				 //Calculate fetches Succeeded 200-299;
				 //Calculate 300-399 as aborted;
				 //Calculate everything else as failed;
				 
				
				 
				 try{
					 _mutex.lock();
					 fetchesAttempted++;
					 
					 if(statusCode>=200 && statusCode<=299){
						 fetchesSucceeded++;
					 }
					 
					 else if(statusCode>=300 && statusCode<=399){
						 fetchesAborted++;
					 }else 
					 {
						 fetchesFailed++;
					 }
					 
					 if(codes.containsKey(statusCode)){
						 codes.put(statusCode, codes.get(statusCode)+1);
					 }else{
						
						 codes.put(statusCode, 1);
					 }
					 _mutex.unlock();
					
				 }catch(Exception e){
					 e.printStackTrace();
				 }
				
				 String url=webUrl.getURL();
			     FetchPages fetch=new FetchPages();
			     fetch.fetchPages(url, statusCode);
				 System.out.println("URL: " + url);
				 System.out.println(url);
				 System.out.println("Status code: " +statusCode);
				 
			}
				 
		 
	
}

			
