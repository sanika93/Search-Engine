import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.HashSet;
import java.util.Set;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;

import edu.uci.ics.crawler4j.parser.ParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;

public class BinaryParser implements ParseData {

	
	  private static final String DEFAULT_ENCODING = "UTF-8";
	  private static final String DEFAULT_OUTPUT_FORMAT = "html";

	  private static final Parser AUTO_DETECT_PARSER = new AutoDetectParser();
	  private static final SAXTransformerFactory SAX_TRANSFORMER_FACTORY = (SAXTransformerFactory) SAXTransformerFactory.newInstance();

	  private final ParseContext context = new ParseContext();
	  private Set<WebURL> outgoingUrls = new HashSet<>();
	  private String html = null;

	  public BinaryParser() {
	    context.set(Parser.class, AUTO_DETECT_PARSER);
	  }

	  public void setBinaryContent(byte[] data) {
	    InputStream inputStream = new ByteArrayInputStream(data);
	    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

	    try {
	      TransformerHandler handler = getTransformerHandler(outputStream, DEFAULT_OUTPUT_FORMAT, DEFAULT_ENCODING);
	      AUTO_DETECT_PARSER.parse(inputStream, handler, new Metadata(), context);

	      // Hacking the following line to remove Tika's inserted DocType
	      String htmlContent = new String(outputStream.toByteArray(), DEFAULT_ENCODING).replace("http://www.w3.org/1999/xhtml", "");
	      setHtml(htmlContent);
	    } catch (Exception e) {
	      System.out.println("Error parsing file"+ e);
	    }
	  }

	  /**
	   * Returns a transformer handler that serializes incoming SAX events to
	   * XHTML or HTML (depending the given method) using the given output encoding.
	   *
	   * @param encoding output encoding, or <code>null</code> for the platform default
	   */
	  private static TransformerHandler getTransformerHandler(OutputStream out, String method, String encoding)
	        throws TransformerConfigurationException {

	    TransformerHandler transformerHandler = SAX_TRANSFORMER_FACTORY.newTransformerHandler();
	    Transformer transformer = transformerHandler.getTransformer();
	    transformer.setOutputProperty(OutputKeys.METHOD, method);
	    transformer.setOutputProperty(OutputKeys.INDENT, "yes");

	    if (encoding != null) {
	      transformer.setOutputProperty(OutputKeys.ENCODING, encoding);
	    }

	    transformerHandler.setResult(new StreamResult(new PrintStream(out)));
	    return transformerHandler;
	  }

	  /** @return Parsed binary content or null */
	  public String getHtml() {
	    return html;
	  }

	  public void setHtml(String html) {
	    this.html = html;
	  }

	  public Set<WebURL> getOutgoingUrls() {
	    return outgoingUrls;
	  }

	  public void setOutgoingUrls(Set<WebURL> outgoingUrls) {
	    this.outgoingUrls = outgoingUrls;
	  }

	  @Override
	  public String toString() {
	    return (html == null || html.isEmpty()) ? "No data parsed yet" : getHtml();
	  }
}
