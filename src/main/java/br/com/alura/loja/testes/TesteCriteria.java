package br.com.alura.loja.testes;

import java.math.BigDecimal;

import javax.persistence.EntityManager;

import br.com.alura.loja.dao.CategoriaDao;
import br.com.alura.loja.dao.ClienteDao;
import br.com.alura.loja.dao.PedidoDao;
import br.com.alura.loja.dao.ProdutoDao;
import br.com.alura.loja.modelo.Categoria;
import br.com.alura.loja.modelo.Cliente;
import br.com.alura.loja.modelo.ItemPedido;
import br.com.alura.loja.modelo.Pedido;
import br.com.alura.loja.modelo.Produto;
import br.com.alura.loja.util.JPAUtil;

public class TesteCriteria {

	public static void main(String[] args) {
		// Cadastrando um produto e cliente para teste
		popularBancoDeDados();

		EntityManager em = JPAUtil.getEntityManager();

		var produtoDao = new ProdutoDao(em);
		produtoDao.buscarPorParametros("Xiaomi Redmi", null, null);
		
		var produtoDao1 = new ProdutoDao(em);
		produtoDao1.buscarPorParametrosComCriteria("PS5", null, null);
		
		em.close();
	}

	private static void popularBancoDeDados() {
		// Criando objetos com novas categorias
		Categoria celulares = new Categoria("CELULARES");
		Categoria videogames = new Categoria("VIDEOGAMES");
		Categoria informatica = new Categoria("INFORMATICA");

		// Criando objetos com novos produtos
		Produto celular = new Produto("Xiaomi Redmi", "Muito legal", new BigDecimal("800"), celulares);
		Produto videogame = new Produto("PS5", "Playstation 5", new BigDecimal("800"), videogames);
		Produto macbook = new Produto("Macbook", "Macbook pro reti", new BigDecimal("800"), informatica);

		// Criando objeto com novo cliente
		Cliente cliente = new Cliente("Almir", "123456");

		// Criando objeto com cliente no pedido
		Pedido pedido = new Pedido(cliente);

		// Criando objeto com cliente no pedido 2
		Pedido pedido2 = new Pedido(cliente);

		EntityManager em = JPAUtil.getEntityManager();
		ProdutoDao produtoDao = new ProdutoDao(em);
		CategoriaDao categoriaDao = new CategoriaDao(em);
		ClienteDao clienteDao = new ClienteDao(em);

		em.getTransaction().begin();

		// Cadastrando novas categorias
		categoriaDao.cadastrar(celulares);
		categoriaDao.cadastrar(videogames);
		categoriaDao.cadastrar(informatica);

		// Cadastrando novos produtos
		produtoDao.cadastrar(celular);
		produtoDao.cadastrar(videogame);
		produtoDao.cadastrar(macbook);

		// Cadastrando novo cliente
		clienteDao.cadastrar(cliente);

		// Adicionando um item ao pedido
		pedido.adicionarItem(new ItemPedido(10, pedido, celular));
		pedido.adicionarItem(new ItemPedido(40, pedido, videogame));

		// Adicionando um item ao pedido 2
		pedido.adicionarItem(new ItemPedido(2, pedido2, macbook));

		// Cadastrando os pedidos
		PedidoDao pedidoDao = new PedidoDao(em);
		pedidoDao.cadastrar(pedido);
		pedidoDao.cadastrar(pedido2);

		em.getTransaction().commit();
		em.close();
	}

}
