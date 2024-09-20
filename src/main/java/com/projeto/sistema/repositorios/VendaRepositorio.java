package com.projeto.sistema.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projeto.sistema.modelo.Venda;

public interface VendaRepositorio extends JpaRepository<Venda, Long> {

	
}
