package br.com.alura.spring.data.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import br.com.alura.spring.data.orm.Cargo;
import br.com.alura.spring.data.orm.Funcionario;
import br.com.alura.spring.data.orm.UnidadeTrabalho;
import br.com.alura.spring.data.repository.CargoRepository;
import br.com.alura.spring.data.repository.FuncionarioRepository;
import br.com.alura.spring.data.repository.UnidadeTrabalhoRepository;

@Service
public class CrudFuncionarioService {

	private Boolean system = true;
	private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

	private final CargoRepository cargoRepository;
	private final FuncionarioRepository funcionarioRepository;
	private final UnidadeTrabalhoRepository unidadeTrabalhoRepository;

	public CrudFuncionarioService(FuncionarioRepository funcionarioRepository, CargoRepository cargoRepository,
			UnidadeTrabalhoRepository unidadeTrabalhoRepository) {
		this.funcionarioRepository = funcionarioRepository;
		this.cargoRepository = cargoRepository;
		this.unidadeTrabalhoRepository = unidadeTrabalhoRepository;
	}

	public void inicial(Scanner scanner) {

		while (system) {

			System.out.println("Qual ação para funcionário deseja executar?");
			System.out.println("0 - Sair");
			System.out.println("1 - Salvar");
			System.out.println("2 - Atualizar");
			System.out.println("3 - Visualizar");
			System.out.println("4 - Deletar");

			int action = scanner.nextInt();

			switch (action) {

			case 1:
				salvar(scanner);
				break;
			case 2:
				atualizar(scanner);
				break;
			case 3:
				visualizar(scanner);
				break;
			case 4:
				deletar(scanner);
				break;
			default:
				system = false;
			}
		}
	}

	public void salvar(Scanner scanner) {
		System.out.println("Informe o nome do funcionário");
		String recebeNome = scanner.next();

		System.out.println("Informe o cpf");
		String cpf = scanner.next();

		System.out.println("Informe o salário");
		Double salario = scanner.nextDouble();

		System.out.println("Informe a data de contratação");
		String dataContratacao = scanner.next();

		System.out.println("Informe o cardoId");
		Integer cargoId = scanner.nextInt();

		List<UnidadeTrabalho> unidades = unidade(scanner);

		Funcionario funcionario = new Funcionario();
		funcionario.setNome(recebeNome);
		funcionario.setCpf(cpf);
		funcionario.setSalario(salario);
		funcionario.setDataContratacao(LocalDate.parse(dataContratacao, formatter));
		Optional<Cargo> cargo = cargoRepository.findById(cargoId);
		funcionario.setCargo(cargo.get());
		funcionario.setUnidadeTrabalhos(unidades);

		funcionarioRepository.save(funcionario);
		System.out.println("Salvo!");
	}

	private List<UnidadeTrabalho> unidade(Scanner scanner) {
		Boolean isTrue = true;
		List<UnidadeTrabalho> unidades = new ArrayList<>();

		while (isTrue) {
			System.out.println("Digite o unidadeId (Para sair digite 0)");
			Integer unidadeId = scanner.nextInt();

			if (unidadeId != 0) {
				Optional<UnidadeTrabalho> unidade = unidadeTrabalhoRepository.findById(unidadeId);
				unidades.add(unidade.get());
			} else {
				isTrue = false;
			}
		}
		return unidades;
	}

	public void atualizar(Scanner scanner) {

		System.out.println("Id do funcionário");
		Integer id = scanner.nextInt();

		System.out.println("Nome do funcionário");
		String nome = scanner.next();

		System.out.println("Informe o cpf");
		String cpf = scanner.next();

		System.out.println("Informe o salário");
		Double salario = scanner.nextDouble();

		System.out.println("Informe a data de contratação");
		String dataContratacao = scanner.next();

		System.out.println("Digite o cargoId");
		Integer cargoId = scanner.nextInt();

		Funcionario funcionario = new Funcionario();
		funcionario.setId(id);
		funcionario.setNome(nome);
		funcionario.setCpf(cpf);
		funcionario.setSalario(salario);
		funcionario.setDataContratacao(LocalDate.parse(dataContratacao, formatter));
		Optional<Cargo> cargo = cargoRepository.findById(cargoId);
		funcionario.setCargo(cargo.get());

		funcionarioRepository.save(funcionario);
		System.out.println("Atualizado!");
	}

	public void visualizar(Scanner scanner) {

		System.out.println("Qual página você deseja visualizar?");
		Integer page = scanner.nextInt();

		Pageable pageable = PageRequest.of(page, 5, Sort.by(Sort.Direction.ASC, "nome")); // cria uma request para
																							// retornar a página
		// selecionada pelo usuário

		Page<Funcionario> funcionarios = funcionarioRepository.findAll(pageable);

		System.out.println(funcionarios); // mostra o número de páginas existentes
		System.out.println("Página atual " + funcionarios.getNumber());
		System.out.println("Total de elementos " + funcionarios.getTotalElements());
		funcionarios.forEach(funcionario -> System.out.println(funcionario));
	}

	public void deletar(Scanner scanner) {

		System.out.println("Id do funcionário");
		int id = scanner.nextInt();

		funcionarioRepository.deleteById(id);
		System.out.println("Deletado!");
	}
}
