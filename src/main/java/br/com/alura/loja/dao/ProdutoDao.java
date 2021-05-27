package br.com.alura.loja.dao;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import br.com.alura.loja.modelo.Produto;

public class ProdutoDao {

	private EntityManager em;

	// Criando uma injeção de dependência para não deixar a classe ProdutoDAO
	// responsável por criar e gerenciar o EntityManager. Ela só vai receber e
	// utilizar
	public ProdutoDao(EntityManager em) {
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
		// Consulta em JPQL com filtro
		String jpql = "SELECT p FROM Produto p WHERE p.nome = :pnome";
		return em.createQuery(jpql, Produto.class).setParameter("pnome", nome).getResultList();
	}

	public List<Produto> buscarPorNomeDaCategoria(String nomeCategoria) {
		// Consultas em JPQL com filtro e join
		String jpql = "SELECT p FROM Produto p WHERE p.categoria.nome = :pnomeCategoria";
		return em.createQuery(jpql, Produto.class).setParameter("pnomeCategoria", nomeCategoria).getResultList();
	}

	public BigDecimal buscarPrecoDoProdutoComNome(String nome) {
		// Consultas em JPQL com filtro e retorno com um atributo somente
		String jpql = "SELECT p.preco FROM Produto p WHERE p.nome = :pnome";
		return em.createQuery(jpql, BigDecimal.class).setParameter("pnome", nome).getSingleResult();
	}
	
	//Consulta com parâmetros dinâmico - na mão
	public List<Produto> buscarPorParametros(String nome, BigDecimal preco, LocalDate dataCadastro) {
		//Montando a consulta
		String jpql = "SELECT p FROM Produto p WHERE 1=1 ";
		if (nome != null && !nome.trim().isEmpty()) {
			jpql += " AND p.nome = :nome ";
		}
				
		if (preco != null) {
			jpql += " AND p.preco = :preco ";
		}
		
		if (dataCadastro != null) {
			jpql += " AND p.dataCadastro = :dataCadastro ";
		}
		
		//Criando a query com os parâmetros
		TypedQuery<Produto> query = em.createQuery(jpql, Produto.class);
		if (nome != null && !nome.trim().isEmpty()) {
			query.setParameter("nome", nome);
		}
				
		if (preco != null) {
			query.setParameter("preco", preco);
		}
		
		if (dataCadastro != null) {
			query.setParameter("dataCadastro", dataCadastro);
		}
		
		return query.getResultList();

	}

	//Consulta com parâmetros dinâmico - com CriteriaQuery
	public List<Produto> buscarPorParametrosComCriteria(String nome, BigDecimal preco, LocalDate dataCadastro) {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Produto> queryCriteria = builder.createQuery(Produto.class);
		Root<Produto> from = queryCriteria.from(Produto.class);
		
		Predicate filtros = builder.and();
		if (nome != null && !nome.trim().isEmpty()) {
			filtros = builder.and(filtros, builder.equal(from.get("nome"), nome));
		}

		if (preco != null) {
			filtros = builder.and(filtros, builder.equal(from.get("preco"), preco));
		}

		if (dataCadastro != null) {
			filtros = builder.and(filtros, builder.equal(from.get("dataCadastro"), dataCadastro));
		}
		queryCriteria.where(filtros);

		return em.createQuery(queryCriteria).getResultList();
	}

}
