package com.github.pedrobonelli.ms_pagamento.repository;

import com.github.pedrobonelli.ms_pagamento.model.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PagamentoRepository extends JpaRepository<Pagamento, Long> {



}
