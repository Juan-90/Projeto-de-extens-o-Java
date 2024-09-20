package com.projeto.sistema.controle;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.projeto.sistema.modelo.ItemVenda;
import com.projeto.sistema.modelo.Produto;
import com.projeto.sistema.modelo.Venda;
import com.projeto.sistema.repositorios.ClienteRepositorio;
import com.projeto.sistema.repositorios.ItemVendaRepositorio;
import com.projeto.sistema.repositorios.ProdutoRepositorio;
import com.projeto.sistema.repositorios.VendaRepositorio;
import com.projeto.sistema.repositorios.VendedorRepositorio;

@Controller
public class VendaControle {

	@Autowired
	private VendaRepositorio vendaRepositorio;
	@Autowired
	private ItemVendaRepositorio itemVendaRepositorio;
	@Autowired
	private ProdutoRepositorio produtoRepositorio;
	@Autowired
	private VendedorRepositorio vendedorRepositorio;
	@Autowired
	private ClienteRepositorio clienteRepositorio;
	
	private List<ItemVenda> listaItemVenda = new ArrayList<ItemVenda>();
	
	@GetMapping("/cadastroVenda")
	public ModelAndView cadastrar(Venda venda, ItemVenda itemVenda) {
		ModelAndView mv = new ModelAndView("administrativo/vendas/cadastro");
		mv.addObject("venda", venda);
		mv.addObject("itemVenda", itemVenda);
		mv.addObject("listaItemVenda", this.listaItemVenda);
		mv.addObject("listaVendedors", vendedorRepositorio.findAll());
		mv.addObject("listaClientes", clienteRepositorio.findAll());
		mv.addObject("listaProdutos", produtoRepositorio.findAll());
		return mv;
	}
	
	@GetMapping("/listarVenda")
	public ModelAndView listar() {
		ModelAndView mv = new ModelAndView("administrativo/vendas/lista");
		mv.addObject("listaVendas", vendaRepositorio.findAll());
		return mv;
	}
	
	@GetMapping("/editarVenda/{id}")
	public ModelAndView editar(@PathVariable("id") Long id) {
	    Optional<Venda> vendaOpt = vendaRepositorio.findById(id);
	    if (vendaOpt.isPresent()) {
	        Venda venda = vendaOpt.get();
	        this.listaItemVenda = itemVendaRepositorio.buscarPorVenda(venda.getId());
	        
	        ModelAndView mv = new ModelAndView("administrativo/vendas/cadastro"); 
	        mv.addObject("venda", venda);
	        mv.addObject("listaItemVenda", this.listaItemVenda);
	        
	        return mv;
	    } else {
	        return new ModelAndView("erro404"); 
	    }
	}



	
	@GetMapping("/removerItemVenda/{id}")
	public ModelAndView removerItemVenda(@PathVariable("id") Long id) {
	    System.out.println("Removendo item de venda com ID: " + id);
	    Optional<ItemVenda> itemVendaOpt = itemVendaRepositorio.findById(id);
	    if (itemVendaOpt.isPresent()) {
	        itemVendaRepositorio.delete(itemVendaOpt.get());
	    } else {
	        System.out.println("Item de venda com ID " + id + " não encontrado.");
	    }
	    return new ModelAndView("redirect:/cadastroVenda"); 

	}


	
	@PostMapping("/salvarVenda")
	public ModelAndView salvar(String acao, Venda venda, ItemVenda itemVenda, BindingResult result) {
		if(result.hasErrors()) {
			return cadastrar(venda, itemVenda);
		}
		
		if (acao.equals("itens")) {
			itemVenda.setValor(itemVenda.getProduto().getPrecoVenda());
			itemVenda.setSubtotal(itemVenda.getProduto().getPrecoVenda() * itemVenda.getQuantidade());
			venda.setValorTotal(venda.getValorTotal() + itemVenda.getValor() * itemVenda.getQuantidade());
			venda.setQuantidadeTotal(venda.getQuantidadeTotal() + itemVenda.getQuantidade());
			
			this.listaItemVenda.add(itemVenda);
			
		} else if(acao.equals("salvar")) {
			vendaRepositorio.saveAndFlush(venda);
			
			for(ItemVenda it: listaItemVenda) {
				it.setVenda(venda);
	//			it.setSubtotal(it.getValor() * it.getQuantidade());
				itemVendaRepositorio.saveAndFlush(it);
				
				Optional<Produto> prod = produtoRepositorio.findById(it.getProduto().getId());
				Produto produto =prod.get();
				produto.setEstoque(produto.getEstoque() - it.getQuantidade());
				produto.setPrecoVenda(it.getValor());
				//produto.setPrecoCusto(it.getValorCusto());
				produtoRepositorio.saveAndFlush(produto);
				
				this.listaItemVenda = new ArrayList<>();
			}
			
			return cadastrar(new Venda(), new ItemVenda());
		}
		
		return cadastrar(venda, new ItemVenda());
	}

	public List<ItemVenda> getListaItemVenda() {
		return listaItemVenda;
	}

	public void setListaItemVenda(List<ItemVenda> listaItemVenda) {
		this.listaItemVenda = listaItemVenda;
	}
	
}
