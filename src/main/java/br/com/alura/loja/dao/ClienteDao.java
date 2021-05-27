package br.com.alura.loja.dao;

import javax.persistence.EntityManager;

import br.com.alura.loja.modelo.Cliente;

public class ClienteDao {

	private EntityManager em;

	// Criando uma inje��o de depend�ncia para n�o deixar a classe ClienteDAO
	// respons�vel por criar e gerenciar o EntityManager. Ela s� vai receber e
	// utilizar
	public ClienteDao(EntityManager em) {
		this.em = em;
	}

	public void cadastrar(Cliente cliente) {
		this.em.persist(cliente);
	}

	public Cliente buscarPorId(Long id) {
		return em.find(Cliente.class, id);
	}

}
