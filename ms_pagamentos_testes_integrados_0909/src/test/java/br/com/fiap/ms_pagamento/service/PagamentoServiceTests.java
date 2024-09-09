package br.com.fiap.ms_pagamento.service;

import br.com.fiap.ms_pagamento.repository.PagamentoRepository;
import br.com.fiap.ms_pagamento.service.exception.ResourceNotFoundException;
import br.com.fiap.ms_pagamento.tests.Factory;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import br.com.fiap.ms_pagamento.dto.PagamentoDTO;
import br.com.fiap.ms_pagamento.model.Pagamento;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(SpringExtension.class)
public class PagamentoServiceTests {

    // Mock - Injetando
    @InjectMocks
    private PagamentoService service;

    // Mock do repositório
    @Mock
    private PagamentoRepository repository;

    //preparando os dados
    private Long existingId;
    private Long nonExistingId;

    //proximos testes
    private Pagamento pagamento;
    private PagamentoDTO pagamentoDTO;


    @BeforeEach
    void setup() throws Exception {
        existingId = 1L;
        nonExistingId = 10L;
        //precisa simular o comportamento do objeto mockado
        //delete - quando id existe
        Mockito.when(repository.existsById(existingId)).thenReturn(true);
        //delete - quando id não existe
        Mockito.when(repository.existsById(nonExistingId)).thenReturn(false);
        //delete - primeiro caso - deleta
        // não faça nada (void) quando ...
        Mockito.doNothing().when(repository).deleteById(existingId);

        //proximos testes
        pagamento = Factory.createPagamento();
        pagamentoDTO = new PagamentoDTO(pagamento);

        //simulação de comportamento
        //findById
        Mockito.when(repository.findById(existingId)).thenReturn(Optional.of(pagamento));
        Mockito.when(repository.findById(nonExistingId)).thenReturn(Optional.empty());

        //insert
        Mockito.when(repository.save(any())).thenReturn(pagamento);

        //update - primeiro caso - id existe
        Mockito.when(repository.getReferenceById(existingId)).thenReturn(pagamento);
        //update - segundo caso - id não existe
        Mockito.when(repository.getReferenceById(nonExistingId)).thenThrow(EntityNotFoundException.class);

        //delete - quando id existe
        Mockito.when(repository.existsById(existingId)).thenReturn(true);
        //delete - quando id não existe
        Mockito.when(repository.existsById(nonExistingId)).thenReturn(false);
        //delete - primeiro caso - deleta
        Mockito.doNothing().when(repository).deleteById(existingId);


    }

    @Test
    @DisplayName("delete Deveria não fazer nada quando Id existe")
    public void deleteShouldDoNothingWhenIdExists() {
        // no service, delete é do tipo void
        Assertions.assertDoesNotThrow(
                () -> {
                    service.delete(existingId);
                }
        );
    }

    @Test
    public void deleteShouldThrowEmptyResultDataAccessExceptionWhenIdDoesNotExist(){
        long nonExistingId = 100L;
        Assertions.assertThrows(EmptyResultDataAccessException.class, () -> {
            repository.deleteById(nonExistingId);
        });
    }

    @Test
    @DisplayName("Deveria excluir pagamento quando Id existe")
    public void deleteShouldDeleteObjectWhenIdExists(){
        //Act
        repository.deleteById(existingId);
        //Assert
        Optional<Pagamento> result = repository.findById(existingId);
        //testa se existe um objeto dentro do optional
        Assertions.assertFalse(result.isPresent());
    }

    @Test
    @DisplayName("findById deveria lançar um ResourceNotFoundException quando o ID não existe")
    public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist(){
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            repository.deleteById(nonExistingId);
        });
    }

    @Test
    @DisplayName("findById deveria retornar um DTO quando o ID existe")
    public void findByIdShouldReturnValidPagamentoDTOWhenIdExists(){
        PagamentoDTO result = service.findById(existingId);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getId(), existingId);
        Assertions.assertEquals(result.getValor(), pagamento.getValor());
    }

    @Test
    @DisplayName("insert deveria retornar um pagamentoDTO")
    public void insertShouldReturnValidPagamentoDTO(){
        PagamentoDTO result = service.insert(this.pagamentoDTO);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getId(), this.pagamentoDTO.getId());
    }

    @Test
    @DisplayName("update deveria retornar um pagamentoDTO quando o ID existe")
    public void updateShouldReturnValidPagamentoDTOWhenIdExists(){
        PagamentoDTO result = service.update(this.pagamentoDTO.getId(), this.pagamentoDTO);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getId(), this.pagamentoDTO.getId());
        Assertions.assertEquals(result.getValor(), this.pagamentoDTO.getValor());
    }

    @Test
    @DisplayName("update deveria lançar ResourceNotFoundException quando o ID não existe")
    public void updateShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist(){
        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> {
                    service.update(nonExistingId, pagamentoDTO);
                }
        );
    }

    


}











