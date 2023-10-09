package br.com.api.produtos.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.api.produtos.model.Produto;
import br.com.api.produtos.model.exception.ResourceNotFoundException;
import br.com.api.produtos.repository.ProdutoRepository;
import br.com.api.produtos.shared.ProdutoDTO;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    /**
     * Retorna uma lista de todos os produtos.
     * 
     * @return Lista de objetos ProdutoDTO contendo as informações dos produtos.
     */
    public List<ProdutoDTO> obterTodos() {

        List<Produto> produtos = produtoRepository.findAll();

        return produtos.stream()
                .map(produto -> new ModelMapper().map(produto, ProdutoDTO.class))
                .collect(Collectors.toList());
    }

    /**
     * Retorna um objeto ProdutoDTO com as informações do produto correspondente ao
     * ID fornecido.
     * 
     * @param id O ID do produto a ser obtido.
     * @return Um objeto Optional contendo o ProdutoDTO correspondente ao ID
     *         fornecido, se encontrado.
     * @throws ResourceNotFoundException Se nenhum produto for encontrado com o ID
     *                                   fornecido.
     */
    public Optional<ProdutoDTO> obterPorId(Integer id) {
        Optional<Produto> produto = produtoRepository.findById(id);
        if (produto.isEmpty()) {
            throw new ResourceNotFoundException("Produto com id " + id + " não encontrado");
        }
        ProdutoDTO dto = new ModelMapper().map(produto.get(), ProdutoDTO.class);
        return Optional.of(dto);
    }

    /**
     * Adiciona um novo produto.
     * 
     * @param produtoDto O objeto ProdutoDTO contendo as informações do produto a
     *                   ser adicionado.
     * @return O ProdutoDTO atualizado com o ID gerado no banco de dados.
     */
    public ProdutoDTO adicionar(ProdutoDTO produtoDto) {
        // Remover o id para conseguir fazer o cadastro
        produtoDto.setId(null);

        // Criar um objeto de mapeamento
        ModelMapper mapper = new ModelMapper();

        // Converter o ProdutoDTO para Produto
        Produto produto = mapper.map(produtoDto, Produto.class);

        // Salvar o produto no banco de dados
        produto = produtoRepository.save(produto);

        produtoDto.setId(produto.getId());

        // Retornar o ProdutoDTO atualizado
        return produtoDto;
    }

    /**
     * Deleta um produto com o ID fornecido.
     *
     * @param id o ID do produto a ser deletado
     * @throws ResourceNotFoundException se nenhum produto for encontrado com o ID
     *                                   fornecido
     */
    public void deletar(Integer id) {
        // Verificar se o produto existe
        Optional<Produto> produto = produtoRepository.findById(id);
        // Se não existir, lançar uma exceção
        if (produto.isEmpty()) {
            throw new ResourceNotFoundException("Produto com id " + id + " não encontrado");
        }
        // Deletar o produto
        produtoRepository.deleteById(id);
    }

    /**
     * Atualiza um produto com o ID fornecido.
     *
     * @param id         o ID do produto a ser atualizado
     * @param produtoDto o objeto ProdutoDTO contendo as informações do produto a
     *                   ser atualizado
     * @return o ProdutoDTO atualizado
     * @throws ResourceNotFoundException se nenhum produto for encontrado com o ID
     *                                   fornecido
     */
    public ProdutoDTO atualizar(Integer id, ProdutoDTO produtoDto) {
        // Passar o id para o produtoDto
        produtoDto.setId(id);
        // Criar um objeto de mapeamento
        ModelMapper mapper = new ModelMapper();
        // Converter o ProdutoDTO para Produto
        Produto produto = mapper.map(produtoDto, Produto.class);
        // Atualizar o produto no banco de dados
        produtoRepository.save(produto);
        // Retornar o ProdutoDTO atualizado
        return produtoDto;
    }
}
