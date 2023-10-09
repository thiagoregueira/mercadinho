package br.com.api.produtos.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.api.produtos.model.Produto;

public interface ProdutoRepository extends JpaRepository<Produto, Integer> {

}
