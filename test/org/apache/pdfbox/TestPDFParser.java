package org.apache.pdfbox;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdfparser.BaseParser;
import org.apache.pdfbox.pdmodel.PDDocument;

/**
 * This will test the PDF parsing in PDFBox.
 * 
 * @author <a href="mailto:ben@benlitchfield.com">Ben Litchfield</a>
 * @version $Revision: 1.3 $
 */
public class TestPDFParser {
	public static void main(String[] args) throws Exception {
		TestPDFParser tester = new TestPDFParser();
		tester.testCOSName();
		tester.testParsingTroublePDFs();
	}

	/**
	 * This will test some cos name parsing.
	 * 
	 * @throws Exception
	 *             If there is an exception while parsing.
	 */
	public void testCOSName() throws Exception {
		TestParser parser = new TestParser(new ByteArrayInputStream(
				"/PANTONE#20116#20CV".getBytes()));
		COSName name = parser.parseCOSName();
		if (!name.getName().equals("PANTONE 116 CV")) {
			System.out.println("Failed to parse COSName");
			System.exit(1);
		}

	}

	/**
	 * Test some trouble PDFs, these should all parse without an issue.
	 * 
	 * @throws Exception
	 *             If there is an error parsing the PDF.
	 */
	public void testParsingTroublePDFs() throws Exception {
		PDDocument doc = null;
		try {
			doc = PDDocument
					.load("C:/provas/armazenamento/PETROBRAS(Objetiva)/prova81.pdf");
		} finally {
			if (doc != null) {
				doc.close();
			}
		}
	}

	/**
	 * A simple class used to test parsing of the cos name.
	 */
	private class TestParser extends BaseParser {
		/**
		 * Constructor.
		 * 
		 * @param input
		 *            The input stream.
		 * @throws IOException
		 *             If there is an error during parsing.
		 */
		public TestParser(InputStream input) throws IOException {
			super(input);
		}
		
		

		/**
		 * Expose the parseCOSName as public.
		 * 
		 * @return The parsed cos name.
		 * @throws IOException
		 *             If there is an error parsing the COSName.
		 */
		public COSName parseCOSName() throws IOException {
			return super.parseCOSName();
		}
	}
}