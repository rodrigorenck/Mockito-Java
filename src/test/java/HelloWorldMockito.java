import br.com.alura.leilao.dao.LanceDao;
import br.com.alura.leilao.dao.LeilaoDao;
import br.com.alura.leilao.model.Leilao;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HelloWorldMockito {

    @Test
    void hello(){
        LeilaoDao mock = Mockito.mock(LeilaoDao.class);
        //simula o metodo -> na verdade ele nao esta indo no banco, ele esta retornando uma lista vazia!
        List<Leilao> leilaos = mock.buscarTodos();
        Leilao leilao = mock.buscarPorId(10L);
        System.out.println(leilao);

        assertEquals(true, leilaos.isEmpty());
    }

//    @Test
//    void hello2(){
//        EntityManagerFactory emf = Persistence.createEntityManagerFactory("leiloes");
//        EntityManager entityManager = emf.createEntityManager();
//        LeilaoDao dao = new LeilaoDao(entityManager);
//        //simula o metodo -> na verdade ele nao esta indo no banco, ele esta retornando uma lista vazia!
//        List<Leilao> leilaos = dao.buscarTodos();
//
//        assertEquals(true, leilaos.isEmpty());
//    }
}
