package com.projeto.sistema.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projeto.sistema.modelo.Produto;

public interface ProdutoRepositorio extends JpaRepository<Produto, Long> {

	
}
