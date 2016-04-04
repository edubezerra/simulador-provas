/**
 * 
 */
package org.apache.pdfbox.util;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.TextPosition;

/**
 * rewrite of PDFText2HTML that extends off of PDFTextStripper2,
 * using that class' improved instrumentation to improve the
 * tagging of text chunks.
 * 
 * @author m.martinez@ll.mit.edu
 *
 */
public class PDFText2HTML2 extends PDFTextStripper2 {

    private static final int INITIAL_PDF_TO_HTML_BYTES = 8192;

    private boolean onFirstPage = true;
    private static final String CR = System.getProperty("line.separator");
	/**
	 * @param encoding
	 * @throws IOException
	 */
	public PDFText2HTML2(String encoding) throws IOException {
		super(encoding);
        this.outputEncoding = encoding;
        setLineSeparator(CR);
        setParagraphStart("<p>");
        setParagraphEnd("</p>"+CR);
        setPageStart("<div style=”page-break-before:always; page-break-after:always”>");
        setPageEnd("</div>"+CR);
	}
	

    /**
     * Write the header to the output document. Now also writes the tag defining
     * the character encoding.
     * 
     * @throws IOException
     *             If there is a problem writing out the header to the document.
     */
    protected void writeHeader() throws IOException 
    {
        StringBuffer buf = new StringBuffer(INITIAL_PDF_TO_HTML_BYTES);
        buf.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\"" + "\n" 
                + "\"http://www.w3.org/TR/html4/loose.dtd\">\n");
        buf.append("<html><head>");
        buf.append("<title>" + getTitle() + "</title>\n");
        if(outputEncoding != null)
        {
            buf.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=" 
                    + this.outputEncoding + "\">\n");
        }
        buf.append("</head>\n");
        buf.append("<body>\n");
        super.writeString(buf.toString());
    }

    /**
     * {@inheritDoc}
     */
    protected void writePage() throws IOException 
    {
        if (onFirstPage) 
        {
            writeHeader();
            onFirstPage = false;
        }
        super.writePage();
    }

    /**
     * {@inheritDoc}
     */
    public void endDocument(PDDocument pdf) throws IOException 
    {
        super.writeString("</body></html>");
    }

    /**
     * This method will attempt to guess the title of the document using
     * either the document properties or the first lines of text.
     * 
     * @return returns the title.
     */
    protected String getTitle() 
    {
        String titleGuess = document.getDocumentInformation().getTitle();
        if(titleGuess != null && titleGuess.length() > 0)
        {
            return titleGuess;
        }
        else 
        {
            Iterator textIter = getCharactersByArticle().iterator();
            float lastFontSize = -1.0f;

            StringBuffer titleText = new StringBuffer();
            while (textIter.hasNext()) 
            {
                Iterator textByArticle = ((List) textIter.next()).iterator();
                while (textByArticle.hasNext()) 
                {
                    TextPosition position = (TextPosition) textByArticle.next();

                    float currentFontSize = position.getFontSize();
                    //If we're past 64 chars we will assume that we're past the title
                    //64 is arbitrary 
                    if (currentFontSize != lastFontSize || titleText.length() > 64) 
                    {
                        if (titleText.length() > 0) 
                        {
                            return titleText.toString();
                        }
                        lastFontSize = currentFontSize;
                    }
                    if (currentFontSize > 13.0f) 
                    { // most body text is 12pt
                        titleText.append(position.getCharacter());
                    }
                }
            }
        }
        return "";
    }


    /**
     * Write out the article separator (div tag) with proper text direction
     * information.
     * 
     * @param isltr true if direction of text is left to right
     * @throws IOException
     *             If there is an error writing to the stream.
     */
    protected void startArticle(boolean isltr) throws IOException 
    {
        if (isltr) 
        {
            super.writeString("<div>");
        } 
        else 
        {
            super.writeString("<div dir=\"RTL\">");
        }
        super.startArticle(isltr);
    }

    /**
     * Write out the article separator.
     * 
     * @throws IOException
     *             If there is an error writing to the stream.
     */
    protected void endArticle() throws IOException 
    {
    	super.endArticle();
        super.writeString("</div>");
    }

    /**
     * Write a string to the output stream and escape some HTML characters.
     *
     * @param chars String to be written to the stream
     * @throws IOException
     *             If there is an error writing to the stream.
     */
    protected void writeString(String chars) throws IOException 
    {
        for (int i = 0; i < chars.length(); i++) 
        {
            char c = chars.charAt(i);
            // write non-ASCII as named entities
            if ((c < 32) || (c > 126)) 
            {
                int charAsInt = c;
                super.writeString("&#" + charAsInt + ";");
            } 
            else 
            {
                switch (c) 
                {
                case 34:
                    super.writeString("&quot;");
                    break;
                case 38:
                    super.writeString("&amp;");
                    break;
                case 60:
                    super.writeString("&lt;");
                    break;
                case 62:
                    super.writeString("&gt;");
                    break;
                default:
                    super.writeString(String.valueOf(c));
                }
            }
        }
    }
}


