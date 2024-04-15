package com.users;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class UsersApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(UsersApplication.class, args);
	}

	/**
	 * Executa um script de shell ao iniciar a aplicação para configurar o ambiente do Couchbase.
	 * <p>
	 * Este método invoca um script externo que realiza a configuração inicial do Couchbase,
	 * garantindo que o cluster esteja pronto para uso. A execução deste script facilita a criação
	 * do bucket necessário para que possamos executar a aplicação de maneira simples no contexto do nosso teste.
	 * <p>
	 * O script também aguarda que o Couchbase, operando em um contêiner Docker, esteja completamente
	 * operacional antes de tentar qualquer configuração. Essa espera é crucial para evitar falhas de
	 * conexão causadas pelo tempo de inicialização do contêiner.
	 */
	@Override
	public void run(String... args) {
		String osName = System.getProperty("os.name").toLowerCase();
		String scriptCommand;

		if (osName.contains("mac") || osName.contains("nix") || osName.contains("nux")) {
			// MacOS or Linux
			scriptCommand = "./configure-couchbase.sh";
		} else {
			// Unsupported OS
			System.err.println("Sistema operacional não suportado para execução do script de configuração do Couchbase.");
			return;
		}

		ProcessBuilder pb = new ProcessBuilder(scriptCommand);
		pb.inheritIO();
		try {
			Process p = pb.start();
			int exitCode = p.waitFor();
			if (exitCode == 0) {
				System.out.println("Script de configuração do Couchbase executado com sucesso.");
			} else {
				System.err.println("Falha ao executar o script de configuração do Couchbase.");
			}
		} catch (IOException | InterruptedException e) {
			System.err.println("Erro ao executar o script: " + e.getMessage());
		}
	}
}
