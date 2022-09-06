package br.com.alura.leilao.service;

import java.time.Clock;
import java.time.DayOfWeek;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.alura.leilao.dao.PagamentoDao;
import br.com.alura.leilao.model.Lance;
import br.com.alura.leilao.model.Pagamento;

@Service
public class GeradorDePagamento {

	private PagamentoDao pagamentos;

	//abstracao de date para usarmos nos testes -> conseguiremos simular uma data fixa -> conseguimos mockar
	private Clock clock;

	public GeradorDePagamento(PagamentoDao pagamentos, Clock clock) {
		this.pagamentos = pagamentos;
		this.clock = clock;
	}

	public void gerarPagamento(Lance lanceVencedor) {
		LocalDate vencimento = LocalDate.now(clock).plusDays(1);
		Pagamento pagamento = new Pagamento(lanceVencedor, proximoDiaUtil(vencimento));
		this.pagamentos.salvar(pagamento);
	}

	//caso o vencimento caia no sabado ou no domingo temos que passa-lo para segunda
	private LocalDate proximoDiaUtil(LocalDate vencimento) {
		DayOfWeek diaDaSemana = vencimento.getDayOfWeek();
		if(diaDaSemana == DayOfWeek.SATURDAY){
			return vencimento.plusDays(2);
		} else if(diaDaSemana == DayOfWeek.SUNDAY){
			return vencimento.plusDays(1);
		}
		return vencimento;
	}
}
