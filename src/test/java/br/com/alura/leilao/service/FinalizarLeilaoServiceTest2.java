package br.com.alura.leilao.service;

import br.com.alura.leilao.dao.LeilaoDao;
import br.com.alura.leilao.model.Lance;
import br.com.alura.leilao.model.Leilao;
import br.com.alura.leilao.model.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FinalizarLeilaoServiceTest2 {

    @Autowired
    FinalizarLeilaoService service;
    @Mock
    LeilaoDao dao;
    @Mock
    EnviadorDeEmails enviadorDeEmails;

    @BeforeEach
    public void inicizalizar() {
        //cria mock dos atributos que tem a anotacao @Mock
        MockitoAnnotations.initMocks(this);
        service = new FinalizarLeilaoService(dao, enviadorDeEmails);
    }


    @Test
    public void deveriaFinalizarUmLeilao() {
        List<Leilao> leiloesExpirados = listaLeiloesExpirados();
        //simular comportamento de um metodo
        Mockito.when(dao.buscarLeiloesExpirados()).thenReturn(leiloesExpirados);

        service.finalizarLeiloesExpirados();

        Leilao leilao = leiloesExpirados.get(0);
        assertEquals(new BigDecimal("1800"), leilao.getLanceVencedor().getValor());
        assertTrue(leilao.isFechado());
        //verificar se o metodo foi chamado
        Mockito.verify(dao).salvar(leilao);
    }

    @Test
    public void deveriaEnviarUmEmailParaLanceVencedor(){
        List<Leilao> leiloesExpirados = listaLeiloesExpirados();
        //simular comportamento de um metodo
        Mockito.when(dao.buscarLeiloesExpirados()).thenReturn(leiloesExpirados);

        service.finalizarLeiloesExpirados();

        Leilao leilao = leiloesExpirados.get(0);
        Mockito.verify(enviadorDeEmails).enviarEmailVencedorLeilao(leilao.getLanceVencedor());
    }



    private List<Leilao> listaLeiloesExpirados() {
        List<Leilao> listaLeilao = new ArrayList<>();
        Leilao leilao1 = new Leilao("Iphone 13",
                new BigDecimal("1000"),
                new Usuario("Joao"));


        Lance lance1 = new Lance(new Usuario("Andre"), new BigDecimal("1500"));
        Lance lance2 = new Lance(new Usuario("Marcelo"),new BigDecimal("1800"));
        leilao1.propoe(lance1);
        leilao1.propoe(lance2);

        listaLeilao.add(leilao1);
        return listaLeilao;
    }
}
