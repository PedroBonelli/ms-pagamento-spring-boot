package com.github.pedrobonelli.ms_pagamento.service;

import com.github.pedrobonelli.ms_pagamento.dto.PagamentoDTO;
import com.github.pedrobonelli.ms_pagamento.model.Pagamento;
import com.github.pedrobonelli.ms_pagamento.model.Status;
import com.github.pedrobonelli.ms_pagamento.repository.PagamentoRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PagamentoService {

    @Autowired
    private PagamentoRepository repository;

    @Transactional()
    public Page<PagamentoDTO> findAll(Pageable pageable){
        Page<Pagamento> page = repository.findAll(pageable);
        return page.map(PagamentoDTO::new);
    }

    @Transactional()
    public PagamentoDTO findById(Long id){
        return new PagamentoDTO(repository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Recurso não encontrado")
        ));
    }

    public PagamentoDTO insert(PagamentoDTO dto) {
        Pagamento entity = new Pagamento();
        copyDtoToEntity(dto, entity);
        entity = repository.save(entity);
        return new PagamentoDTO(entity);
    }

    public void delete(Long id){
        if(!repository.existsById(id)){
            throw new IllegalArgumentException("Esse recurso não existe no sistema");
        }
        repository.deleteById(id);
    }

    private void copyDtoToEntity(PagamentoDTO dto, Pagamento entity){
        entity.setValor(dto.getValor());
        entity.setNome(dto.getNome());
        entity.setNumeroDoCartao(dto.getNumeroDoCartao());
        entity.setValidade(dto.getValidade());
        entity.setCodigoDeSeguranca(dto.getCodigoDeSeguranca());
        entity.setStatus(Status.CRIADO);
        entity.setPedidoId(dto.getPedidoId());
        entity.setFormaDePagamentoId(dto.getFormaDePagamentoId());
    }

}
