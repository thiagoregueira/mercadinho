package br.com.api.produtos.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import br.com.api.produtos.model.Produto;
import br.com.api.produtos.model.exception.ResourceNotFoundException;

@Repository
public class ProdutoRepositoryNaMao {

    private List<Produto> produtos = new ArrayList<>();
    private Integer ultimoId = 0;

    /**
     * Retorna todos os produtos.
     *
     * @return uma lista de objetos Produto representando todos os produtos
     */
    public List<Produto> obterTodos() {
        return produtos;
    }

    /**
     * Recupera um produto pelo seu ID.
     *
     * @param id o ID do produto a ser recuperado
     * @return um Optional contendo o produto com o ID fornecido, ou vazio se nenhum
     *         produto com esse ID existir
     */
    public Optional<Produto> obterPorId(Integer id) {
        return produtos
                .stream()
                .filter(produto -> produto.getId() == id)
                .findFirst();
    }

    /**
     * Adiciona um novo produto à lista de produtos.
     *
     * @param produto o produto a ser adicionado
     * @return o produto adicionado
     */
    public Produto adicionar(Produto produto) {
        produto.setId(++ultimoId);
        produtos.add(produto);
        return produto;
    }

    /**
     * Deleta um elemento da lista com base no ID fornecido.
     *
     * @param id o ID do elemento a ser deletado
     */
    public void deletar(Integer id) {
        produtos.removeIf(produto -> produto.getId() == id);

    }

    /**
     * Atualiza um produto no sistema.
     *
     * @param produto o produto a ser atualizado
     * @return o produto atualizado
     */
    public Produto atualizar(Produto produto) {
        Optional<Produto> produtoExistente = obterPorId(produto.getId());

        if (produtoExistente.isEmpty()) {
            throw new ResourceNotFoundException("Produto não encontrado");
        }

        deletar(produto.getId());
        produtos.add(produto);
        return produto;

    }
}
