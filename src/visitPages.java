import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import edu.uci.ics.crawler4j.url.WebURL;

public class visitPages {
	
	private final Lock _mutex = new ReentrantLock(true);
	public static int fs1=0;
	public static int fs2=0;
	public static int fs3=0;
	public static int fs4=0;
	public static int fs5=0;
	public static int i=0;
	
	public void visitPage(byte[] data,String contentType,String url,List<WebURL> outLinks){
	
		FileWriter newfstream=null;
		
		try{
			_mutex.lock();
			i++;
			
			_mutex.unlock();
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		String nameOfFile="";
		
			/*String Name=url.replace("https://","").replace("http://", "");
			
		String fileName=Name.replace("/", "_").replace("?", "");
			//String fileName=Name.replace("/", "_");
			 
*/		/*String hashValue=null;
		try {
	        MessageDigest digest = MessageDigest.getInstance("MD5");
	        byte[] hashedBytes = digest.digest(url.getBytes("UTF-8"));
	 
	        hashValue = new String(hashedBytes);
	    } catch (NoSuchAlgorithmException | UnsupportedEncodingException ex) {
	        try {
				throw new Exception(
				        "Could not generate hash from String", ex);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
		*/
		if(contentType.contains("html")){
			nameOfFile="C:/Users/sanika/workspace/MyCrawlerProject/downloads/"+"index"+i+".html";
		}else if(contentType.contains("htm")){
			nameOfFile="C:/Users/sanika/workspace/MyCrawlerProject/downloads/"+"index"+i+".htm";
		}else if(contentType.contains("pdf")){
			nameOfFile="C:/Users/sanika/workspace/MyCrawlerProject/downloads/"+"index"+i+".pdf";
		}else if(contentType.contains("msword")){
			nameOfFile="C:/Users/sanika/workspace/MyCrawlerProject/downloads/"+"index"+i+".msword";
		}else if(contentType.contains("docx")){
			nameOfFile="C:/Users/sanika/workspace/MyCrawlerProject/downloads/"+"index"+i+".docx";
		}else if(contentType.contains("doc")){
			nameOfFile="C:/Users/sanika/workspace/MyCrawlerProject/downloads/"+"index"+i+".doc";
		}
		
		FileOutputStream pw=null;
			
			try {
				pw=new FileOutputStream(nameOfFile);
				pw.write(data);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		   
		FileWriter hashFile=null;
		try {
			hashFile = new FileWriter("C:/Users/sanika/workspace/MyCrawlerProject/hash.csv",true);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		BufferedWriter out1 = new BufferedWriter(hashFile);
		 StringBuilder sb1 = new StringBuilder();
		 sb1.append(url);
	     sb1.append(',');
	     sb1.append(nameOfFile);
	     sb1.append('\n');
		
		
		
		 try {
				out1.write(sb1.toString());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		     
		     try {
				out1.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		   
		   try {
			newfstream=new FileWriter("C:/Users/sanika/workspace/MyCrawlerProject/visit.csv", true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		   double size=Math.floor((data.length/1024));
		   
		   
		   
			   try{
				   _mutex.lock();
				   if(size<1){
					   fs1++;
				   }else if(1<=size && size<10){
					   fs2++;
				   }else if(10<=size && size<100){
					   fs3++;
				   }else if(100<=size && size<1000){
					   fs4++;
				   }else{
					   fs5++;
				   }
				   
				   _mutex.unlock();
			   }
		   catch(Exception e){
			   e.printStackTrace();
		   }
		 
		   BufferedWriter out = new BufferedWriter(newfstream);
			 StringBuilder sb = new StringBuilder();
			 sb.append(url);
			 sb.append(',');
		     sb.append(size+"KB");
		     sb.append(',');
		     if(outLinks==null){
		    	 sb.append(0);
			 }else
			 {sb.append(outLinks.size());}
		     sb.append(',');
		     sb.append(contentType);
		     sb.append('\n');
		     
		     try {
				out.write(sb.toString());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		     
		     try {
				out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		   
		     if(outLinks!=null){
		     try{
		    	 _mutex.lock();
		    	 FileWriter fw=new FileWriter("C:/Users/sanika/workspace/MyCrawlerProject/pagerankdata.csv",true);
		    	 fw.append(url);
		    	 fw.append(',');
		    	 for(WebURL weburl: outLinks){
		    		 String getURL=weburl.getURL();
		    		 fw.append(getURL);
		    		 fw.append(',');
		    		 
		    	 }
		    	 fw.append('\n');
		    	 fw.close();
		     }catch(Exception e){
		    	 e.printStackTrace();
		     }
		}
	}
		
		
	}

	


