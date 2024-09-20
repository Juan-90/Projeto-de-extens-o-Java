package com.projeto.sistema.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projeto.sistema.modelo.Fornecedor;

public interface FornecedorRepositorio extends JpaRepository<Fornecedor, Long> {

	
}
