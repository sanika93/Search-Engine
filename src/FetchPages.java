import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class FetchPages {
	
	public void fetchPages(String url,int statusCode){
		
		FileWriter fstream=null;
		 try {
			 fstream= new FileWriter("C:/Users/sanika/workspace/MyCrawlerProject/fetch.csv", true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		 BufferedWriter out = new BufferedWriter(fstream);
		 StringBuilder sb = new StringBuilder();
		 sb.append(url);
	     sb.append(',');
	     sb.append(statusCode);
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
		
		
	}

	

}
