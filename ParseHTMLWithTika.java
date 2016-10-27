import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipException;

import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.pdf.PDFParser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.ContentHandler;

public class ParseHTMLWithTika {

	public static void main(String[] args) throws Exception{
		// TODO Auto-generated method stub
		InputStream is = null;
		FileWriter fstream=null;
		BufferedWriter out=null;
		File dir = new File("C:/Users/sanika/workspace/MyCrawlerProject/downloads");
		  File[] directoryListing = dir.listFiles();
		  if (directoryListing != null) {
		    for (File child : directoryListing) {
		      // Do something with child
		    	String getNew=child.getName();
		    	if(getNew.contains("html")){
		    		try {

		   	         is = new FileInputStream(child.getAbsolutePath());
		   	         fstream=new FileWriter("F:/Linux/newDoc.txt",true);
		   	         out = new BufferedWriter(fstream);
		   		 StringBuilder sb= new StringBuilder();
		   	         ContentHandler contenthandler = new BodyContentHandler(1000000000);
		   	         Metadata metadata = new Metadata();
		   	         Parser parser = new AutoDetectParser();
		   	         parser.parse(is, contenthandler, metadata, new ParseContext());
		   	         String getContent=contenthandler.toString();
		   	         getContent=getContent.replaceAll("^\\s+|(?<=\\s)\\s+|\\s+$", "");
		   	         sb.append(getContent);
		   	         out.write(sb.toString());
		   	         
		   	    }
		   	    catch (Exception e) {
		   	      e.printStackTrace();
		   	    }
		   	    finally {
		   	        if (is != null)
		   				try {
		   					is.close();
		   				} catch (IOException e) {
		   					// TODO Auto-generated catch block
		   					e.printStackTrace();
		   				}
		   	        if(out!=null){
		   	        	try {
		   					out.close();
		   				} catch (IOException e) {
		   					// TODO Auto-generated catch block
		   					e.printStackTrace();
		   				}
		   	        }
		   	    }
		    	}
		    	else if(getNew.contains("pdf")){
		    		try {

			   	         is = new FileInputStream(child.getAbsolutePath());
			   	         fstream=new FileWriter("F:/Linux/trial.txt",true);
			   	         out = new BufferedWriter(fstream);
			   			 StringBuilder sb= new StringBuilder();
			   	         ContentHandler contenthandler = new BodyContentHandler(1000000000);
			   	         Metadata metadata = new Metadata();
			   	         PDFParser pdfparser = new PDFParser();
			   	         pdfparser.parse(is, contenthandler, metadata, new ParseContext());
			   	         
			   	         String getContent=contenthandler.toString();
			   	         getContent=getContent.replaceAll("^\\s+|(?<=\\s)\\s+|\\s+$", "");
			   	         sb.append(getContent);
			   	         out.write(sb.toString());
			   	         
			   	    }
		    		catch(ZipException e){
		    			continue;
		    		}
		    		catch(Exception e){
		    			e.printStackTrace();
		    		}
		    		
		    		
			   	    finally {
			   	        if (is != null)
			   				try {
			   					is.close();
			   				} catch (IOException e) {
			   					// TODO Auto-generated catch block
			   					e.printStackTrace();
			   				}
			   	        if(out!=null){
			   	        	try {
			   					out.close();
			   				} catch (IOException e) {
			   					// TODO Auto-generated catch block
			   					e.printStackTrace();
			   				}
			   	        }
			   	    }
		    		
		    	}
		    	else if(getNew.contains("doc") || getNew.contains("msword")){
		    		
		    		is = new FileInputStream(child.getAbsolutePath());
		    		System.out.print(getNew);
		    		FileReader freader=new FileReader(child.getAbsolutePath());
		    		BufferedReader bufferedReader = new BufferedReader(freader);
		    		fstream=new FileWriter("F:/Linux/trial.txt",true);
		   	        out = new BufferedWriter(fstream);
		   		StringBuilder sb= new StringBuilder();
		                String line;
				    while((line = bufferedReader.readLine()) != null) {
		                    sb.append(line);
		    	}
					String print=sb.toString();
					print=print.replaceAll("^\\s+|(?<=\\s)\\s+|\\s+$", "");
					out.write(sb.toString());
					bufferedReader.close();
		    }
		    }
		  } 
		
	    
	}

}
