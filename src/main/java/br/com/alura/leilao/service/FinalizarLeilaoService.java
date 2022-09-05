package br.com.alura.leilao.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import br.com.alura.leilao.dao.LeilaoDao;
import br.com.alura.leilao.model.Lance;
import br.com.alura.leilao.model.Leilao;

@Service
public class FinalizarLeilaoService {

	private LeilaoDao leiloes;

	private EnviadorDeEmails enviadorDeEmails;

	/**
	 * Injecao de dependencia por construtor eh uma pratica melhor do que o Autowired,
	 * pois conseguimos passar a classe Mockada na hora do teste
	 */
	public FinalizarLeilaoService(LeilaoDao leiloes, EnviadorDeEmails enviadorDeEmails){
		this.leiloes = leiloes;
		this.enviadorDeEmails = enviadorDeEmails;
	}

	public void finalizarLeiloesExpirados() {
		List<Leilao> expirados = leiloes.buscarLeiloesExpirados();
		expirados.forEach(leilao -> {
			Lance maiorLance = maiorLanceDadoNoLeilao(leilao);
			leilao.setLanceVencedor(maiorLance);
			leilao.fechar();
			leiloes.salvar(leilao);
			//enviar um email para o lance vencedor
			enviadorDeEmails.enviarEmailVencedorLeilao(leilao.getLanceVencedor());
		});
	}

	private Lance maiorLanceDadoNoLeilao(Leilao leilao) {
		List<Lance> lancesDoLeilao = new ArrayList<>(leilao.getLances());
		lancesDoLeilao.sort((lance1, lance2) -> {
			return lance2.getValor().compareTo(lance1.getValor());
		});
		return lancesDoLeilao.get(0);
	}
	
}
