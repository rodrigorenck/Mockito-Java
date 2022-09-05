package br.com.alura.leilao.service;

import br.com.alura.leilao.dao.PagamentoDao;
import br.com.alura.leilao.model.Lance;
import br.com.alura.leilao.model.Leilao;
import br.com.alura.leilao.model.Pagamento;
import br.com.alura.leilao.model.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class GeradorDePagamentoTest {

    @Autowired
    private GeradorDePagamento service;
    @Mock
    private PagamentoDao dao;

    //quando temos que testar um objeto que esta sendo criado dentro do metodo a ser testado
    // -> para termos acesso a ele nos capturamos ele quando ele for parametro de uma classe mockada
    @Captor
    private ArgumentCaptor<Pagamento> captor;

    @BeforeEach
    private void inicializar() {
        MockitoAnnotations.initMocks(this);
        service = new GeradorDePagamento(dao);
    }

    @Test
    void deveriaGerarPagamento() {
        Leilao leilao = leilao();
        Lance vencedor = leilao.getLanceVencedor();

        service.gerarPagamento(vencedor);

        //ao passar o argumento como parametro do nosso Mock conseguimos captura-lo
        Mockito.verify(dao).salvar(captor.capture());

        Pagamento pagamento = captor.getValue();
        LocalDate vencimento = LocalDate.now().plusDays(1);

        assertAll(
                () -> assertEquals(leilao, pagamento.getLeilao()),
                () -> assertEquals(vencedor.getUsuario(), pagamento.getUsuario()),
                () -> assertEquals(vencedor.getValor(), pagamento.getValor()),
                () -> assertEquals(vencimento, pagamento.getVencimento()),
                ()-> assertFalse(pagamento.getPago())
        );
    }

    private Leilao leilao() {
        Leilao leilao1 = new Leilao("Iphone 13",
                new BigDecimal("1000"),
                new Usuario("Joao"));

        Lance lance1 = new Lance(new Usuario("Andre"), new BigDecimal("1500"));
        Lance lance2 = new Lance(new Usuario("Marcelo"), new BigDecimal("1800"));
        leilao1.propoe(lance1);
        leilao1.propoe(lance2);
        leilao1.setLanceVencedor(lance2);
        return leilao1;
    }
}