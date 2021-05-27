package br.com.alura.loja.dao;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.EntityManager;

import br.com.alura.loja.modelo.Pedido;
import br.com.alura.loja.vo.RelatorioDeVendasVo;

public class PedidoDao {

	private EntityManager em;

	// Criando uma inje��o de depend�ncia para n�o deixar a classe PedidoDAO
	// respons�vel por criar e gerenciar o EntityManager. Ela s� vai receber e
	// utilizar
	public PedidoDao(EntityManager em) {
		this.em = em;
	}

	public void cadastrar(Pedido pedido) {
		this.em.persist(pedido);
	}

	public BigDecimal valorTotalVendido() {
		String jpql = "SELECT SUM(p.valorTotal) FROM Pedido p";
		return em.createQuery(jpql, BigDecimal.class).getSingleResult();
	}

	// Consulta com v�rios joins e v�rios atributos de retorno
	public List<RelatorioDeVendasVo> relatorioDeVendas() {
		String jpql = "SELECT new br.com.alura.loja.vo.RelatorioDeVendasVo(produto.nome, SUM(item.quantidade), MAX(pedido.data)) "
					+ "  FROM Pedido pedido JOIN pedido.itens item " 
					+ "                     JOIN item.produto produto "
					+ " GROUP BY produto.nome " + " ORDER BY item.quantidade DESC";
		return em.createQuery(jpql, RelatorioDeVendasVo.class).getResultList();
	}

	// M�todo com query planejada para trazer dados que numa situa��o LAZY correria
	// o risco de resultar em LazyInitializationException
	public Pedido buscarPedidoComCliente(Long id) {
		String jpql = "SELECT p FROM Pedido p JOIN FETCH p.cliente WHERE p.id = :id";
		return em.createQuery(jpql, Pedido.class).setParameter("id", id).getSingleResult();
	}
}
