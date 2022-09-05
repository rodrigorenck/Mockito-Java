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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FinalizarLeilaoServiceTest {

    private FinalizarLeilaoService service;

    @Mock
    private LeilaoDao leilaoDao;

    @Mock
    private EnviadorDeEmails enviadorDeEmails;

    @BeforeEach
    public void inicializar(){
        //diz para o Mockito criar um mock das classes que possuem a anotacao @Mock
        //eu poderia criar um mock com o Mockito.mock() tambem
        MockitoAnnotations.initMocks(this);
        service = new FinalizarLeilaoService(leilaoDao, enviadorDeEmails);
    }

    @Test
    public void deveriaFinalizarUmLeilao(){
        //precisamos ensinar o mockito a chamar essa lista quando for buscar por leiloes no banco, ao inves de devolver uma lista vazia
        List<Leilao> leiloes = leiloes();
        //Quando eu chamar o buscarLeiloesExpirados ao inves de me retornar uma lista vazia eu quero que voce me retorne essa lista que eu criei
        //A lista de leilões foi criada e passada para o o Mockito a retornar, simulando com isso que foi no banco de dados e encontrou essa lista de leilões.
        Mockito.when(leilaoDao.buscarLeiloesExpirados()).thenReturn(leiloes);

        //esse eh o cara que queremos testar
        service.finalizarLeiloesExpirados();

        //E nos asserts verificamos se os objetos leilão dessa lista foram modificados, pois esse é o esperado para acontecer quando o método finalizarLeiloesExpirados for chamado.
        Leilao leilao = leiloes.get(0);
        assertEquals(new BigDecimal("900"), leilao.getLanceVencedor().getValor());
        assertTrue(leilao.isFechado());
        //estou falando para o mockito verificar que o metodo salvar foi chamado
        Mockito.verify(leilaoDao).salvar(leilao);
    }

    /**
     * Metodo para criar leiloes -> criando o cenario para testar o metodo finzalizar leiloes expirados
     * Como nos estamos mockando nosso acesso ao banco, ele vai devolver uma lista vazia, entao precisaremos criar uma lists ficticia manualmente
     * Estamos criando uma lista expirada e queremos ver se ela vai ser fechada quando chamarmos o metodo
     */
    private List<Leilao> leiloes(){
        List<Leilao> listLeilao = new ArrayList<>();
        Leilao leilao = new Leilao("Iphone X", new BigDecimal("500"), new Usuario("Fulano"));

        Lance lance1 = new Lance(new Usuario("Marcelo"), new BigDecimal("800"));
        Lance lance2 = new Lance(new Usuario("Giovani"), new BigDecimal("900"));

        leilao.propoe(lance1);
        leilao.propoe(lance2);

        listLeilao.add(leilao);
        return  listLeilao;
    }

}