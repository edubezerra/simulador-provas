package br.com.sc.dominio.extrator;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import br.com.sc.dominio.InjetorDependencias;
import br.com.sc.dominio.Prova;
import br.com.sc.dominio.Questao;
import br.com.sc.exception.EntidadeInconsistenteException;

public class ExtratorAbstratoQuestoesTest {

	private static String rootContextDirectoryClassPath = "";
	private static String startupContextPath = rootContextDirectoryClassPath
			+ "./springmvc-servlet.xml";
	private static ApplicationContext ctx = new FileSystemXmlApplicationContext(
			startupContextPath);

	static InjetorDependencias injetor;

	public static void main(String[] args)
			throws EntidadeInconsistenteException {
		injetor = (InjetorDependencias) ctx
				.getBean("injetorReferenciasEstaticas");

		BufferedInputStream streamArquivo;
		try {
			streamArquivo = new BufferedInputStream(
					new FileInputStream("cespe/2004/prova.pdf"));
			byte[] arquivo = IOUtils.toByteArray(streamArquivo);

			Prova prova = new Prova("prova.pdf", "Cespe", 2004, arquivo);

			System.out.println("***PROVA RECONSTITUÍDA***");
			for (Questao questao : prova.getQuestoes()) {
				if (questao.isValida()) {
					System.out.println(questao.toString());
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
