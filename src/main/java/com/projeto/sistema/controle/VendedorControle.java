package com.projeto.sistema.controle;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.projeto.sistema.modelo.Vendedor;
import com.projeto.sistema.repositorios.VendedorRepositorio;

@Controller
public class VendedorControle {

	@Autowired
	private VendedorRepositorio vendedorRepositorio;
	
	@GetMapping("/cadastroVendedor")
	public ModelAndView cadastrar(Vendedor vendedor) {
		ModelAndView mv = new ModelAndView("administrativo/vendedors/cadastro");
		mv.addObject("vendedor", vendedor);
		return mv;
	}
	
	@GetMapping("/listarVendedor")
	public ModelAndView listar() {
		ModelAndView mv = new ModelAndView("administrativo/vendedors/lista");
		mv.addObject("listaVendedors", vendedorRepositorio.findAll());
		return mv;
	}
	
	@GetMapping("/editarVendedor/{id}")
	public ModelAndView editar(@PathVariable("id") Long id) {
		Optional<Vendedor> vendedor = vendedorRepositorio.findById(id);
		return cadastrar(vendedor.get());
	}
	
	@GetMapping("/removerVendedor/{id}")
	public ModelAndView remover(@PathVariable("id") Long id) {
		Optional<Vendedor> vendedor = vendedorRepositorio.findById(id);
		vendedorRepositorio.delete(vendedor.get());
		return listar();
	}
	
	@PostMapping("/salvarVendedor")
	public ModelAndView salvar(Vendedor vendedor, BindingResult result) {
		if(result.hasErrors()) {
			return cadastrar(vendedor);
		}
		vendedorRepositorio.saveAndFlush(vendedor);
		return cadastrar(new Vendedor());
	}
}
