package pdf;


import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.apache.pdfbox.pdmodel.PDDocument;

public class Test {

  public static void main(String[] args) {
    //File input = new File("/Users/neo/Downloads/2012.desember.tdt4120.losn.no.pdf");
    //File input = new File("/Users/neo/Downloads/V2011bm.pdf");
    //File input = new File("/Users/neo/Downloads/KTN-2012V.pdf");
    //File input = new File("/Users/neo/Downloads/TDT4145_2015.pdf");
    File input = new File("/Users/neo/Downloads/KJ1042 Eksamen 2013 Vår.pdf");
    try {
      PDDocument document = PDDocument.load(input);
      String outputHTML = PDFParser.getPrettyHTML(document);
      PrintWriter printWriter = new PrintWriter("/Users/neo/Downloads/test/out.html");
      printWriter.print(outputHTML);
      printWriter.close();
    } catch (IOException | ParserConfigurationException | TransformerException e) {
      e.printStackTrace();
    }

  }

}
