package br.com.alura.loja.dao;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.EntityManager;

import br.com.alura.loja.modelo.Produto;

public class ProdutoDao {

	private EntityManager em;

	// Criando uma injeção de dependência para não deixar a classe ProdutoDAO
	// responsável por criar e gerenciar o EntityManager. Ela só vai receber e
	// utilizar
	public ProdutoDao(EntityManager em) {
		super();
		this.em = em;
	}

	public void cadastrar(Produto produto) {
		this.em.persist(produto);
	}

	public void atualizar(Produto produto) {
		this.em.merge(produto);
	}

	public void remover(Produto produto) {
		produto = em.merge(produto);
		this.em.remove(produto);
	}

	public Produto buscarPorId(Long id) {
		return em.find(Produto.class, id);
	}
	
	public List<Produto> buscarTodos() {
		String jpql = "SELECT p FROM Produto p";
		return em.createQuery(jpql, Produto.class).getResultList();
	}

	public List<Produto> buscarPorNome(String nome) {
		String jpql = "SELECT p FROM Produto p WHERE p.nome = :pnome";
		return em.createQuery(jpql, Produto.class)
				.setParameter("pnome", nome)
				.getResultList();
	}

	public List<Produto> buscarPorNomeDaCategoria(String nomeCategoria) {
		String jpql = "SELECT p FROM Produto p WHERE p.categoria.nome = :pnomeCategoria";
		return em.createQuery(jpql, Produto.class)
				.setParameter("pnomeCategoria", nomeCategoria)
				.getResultList();
	}

	public BigDecimal buscarPrecoDoProdutoComNome(String nome) {
		String jpql = "SELECT p.preco FROM Produto p WHERE p.nome = :pnome";
		return em.createQuery(jpql, BigDecimal.class)
				.setParameter("pnome", nome)
				.getSingleResult();
	}

}
