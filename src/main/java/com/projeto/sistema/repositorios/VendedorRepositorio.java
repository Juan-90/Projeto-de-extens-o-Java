package com.projeto.sistema.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projeto.sistema.modelo.Vendedor;

public interface VendedorRepositorio extends JpaRepository<Vendedor, Long> {

	
}
