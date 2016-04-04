package br.com.sc.dominio.extrator;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.util.PDFTextStripper;

public class ExtratorCodePointsTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		InputStreamReader arquivo;
		InputStream streamArquivo = null;
		PDDocument documento = null;
		
		try {
			
			
			// A solução abaixo não leu o texto no documento PDF
//			arquivo = new InputStreamReader(
//					new FileInputStream("cespe/2004/prova.pdf"), "UTF-8");
//			byte[] byteArray= IOUtils.toByteArray(arquivo);
//			System.out.println("Total de bytes no documento: " + byteArray.length);
			
			
			streamArquivo = new BufferedInputStream(
					new FileInputStream("cespe/2004/prova.pdf"));
			byte[] byteArray= IOUtils.toByteArray(streamArquivo);
			 
			// 1 - Todo o conteudo da prova é extraído para uma string
			streamArquivo = new ByteArrayInputStream(byteArray);
			documento = PDDocument.load(streamArquivo);
			PDFTextStripper stripper = new PDFTextStripper();
			
			String texto = stripper.getText(documento);
			System.out.println("\nTotal de caracteres no texto: " + texto.length());
			
			System.out.println("\nA primeira ocorrencia desse  caractere e: " + texto.indexOf(""));
			
			System.out.println("\nA sessao com a ocorrencia desse  caractere e: " + texto.substring(0, texto.indexOf("")));
			
			System.out.println("\nQuantidade de codepoints: " + texto.codePointCount(0, texto.length()));
			
			texto = texto.replaceAll("—", "-");
			texto = texto.replaceAll("([^\\p{L}+\\s\\.\\,\\-\\?\\n\\r\\)\\(1-9])", "OPT-");
			
			System.out.println("\nInicio do texto" + texto);
		
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
