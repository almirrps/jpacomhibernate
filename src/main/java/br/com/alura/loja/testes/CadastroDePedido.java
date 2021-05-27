package br.com.alura.loja.testes;

import java.math.BigDecimal;
import java.util.List;

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
import br.com.alura.loja.vo.RelatorioDeVendasVo;

public class CadastroDePedido {

	public static void main(String[] args) {
		// Cadastrando um produto e cliente para teste
		popularBancoDeDados();

		EntityManager em = JPAUtil.getEntityManager();
		ProdutoDao produtoDao = new ProdutoDao(em);
		ClienteDao clienteDao = new ClienteDao(em);

		// Buscando os produtos no banco de dados
		Produto produto = produtoDao.buscarPorId(1l);
		Produto produto2 = produtoDao.buscarPorId(2l);
		Produto produto3 = produtoDao.buscarPorId(3l);

		// Buscando o cliente no banco de dados
		Cliente cliente = clienteDao.buscarPorId(1l);

		em.getTransaction().begin();

		// Adicionando cliente no pedido
		Pedido pedido = new Pedido(cliente);

		// Adicionando um item ao pedido
		pedido.adicionarItem(new ItemPedido(10, pedido, produto));
		pedido.adicionarItem(new ItemPedido(40, pedido, produto2));

		// Adicionando cliente no pedido 2
		Pedido pedido2 = new Pedido(cliente);

		// Adicionando um item ao pedido 2
		pedido.adicionarItem(new ItemPedido(2, pedido2, produto3));

		// Cadastrando os pedidos
		PedidoDao pedidoDao = new PedidoDao(em);
		pedidoDao.cadastrar(pedido);
		pedidoDao.cadastrar(pedido2);

		em.getTransaction().commit();

		// Apresentando o valor total vendido
		BigDecimal totalVendido = pedidoDao.valorTotalVendido();
		System.out.println("VALOR TOTAL VENDIDO: " + totalVendido);

		// Buscando dados para relatório
		List<RelatorioDeVendasVo> relatorio = pedidoDao.relatorioDeVendas();
		relatorio.forEach(System.out::println);

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

		// cadastrando novo cliente
		clienteDao.cadastrar(cliente);

		em.getTransaction().commit();
		em.close();
	}

}
