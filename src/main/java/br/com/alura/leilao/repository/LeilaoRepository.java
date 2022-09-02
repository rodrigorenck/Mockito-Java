package br.com.alura.leilao.repository;

import br.com.alura.leilao.model.Leilao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LeilaoRepository extends JpaRepository<Leilao, Long> {
}
