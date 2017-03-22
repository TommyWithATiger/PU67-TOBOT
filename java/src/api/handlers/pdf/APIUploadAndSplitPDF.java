package api.handlers.pdf;

import static api.helpers.RequestMethodHelper.checkRequestMethod;
import static api.helpers.isLoggedInHelper.getUserFromRequest;

import api.exceptions.APIBadRequestException;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.json.JSONObject;
import pdf.PDFParser;

public class APIUploadAndSplitPDF {

  /**
   * An API handler for uploading and splitting an PDF on exercises. This also includes converting
   * from PDF to HTML. Require the request to have a PDF document attached as content
   *
   * @param httpRequest The request to handle
   * @return A JSON string with one variable content, which is an HTML string of the PDF with the
   * given exercises
   */
  public static String uploadAndSplitPDF(HttpRequest httpRequest) {
    checkRequestMethod("POST", httpRequest);

    getUserFromRequest(httpRequest, ", must be logged in to split PDF");

    try {
      /*
       Must load PDF from an InputStream, as Java string encoding will change some of the white
       spaces. Where white spaces are really important for parsing PDFs, due to them having streams
       of byte length. Since PDFs have special start and end symbols we can just send in the complete
       request content to the parser, which will then find the first PDF in the request
        */
      PDDocument pdDocument = PDDocument
          .load(((HttpEntityEnclosingRequest) httpRequest).getEntity().getContent());
      JSONObject response = new JSONObject();
      response.put("content", PDFParser.getPrettyHTML(pdDocument));
      pdDocument.close();
      return response.toString();
    } catch (IOException | TransformerException | ParserConfigurationException ignored) {
      /*
        If we get any exceptions the format of the file makes it impossible for us to parse and
        we can ignore it. Especially if the request does not have an PDF in it, the parser will
        throw an IOException
       */
      throw new APIBadRequestException("");
    }

  }

}
