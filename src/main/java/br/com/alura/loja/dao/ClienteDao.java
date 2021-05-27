package br.com.alura.loja.dao;

import javax.persistence.EntityManager;

import br.com.alura.loja.modelo.Cliente;

public class ClienteDao {

	private EntityManager em;

	// Criando uma injeção de dependência para não deixar a classe ClienteDAO
	// responsável por criar e gerenciar o EntityManager. Ela só vai receber e
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
