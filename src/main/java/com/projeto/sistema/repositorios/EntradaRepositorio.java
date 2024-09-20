package com.projeto.sistema.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projeto.sistema.modelo.Entrada;

public interface EntradaRepositorio extends JpaRepository<Entrada, Long> {

	
}
