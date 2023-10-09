package br.com.api.produtos.view.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.api.produtos.service.ProdutoService;
import br.com.api.produtos.shared.ProdutoDTO;
import br.com.api.produtos.view.model.ProdutoRequest;
import br.com.api.produtos.view.model.ProdutoResponse;

@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {

    @Autowired
    private ProdutoService produtoService;

    /**
     * Obtém todos os produtos.
     * 
     * @return Um objeto ResponseEntity contendo uma lista de objetos
     *         ProdutoResponse com as informações dos produtos e o status HTTP OK.
     */
    @GetMapping
    public ResponseEntity<List<ProdutoResponse>> obterTodos() {
        List<ProdutoDTO> produtos = produtoService.obterTodos();

        ModelMapper mapper = new ModelMapper();

        List<ProdutoResponse> resposta = produtos.stream()
                .map(produtoDto -> mapper.map(produtoDto, ProdutoResponse.class))
                .collect(Collectors.toList());

        return new ResponseEntity<>(resposta, HttpStatus.OK);
    }

    /**
     * Adiciona um novo produto.
     *
     * @param produtoRequest O objeto ProdutoRequest contendo as informações do
     *                       produto a ser adicionado.
     * @return O ProdutoResponse atualizado com o ID gerado no banco de dados e o
     *         status HTTP CREATED.
     */
    @PostMapping
    public ResponseEntity<ProdutoResponse> adicionar(@RequestBody ProdutoRequest produtoRequest) {

        ModelMapper mapper = new ModelMapper();

        ProdutoDTO produtoDto = mapper.map(produtoRequest, ProdutoDTO.class);

        produtoDto = produtoService.adicionar(produtoDto);

        return new ResponseEntity<>(mapper.map(produtoDto, ProdutoResponse.class), HttpStatus.CREATED);
    }

    /**
     * Retorna um objeto ProdutoResponse com as informações do produto
     * correspondente ao ID fornecido.
     *
     * @param id O ID do produto a ser obtido.
     * @return Um objeto Optional contendo o ProdutoResponse correspondente ao ID
     *         fornecido, se encontrado.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Optional<ProdutoResponse>> obterPorId(@PathVariable("id") Integer id) {
        try {
            Optional<ProdutoDTO> dto = produtoService.obterPorId(id);
            ProdutoResponse resposta = new ModelMapper().map(dto.get(), ProdutoResponse.class);

            return new ResponseEntity<>(Optional.of(resposta), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    /**
     * Deleta um produto pelo seu ID.
     * 
     * @param id O ID do produto a ser deletado.
     * @return Um objeto ResponseEntity contendo o status HTTP NO_CONTENT.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletar(@PathVariable("id") Integer id) {
        produtoService.deletar(id);
        return new ResponseEntity<>("Produto deletado com sucesso", HttpStatus.NO_CONTENT);

    }

    /**
     * Atualiza um produto pelo seu ID.
     * 
     * @param produtoRequest O objeto ProdutoRequest contendo as informações
     *                       atualizadas do produto.
     * @param id             O ID do produto a ser atualizado.
     * @return Um objeto ResponseEntity contendo o ProdutoResponse atualizado e o
     *         status HTTP OK.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProdutoResponse> atualizar(@RequestBody ProdutoRequest produtoRequest,
            @PathVariable("id") Integer id) {
        ModelMapper mapper = new ModelMapper();

        ProdutoDTO produtoDto = mapper.map(produtoRequest, ProdutoDTO.class);

        produtoDto = produtoService.atualizar(id, produtoDto);

        return new ResponseEntity<>(
                mapper.map(produtoDto, ProdutoResponse.class),
                HttpStatus.OK);
    }
}
