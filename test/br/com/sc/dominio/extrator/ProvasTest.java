package br.com.sc.dominio.extrator;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import br.com.sc.dominio.InjetorDependencias;
import br.com.sc.dominio.Prova;
import br.com.sc.dominio.Questao;
import br.com.sc.dominio.RepositorioProva;
import br.com.sc.servico.ServicoIndexacao;

public class ProvasTest {

	private static String rootContextDirectoryClassPath = "";
	private static String startupContextPath = rootContextDirectoryClassPath
			+ "./springmvc-servlet.xml";
	private static ApplicationContext ctx = new FileSystemXmlApplicationContext(
			startupContextPath);

	static InjetorDependencias injetor;
	
	static private RepositorioProva repositorio;

	public static void main(String[] args) {
		injetor = (InjetorDependencias) ctx.getBean("injetorReferenciasEstaticas");

		List<Questao> questoes = new ArrayList<Questao>();
		
		List<Prova> provas = ServicoIndexacao.repositorioProva.obterTodas();
		for (Prova prova : provas) {
			System.out.println(prova.getNomeArquivo());
			questoes.addAll(prova.getQuestoes());
		}
		
		for (Questao questao : questoes) {
			System.out.println(questao);
		}
	}

}
